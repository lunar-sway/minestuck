package com.mraof.minestuck.entry.meteor;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.MeteorEntity;
import com.mraof.minestuck.entity.MiniMeteorEntity;
import com.mraof.minestuck.network.MeteorPackets;
import com.mraof.minestuck.player.PlayerIdentifier;

import com.mraof.minestuck.skaianet.SkaianetData;
import com.mraof.minestuck.skaianet.Session;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Manages all active meteor countdowns.
 * One countdown per player (identified by the cruxtruder owner).
 */
public class MeteorManager extends SavedData
{
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DATA_NAME = Minestuck.MOD_ID + "_meteors";
	private final Set<String> impactPending = new HashSet<>();
	
	/**
	 * Total countdown: 4 min 13 sec = 253 seconds = 5060 ticks
	 */
	public static final int TOTAL_TICKS = 5060;
	/**
	 * Theme starts 41 seconds before impact = 820 ticks before end
	 */
	public static final int MUSIC_TRIGGER_TICKS = TOTAL_TICKS - 820;
	/**
	 * Final acceleration phase: 10-15 seconds before impact = ~300 ticks
	 */
	public static final int DASH_PHASE_TICKS = TOTAL_TICKS - 300;
	
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
	}
	
	public void respawnEntitiesForActiveCountdowns()
	{
		for(MeteorCountdown cd : new ArrayList<>(countdowns.values()))
		{
			MeteorEntity existing = findMeteorEntity(cd);
			if(existing == null)
			{
				spawnMeteorEntity(cd);
				broadcastCountdownStart(cd);
			}
		}
	}
	
	public int getTicksForMeteor(int entityId)
	{
		int result = countdowns.values().stream().filter(cd -> cd.getMeteorEntityId() == entityId).mapToInt(MeteorCountdown::getTicksElapsed).findFirst().orElse(-1);
		
		LOGGER.info("Lookup meteor {} -> {}", entityId, result);
		
		return result;
	}
	
	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries)
	{
		ListTag list = new ListTag();
		for(MeteorCountdown cd : countdowns.values())
			list.add(cd.write());
		tag.put("countdowns", list);
		return tag;
	}
	
	@Override
	public boolean isDirty()
	{
		return true;
	}
	
	public static MeteorManager get(MinecraftServer server)
	{
		return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(() -> new MeteorManager(server), (nbt, provider) -> new MeteorManager(server, nbt)), DATA_NAME);
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
			LOGGER.debug("Meteor countdown already running for {}", player.getUsername());
			return;
		}
		
		int sessionSize = getEnteredPlayerCount(player);
		
		MeteorCountdown countdown = new MeteorCountdown(player, cruxtruderPos, levelKey, sessionSize);
		countdowns.put(key, countdown);
		spawnMeteorEntity(countdown);
		broadcastCountdownStart(countdown);
		
		LOGGER.info("Meteor countdown started for {} (session size: {})", player.getUsername(), sessionSize);
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
		meteor.setOwnerKey(countdown.getPlayerKey());
		meteor.setMeteorSize(countdown.getMeteorSize());
		level.addFreshEntity(meteor);
		
		countdown.setMeteorEntityId(meteor.getId());
	}
	
	
	public void resendAllCountdowns(ServerPlayer player)
	{
		for(MeteorCountdown cd : countdowns.values())
		{
			PacketDistributor.sendToPlayer(player, new MeteorPackets.CountdownStart(cd.getPlayerKey(), cd.getCruxtruderPos(), cd.getLevelKey(), cd.getMeteorSize(), cd.getMeteorEntityId(), cd.getTicksElapsed()));
		}
	}
	
	private void broadcastCountdownStart(MeteorCountdown countdown)
	{
		mcServer.getPlayerList().getPlayers().forEach(p -> PacketDistributor.sendToPlayer(p, new MeteorPackets.CountdownStart(countdown.getPlayerKey(), countdown.getCruxtruderPos(), countdown.getLevelKey(), countdown.getMeteorSize(), countdown.getMeteorEntityId(), countdown.getTicksElapsed())));
	}
	
	public void tick()
	{
		List<String> toRemove = new ArrayList<>();
		
		for(MeteorCountdown cd : countdowns.values())
		{
			String key = cd.getPlayerKey();
			
			if(impactPending.contains(key))
			{
				handleImpact(cd);
				toRemove.add(key);
				continue;
			}
			
			cd.tick();
			processMilestones(cd);
			
			if(cd.isExpired())
			{
				MeteorEntity meteor = findMeteorEntity(cd);
				if(meteor != null)
					meteor.moveTick(TOTAL_TICKS);
				
				impactPending.add(key);
			}
		}
		
		toRemove.forEach(key -> {
			countdowns.remove(key);
			impactPending.remove(key);
		});
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
			broadcastMeteorUpdate(cd);
		}
	}
	
	private void triggerMusic(MeteorCountdown cd)
	{
		PlayerIdentifier pid = cd.getOwner();
		ServerPlayer player = pid.getPlayer(mcServer);
		if(player != null)
		{
			PacketDistributor.sendToPlayer(player, new MeteorPackets.PlayMeteorMusic(true));
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
		if(!MinestuckConfig.SERVER.meteorShower.get()) return;
		
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
		if(level == null) return;
		
		long existingCount = level.getEntities(EntityTypeTest.forClass(MiniMeteorEntity.class), e -> true).size();
		if(existingCount >= MinestuckConfig.SERVER.miniMeteorsCount.get()) return;
		
		BlockPos center = cd.getCruxtruderPos();
		int radius = MinestuckConfig.SERVER.artifactRange.get() + 5;
		
		double angle = level.random.nextDouble() * Math.PI * 2;
		double dist = level.random.nextDouble() * radius;
		double targetX = center.getX() + 0.5 + Math.cos(angle) * dist;
		double targetZ = center.getZ() + 0.5 + Math.sin(angle) * dist;
		double targetY = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, (int) targetX, (int) targetZ);
		
		double spawnOffsetX = (level.random.nextDouble() - 0.5) * 30;
		double spawnOffsetZ = (level.random.nextDouble() - 0.5) * 30;
		double spawnY = targetY + 60 + level.random.nextDouble() * 30;
		
		MiniMeteorEntity mini = new MiniMeteorEntity(MSEntityTypes.MINI_METEOR.get(), level);
		mini.moveTo(targetX + spawnOffsetX, spawnY, targetZ + spawnOffsetZ);
		mini.setTargetPos(new BlockPos((int) targetX, (int) targetY, (int) targetZ));
		level.addFreshEntity(mini);
	}
	
	private void broadcastMeteorUpdate(MeteorCountdown cd)
	{
		MeteorEntity meteor = findMeteorEntity(cd);
		if(meteor == null) return;
		
		MeteorPackets.MeteorPosition packet = new MeteorPackets.MeteorPosition(cd.getMeteorEntityId(), cd.getTicksElapsed());
		mcServer.getPlayerList().getPlayers().forEach(p -> PacketDistributor.sendToPlayer(p, packet));
	}
	
	private MeteorEntity findMeteorEntity(MeteorCountdown cd)
	{
		ServerLevel level = mcServer.getLevel(cd.getLevelKey());
		if(level == null) return null;
		var entity = level.getEntity(cd.getMeteorEntityId());
		return entity instanceof MeteorEntity me ? me : null;
	}
	
	private void handleImpact(MeteorCountdown cd)
	{
		ServerLevel level = mcServer.getLevel(cd.getLevelKey());
		if(level == null) return;
		
		BlockPos impactPos = cd.getCruxtruderPos();
		
		MeteorEntity meteor = findMeteorEntity(cd);
		if(meteor != null) meteor.discard();
		
		ServerPlayer player = cd.getOwner().getPlayer(mcServer);
		boolean hasEntered = player != null && com.mraof.minestuck.skaianet.SburbPlayerData.get(player).hasEntered();
		
		if(hasEntered)
		{
			float power = 3.0f + cd.getMeteorSize();
			level.explode(null, impactPos.getX() + 0.5, impactPos.getY() + 0.5, impactPos.getZ() + 0.5, power, Level.ExplosionInteraction.TNT);
		} else
		{
			createCrater(level, impactPos, cd);
			
			int range = com.mraof.minestuck.MinestuckConfig.SERVER.artifactRange.get() + 10;
			net.minecraft.world.phys.AABB aabb = new net.minecraft.world.phys.AABB(impactPos.getX() - range, impactPos.getY() - range, impactPos.getZ() - range, impactPos.getX() + range, impactPos.getY() + range, impactPos.getZ() + range);
			
			level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, aabb).forEach(entity -> {
				if(!(entity instanceof ServerPlayer))
				{
					entity.hurt(level.damageSources().explosion(null, null), Float.MAX_VALUE);
					if(entity.isAlive()) entity.kill();
				}
			});
			if(player != null) player.hurt(level.damageSources().explosion(null, null), Float.MAX_VALUE);
		}
		
		level.playSound(null, impactPos, com.mraof.minestuck.util.MSSoundEvents.METEOR_IMPACT.get(), net.minecraft.sounds.SoundSource.AMBIENT, 5.0f, 0.8f);
		
		if(player != null)
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, new com.mraof.minestuck.network.MeteorPackets.PlayMeteorMusic(false));
		
		mcServer.getPlayerList().getPlayers().forEach(p -> net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(p, new com.mraof.minestuck.network.MeteorPackets.MeteorRemoved(cd.getMeteorEntityId())));
	}
	
	private void createCrater(ServerLevel level, BlockPos center, MeteorCountdown cd)
	{
		int range = com.mraof.minestuck.MinestuckConfig.SERVER.artifactRange.get();
		float meteorSize = cd.getMeteorSize();
		int craterRadius = (int) (range * meteorSize);
		
		for(BlockPos pos : net.minecraft.core.BlockPos.betweenClosed(center.offset(-craterRadius, -craterRadius, -craterRadius), center.offset(craterRadius, craterRadius, craterRadius)))
		{
			double dx = pos.getX() - center.getX();
			double dy = pos.getY() - center.getY();
			double dz = pos.getZ() - center.getZ();
			
			if(dx * dx + dy * dy + dz * dz <= craterRadius * craterRadius)
			{
				if(!level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.BEDROCK))
				{
					level.setBlock(pos.immutable(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), net.minecraft.world.level.block.Block.UPDATE_CLIENTS);
				}
			}
		}
		
		for(int i = 0; i < 5 + (int) (meteorSize * 2); i++)
		{
			double ox = (level.random.nextDouble() - 0.5) * craterRadius * 2;
			double oz = (level.random.nextDouble() - 0.5) * craterRadius * 2;
			level.explode(null, center.getX() + ox, center.getY() + 2, center.getZ() + oz, 4.0f + meteorSize, Level.ExplosionInteraction.TNT);
		}
	}
}