package com.mraof.minestuck.entry.meteor;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.KernelspriteEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.MeteorEntity;
import com.mraof.minestuck.entity.MiniMeteorEntity;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SkaianetData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

import static com.mraof.minestuck.MinestuckConfig.SERVER;
import static com.mraof.minestuck.network.MeteorPackets.*;
import static com.mraof.minestuck.util.MSSoundEvents.*;
import static net.minecraft.core.BlockPos.*;
import static net.minecraft.world.level.block.Block.*;
import static net.minecraft.world.level.block.Blocks.*;
import static net.neoforged.neoforge.network.PacketDistributor.sendToPlayer;

/**
 * Manages all active meteor countdowns.
 * One countdown per player (identified by the cruxtruder owner).
 */
public class MeteorManager extends SavedData
{
	
	/**
	 * Total countdown: 4 min 13 sec = 253 seconds = 5060 ticks
	 */
	public static final int TOTAL_TICKS = 5060;
	/**
	 * Theme starts 55 seconds before impact = 1140 ticks before end
	 */
	public static final int MUSIC_TRIGGER_TICKS = TOTAL_TICKS - 1140;
	/**
	 * Final acceleration phase: 10-15 seconds before impact = ~300 ticks
	 */
	public static final int DASH_PHASE_TICKS = TOTAL_TICKS - 300;
	private static final int CRATER_BLOCKS_PER_TICK_TOTAL = 1_500;
	private static final int MIN_CRATER_BLOCKS_PER_TICK = 200;
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DATA_NAME = Minestuck.MOD_ID + "_meteors";
	private final Set<String> impactPending = new HashSet<>();
	private final Map<String, CraterJob> activeImpacts = new HashMap<>();
	// Map: player UUID string -> countdown data
	private final Map<String, MeteorCountdown> countdowns = new HashMap<>();
	private final MinecraftServer mcServer;
	
	public MeteorManager(MinecraftServer server)
	{
		this.mcServer = server;
	}
	
	public MeteorManager(MinecraftServer server, CompoundTag tag)
	{
		this.mcServer = server;
		ListTag list = tag.getList("countdowns", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			MeteorCountdown cd = MeteorCountdown.read(list.getCompound(i));
			if(cd != null) countdowns.put(cd.getPlayerKey(), cd);
		}
		
		ListTag impacts = tag.getList("activeImpacts", Tag.TAG_COMPOUND);
		for(int i = 0; i < impacts.size(); i++)
		{
			CompoundTag jobTag = impacts.getCompound(i);
			CraterJob job = CraterJob.read(jobTag, server);
			if(job != null && !job.isFinished())
				activeImpacts.put(jobTag.getString("key"), job);
		}
	}
	
	public static MeteorManager get(MinecraftServer server)
	{
		return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(() -> new MeteorManager(server), (nbt, provider) -> new MeteorManager(server, nbt)), DATA_NAME);
	}
	
	public void respawnEntitiesForActiveCountdowns()
	{
		for(MeteorCountdown cd : new ArrayList<>(countdowns.values()))
		{
			MeteorEntity existing = findMeteorEntity(cd);
			if(existing == null)
			{
				spawnMeteorEntity(cd);
				sendCountdownStart(cd);
			}
		}
	}
	
	public int getTicksForMeteor(int entityId)
	{
		return countdowns.values().stream().filter(cd -> cd.getMeteorEntityId() == entityId).mapToInt(MeteorCountdown::getTicksElapsed).findFirst().orElse(-1);
	}
	
	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries)
	{
		ListTag list = new ListTag();
		for(MeteorCountdown cd : countdowns.values())
			list.add(cd.write());
		tag.put("countdowns", list);
		
		ListTag impacts = new ListTag();
		for(Map.Entry<String, CraterJob> entry : activeImpacts.entrySet())
		{
			CompoundTag jobTag = entry.getValue().write();
			jobTag.putString("key", entry.getKey());
			impacts.add(jobTag);
		}
		tag.put("activeImpacts", impacts);
		
		return tag;
	}
	
	@Override
	public boolean isDirty()
	{
		return true;
	}
	
	/**
	 * Called when a player removes the lid from their cruxtruder.
	 * Starts a new countdown for the player.
	 *
	 * @param player        The owner of the cruxtruder
	 * @param cruxtruderPos Position of the cruxtruder main block
	 */
	public void startCountdown(PlayerIdentifier player, BlockPos cruxtruderPos, ResourceKey<Level> levelKey)
	{
		String key = player.getCommandString();
		if(countdowns.containsKey(key))
		{
			return;
		}
		int sessionSize = getEnteredPlayerCount(player);
		
		MeteorCountdown countdown = new MeteorCountdown(player, cruxtruderPos, levelKey, sessionSize);
		countdowns.put(key, countdown);
		spawnMeteorEntity(countdown);
		sendCountdownStart(countdown);
	}
	
	public void cancelCountdown(PlayerIdentifier playerId)
	{
		String key = playerId.getCommandString();
		
		MeteorCountdown cd = countdowns.remove(key);
		impactPending.remove(key);
		
		if(cd == null) return;
		
		MeteorEntity meteor = findMeteorEntity(cd);
		if(meteor != null) meteor.discard();
		
		ServerPlayer player = playerId.getPlayer(mcServer);
		if(player != null)
		{
			sendToPlayer(player, new MeteorRemoved(cd.getMeteorEntityId()));
		}
	}
	
	/**
	 * Count entered players in the same session as this player
	 */
	private int getEnteredPlayerCount(PlayerIdentifier player)
	{
		try
		{
			SkaianetData data = SkaianetData.get(mcServer);
			Optional<Session> session = data.sessionHandler.getSession(player);
			if(session.isEmpty()) return 1;
			return (int) session.get().getPlayers().stream().filter(p -> data.getOrCreateData(p).hasEntered()).count();
		} catch(Exception e)
		{
			return 1;
		}
	}
	
	private void spawnMeteorEntity(MeteorCountdown countdown)
	{
		ServerLevel level = mcServer.getLevel(countdown.getLevelKey());
		if(level == null) return;
		
		BlockPos target = countdown.getCruxtruderPos();
		
		double startX = target.getX() + 0.5;
		double startZ = target.getZ() + 0.5;
		
		double startY = MeteorEntity.getSpawnHeightY(target);
		
		MeteorEntity meteor = new MeteorEntity(MSEntityTypes.METEOR.get(), level);
		
		meteor.moveTo(startX, startY, startZ, 0.0F, 90.0F);
		
		meteor.setTargetPos(target);
		meteor.setOwner(countdown.getOwner());
		meteor.setMeteorSize(countdown.getMeteorSize());
		level.addFreshEntity(meteor);
		
		countdown.setMeteorEntityId(meteor.getId());
	}
	
	
	public void resendAllCountdowns(ServerPlayer player)
	{
		for(MeteorCountdown cd : countdowns.values())
		{
			if(cd.getOwner().appliesTo(player))
			{
				sendToPlayer(player, new CountdownStart(cd.getPlayerKey(), cd.getCruxtruderPos(), cd.getLevelKey(), cd.getMeteorSize(), cd.getMeteorEntityId(), cd.getTicksElapsed()));
			}
		}
	}
	
	private void sendCountdownStart(MeteorCountdown countdown)
	{
		ServerPlayer player = countdown.getOwner().getPlayer(mcServer);
		if(player != null)
		{
			sendToPlayer(player, new CountdownStart(countdown.getPlayerKey(), countdown.getCruxtruderPos(), countdown.getLevelKey(), countdown.getMeteorSize(), countdown.getMeteorEntityId(), countdown.getTicksElapsed()));
		}
	}
	
	public void tick()
	{
		List<String> toRemove = new ArrayList<>();
		
		for(MeteorCountdown cd : countdowns.values())
		{
			String key = cd.getPlayerKey();
			
			try
			{
				if(impactPending.contains(key))
				{
					LOGGER.info("[Meteor] Impact pending for {}, starting impact processing", key);
					startImpact(cd);
					toRemove.add(key);
					continue;
				}
				
				cd.tick();
				processMilestones(cd);
				
				if(cd.isExpired())
				{
					MeteorEntity meteor = findMeteorEntity(cd);
					if(meteor != null) meteor.moveTick(TOTAL_TICKS);
					
					impactPending.add(key);
				}
			} catch(Exception e)
			{
				toRemove.add(key);
			}
		}
		
		toRemove.forEach(key -> {
			countdowns.remove(key);
			impactPending.remove(key);
		});
		
		tickImpacts();
	}
	
	private void processMilestones(MeteorCountdown cd)
	{
		int ticks = cd.getTicksElapsed();
		
		if(ticks == MUSIC_TRIGGER_TICKS && !cd.isMusicTriggered())
		{
			cd.setMusicTriggered(true);
			triggerMusic(cd);
		}
		
		// Mini meteor spawning andd frequency increases over time
		spawnMiniMeteorsIfNeeded(cd);
		
		if(ticks % 10 == 0)
		{
			sendMeteorUpdate(cd);
		}
	}
	
	private void triggerMusic(MeteorCountdown cd)
	{
		PlayerIdentifier pid = cd.getOwner();
		ServerPlayer player = pid.getPlayer(mcServer);
		if(player != null)
		{
			sendToPlayer(player, new PlayMeteorMusic(true));
		} else
		{
		}
	}
	
	/**
	 * Mini meteor spawn rate increases as timer progresses like:
	 * Early phase: every ~160 ticks
	 * Mid phase: every ~80 ticks
	 * Late phase: every ~20 ticks
	 */
	private void spawnMiniMeteorsIfNeeded(MeteorCountdown cd)
	{
		if(!SERVER.meteorShower.get()) return;
		
		int ticks = cd.getTicksElapsed();
		int interval;
		
		if(ticks < TOTAL_TICKS * 0.3)
		{
			interval = 160;
		} else if(ticks < TOTAL_TICKS * 0.6)
		{
			interval = 80;
		} else if(ticks < TOTAL_TICKS * 0.85)
		{
			interval = 40;
		} else
		{
			interval = 20;
		}
		
		if(ticks % interval == 0)
		{
			spawnMiniMeteor(cd);
		}
	}
	
	private void spawnMiniMeteor(MeteorCountdown cd)
	{
		ServerLevel level = mcServer.getLevel(cd.getLevelKey());
		if(level == null)
		{
			return;
		}
		
		BlockPos center = cd.getCruxtruderPos();
		int radius = SERVER.artifactRange.get() + 5;
		
		AABB nearby = new AABB(center).inflate(radius + 30);
		long existingCount = level.getEntities(EntityTypeTest.forClass(MiniMeteorEntity.class), nearby, e -> true).size();
		if(existingCount >= SERVER.miniMeteorsCount.get()) return;
		
		double angle = level.random.nextDouble() * Math.PI * 2;
		double dist = level.random.nextDouble() * radius;
		double targetX = center.getX() + 0.5 + Math.cos(angle) * dist;
		double targetZ = center.getZ() + 0.5 + Math.sin(angle) * dist;
		double targetY = level.getHeight(Types.MOTION_BLOCKING, (int) targetX, (int) targetZ);
		
		double spawnOffsetX = (level.random.nextDouble() - 0.5) * 30;
		double spawnOffsetZ = (level.random.nextDouble() - 0.5) * 30;
		double spawnY = targetY + 60 + level.random.nextDouble() * 30;
		
		MiniMeteorEntity mini = new MiniMeteorEntity(MSEntityTypes.MINI_METEOR.get(), level);
		mini.moveTo(targetX + spawnOffsetX, spawnY, targetZ + spawnOffsetZ);
		mini.setTargetPos(new BlockPos((int) targetX, (int) targetY, (int) targetZ));
		level.addFreshEntity(mini);
	}
	
	private void sendMeteorUpdate(MeteorCountdown cd)
	{
		MeteorEntity meteor = findMeteorEntity(cd);
		if(meteor == null) return;
		
		ServerPlayer player = cd.getOwner().getPlayer(mcServer);
		if(player == null) return;
		
		sendToPlayer(player, new MeteorPosition(cd.getMeteorEntityId(), cd.getTicksElapsed()));
	}
	
	private MeteorEntity findMeteorEntity(MeteorCountdown cd)
	{
		ServerLevel level = mcServer.getLevel(cd.getLevelKey());
		if(level == null) return null;
		var entity = level.getEntity(cd.getMeteorEntityId());
		return entity instanceof MeteorEntity me ? me : null;
	}
	
	private void startImpact(MeteorCountdown cd)
	{
		ServerLevel level = mcServer.getLevel(cd.getLevelKey());
		if(level == null)
		{
			return;
		}
		BlockPos impactPos = cd.getCruxtruderPos();
		MeteorEntity meteor = findMeteorEntity(cd);
		if(meteor != null) meteor.discard();
		
		ServerPlayer player = cd.getOwner().getPlayer(mcServer);
		boolean hasEntered = player != null && (SburbPlayerData.get(player).hasEntered() || player.level().dimension() != cd.getLevelKey());
		int baseCraterRadius = 14;
		int craterRadius = Math.min(30, Math.round(baseCraterRadius * cd.getMeteorSize()));
		
		if(hasEntered)
		{
			float power = 3.0f + cd.getMeteorSize();
			level.explode(null, impactPos.getX() + 0.5, impactPos.getY() + 0.5, impactPos.getZ() + 0.5, power, Level.ExplosionInteraction.TNT);
		} else
		{
			float meteorSize = cd.getMeteorSize();
			int burstCount = 3 + (int) meteorSize;
			for(int i = 0; i < burstCount; i++)
			{
				double ox = (level.random.nextDouble() - 0.5) * craterRadius * 2;
				double oz = (level.random.nextDouble() - 0.5) * craterRadius * 2;
				level.explode(null, impactPos.getX() + ox, impactPos.getY() + 2, impactPos.getZ() + oz, 4.0f + meteorSize, Level.ExplosionInteraction.TNT);
			}
			
			// Guaranteed-clear narrow shaft straight up from the impact point (unlike burst explosions,
			// which destroy blocks probabilistically) - so a house built directly above doesn't survive
			// just because explosions didn't roll well. Small radius keeps this cheap enough to do in one
			// go, same as everything else above.
			int shaftRadius = 5;
			int shaftHeight = 80;
			for(int dy = 1; dy <= shaftHeight; dy++)
			{
				for(int dx = -shaftRadius; dx <= shaftRadius; dx++)
				{
					for(int dz = -shaftRadius; dz <= shaftRadius; dz++)
					{
						if(dx * dx + dz * dz > shaftRadius * shaftRadius) continue;
						BlockPos shaftPos = impactPos.offset(dx, dy, dz);
						net.minecraft.world.level.block.state.BlockState shaftState = level.getBlockState(shaftPos);
						if(shaftState.getDestroySpeed(level, shaftPos) >= 0)
							level.setBlock(shaftPos, AIR.defaultBlockState(), UPDATE_CLIENTS);
					}
				}
			}
			
			int entityRange = SERVER.artifactRange.get() + 10;
			AABB aabb = new AABB(impactPos.getX() - entityRange, impactPos.getY() - entityRange, impactPos.getZ() - entityRange, impactPos.getX() + entityRange, impactPos.getY() + entityRange, impactPos.getZ() + entityRange);
			
			List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);
			
			PlayerData ownerData = (player != null && !hasEntered) ? PlayerData.get(cd.getOwner(), level.getServer()) : null;
			for(LivingEntity entity : nearbyEntities)
			{
				if(entity instanceof ServerPlayer otherPlayer)
				{
					if(otherPlayer != player && !SburbPlayerData.get(otherPlayer).hasEntered())
						otherPlayer.kill();
					continue;
				}
				
				if(entity instanceof KernelspriteEntity)
				{
					entity.discard();
					if(ownerData != null)
						ownerData.setData(MSAttachments.HAS_KERNELSPRITE, false);
					continue;
				}
				
				entity.kill();
			}
			
			if(player != null)
			{
				player.kill();
			}
			CraterJob job = new CraterJob(level, impactPos, craterRadius);
			activeImpacts.put(cd.getPlayerKey(), job);
		}
		
		level.playSound(null, impactPos, METEOR_IMPACT.get(), net.minecraft.sounds.SoundSource.AMBIENT, 10.0f, 0.8f);
		if(player != null)
		{
			sendToPlayer(player, new PlayMeteorMusic(false));
			sendToPlayer(player, new MeteorRemoved(cd.getMeteorEntityId()));
		}
	}
	
	private void tickImpacts()
	{
		if(activeImpacts.isEmpty())
			return;
		
		int perJobBudget = Math.max(MIN_CRATER_BLOCKS_PER_TICK, CRATER_BLOCKS_PER_TICK_TOTAL / activeImpacts.size());
		
		Iterator<Map.Entry<String, CraterJob>> iterator = activeImpacts.entrySet().iterator();
		while(iterator.hasNext())
		{
			Map.Entry<String, CraterJob> entry = iterator.next();
			CraterJob job = entry.getValue();
			
			try
			{
				job.tick(perJobBudget);
			} catch(Exception e)
			{
				iterator.remove();
				continue;
			}
			
			if(job.isFinished())
			{
				iterator.remove();
			}
		}
	}
	
	/**
	 * Spreads the crater block removal for a single meteor impact across multiple ticks instead of clearing
	 * the whole sphere in one go, using the same per-tick budget pattern as EntryProcess.
	 */
	private static final class CraterJob
	{
		private final ServerLevel level;
		private final BlockPos impactPos;
		private final int craterRadius;
		private final long estimatedTotalPositions;
		
		private long blocksProcessed = 0;
		private int tickCallCount = 0;
		private Iterator<? extends BlockPos> blockIterator;
		private boolean finished = false;
		
		CraterJob(ServerLevel level, BlockPos impactPos, int craterRadius)
		{
			this.level = level;
			this.impactPos = impactPos;
			this.craterRadius = craterRadius;
			this.estimatedTotalPositions = estimateTotalPositions(craterRadius);
			this.blockIterator = newIterator();
		}
		
		private CraterJob(ServerLevel level, BlockPos impactPos, int craterRadius, long alreadyProcessed)
		{
			this.level = level;
			this.impactPos = impactPos;
			this.craterRadius = craterRadius;
			this.estimatedTotalPositions = estimateTotalPositions(craterRadius);
			this.blockIterator = newIterator();
			fastForward(alreadyProcessed);
		}
		
		private static long estimateTotalPositions(int craterRadius)
		{
			long sideXZ = (long) craterRadius * 2 + 1;
			long heightY = craterRadius + 1L;
			return sideXZ * sideXZ * heightY;
		}
		
		private Iterator<? extends BlockPos> newIterator()
		{
			return betweenClosed(impactPos.offset(-craterRadius, -craterRadius, -craterRadius), impactPos.offset(craterRadius, 0, craterRadius)).iterator();
		}
		
		private void fastForward(long count)
		{
			for(long i = 0; i < count && blockIterator.hasNext(); i++)
			{
				blockIterator.next();
				blocksProcessed++;
			}
			
			if(!blockIterator.hasNext())
			{
				blockIterator = null;
				finished = true;
			}
		}
		
		void tick(int budget)
		{
			tickCallCount++;
			
			if(blockIterator == null)
			{
				finished = true;
				return;
			}
			
			int processed = 0;
			while(blockIterator.hasNext() && processed < budget)
			{
				BlockPos pos = blockIterator.next();
				processed++;
				blocksProcessed++;
				
				double dx = pos.getX() - impactPos.getX();
				double dy = pos.getY() - impactPos.getY();
				double dz = pos.getZ() - impactPos.getZ();
				
				if(dx * dx + dy * dy + dz * dz <= (double) craterRadius * craterRadius)
				{
					net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
					if(state.getDestroySpeed(level, pos) >= 0)
					{
						level.setBlock(pos.immutable(), AIR.defaultBlockState(), UPDATE_CLIENTS);
					}
				}
			}
			
			if(!blockIterator.hasNext())
			{
				blockIterator = null;
				finished = true;
			}
		}
		
		boolean isFinished()
		{
			return finished;
		}
		
		CompoundTag write()
		{
			CompoundTag tag = new CompoundTag();
			tag.putInt("x", impactPos.getX());
			tag.putInt("y", impactPos.getY());
			tag.putInt("z", impactPos.getZ());
			tag.putInt("radius", craterRadius);
			tag.putLong("processed", blocksProcessed);
			Level.RESOURCE_KEY_CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, level.dimension()).resultOrPartial(LOGGER::error).ifPresent(t -> tag.put("level", t));
			return tag;
		}
		
		@Nullable
		static CraterJob read(CompoundTag tag, MinecraftServer server)
		{
			ResourceKey<Level> levelKey = Level.RESOURCE_KEY_CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, tag.get("level")).resultOrPartial(LOGGER::error).orElse(Level.OVERWORLD);
			ServerLevel level = server.getLevel(levelKey);
			if(level == null) return null;
			
			BlockPos impactPos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
			int radius = tag.getInt("radius");
			long processed = tag.getLong("processed");
			
			return new CraterJob(level, impactPos, radius, processed);
		}
	}
}