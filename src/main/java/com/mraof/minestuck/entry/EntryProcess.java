package com.mraof.minestuck.entry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.computer.SburbClientData;
import com.mraof.minestuck.computer.SburbServerData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.KernelspriteEntity;
import com.mraof.minestuck.network.EntryEffectPackets;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

@EventBusSubscriber(modid = Minestuck.MOD_ID)
public class EntryProcess
{
	public static final String WRONG_DIMENSION = "minestuck.entry.wrong_dimension";
	public static final String BUSY = "minestuck.entry.busy";
	public static final String CREATION_FAILED = "minestuck.entry.creation_failed";
	public static final String DIMENSION_MISSING = "minestuck.entry.dimension_missing";
	public static final String NOT_ALLOWED_HERE = "minestuck.entry.not_allowed_here";
	public static final String NO_REENTRY = "minestuck.entry.no_reentry";
	public static final String WRONG_DIMENSION_REENTRY = "minestuck.entry.wrong_dimension_reentry";
	public static final String TELEPORT_FAILED = "minestuck.entry.teleport_failed";
	public static final String COMMAND_BLOCK_DENIED = "minestuck.entry.command_block_denied";
	public static final String SKAIANET_DENIED = "minestuck.entry.skaianet_denied";
	public static final String NOT_YOUR_COMPUTER = "minestuck.entry.not_your_computer";
	public static final String NEEDS_COMPUTER = "minestuck.entry.needs_computer";
	public static final String EXCEPTION = "minestuck.entry.exception";
	
	private static final Logger LOGGER = LogManager.getLogger();
	public static final TicketType<Unit> CHUNK_TICKET_TYPE = TicketType.create("entry", (_left, _right) -> 0);
	
	// Total block operations handed out to all active entry processes per tick, split evenly between them
	private static final int BLOCKS_PER_TICK_TOTAL = 12_000;
	private static final int MIN_BLOCKS_PER_TICK_PER_PROCESS = 512;
	
	private enum Phase
	{
		WAITING, CHECKING, COPYING, TELEPORTING, REMOVING, DONE
	}
	
	private final PlayerIdentifier playerId;
	private final ServerLevel originLevel, landLevel;
	private final int artifactRange = MinestuckConfig.SERVER.artifactRange.get();
	
	private final int xDiff, yDiff, zDiff;
	private final BlockPos origin;
	private final boolean creative;
	private final long creationTime = System.currentTimeMillis();
	
	private Phase phase = Phase.WAITING;
	private long startTime;
	private boolean ticketRemoved = false;
	
	private ServerPlayer player;
	private Iterator<? extends BlockPos> blockIterator;
	private boolean foundComputer;
	
	private boolean wasInsideEntryArea;
	private AABB entityTeleportBB;
	private List<Entity> entities;
	
	private EntryProcess(ServerPlayer player, ServerLevel landLevel, BlockPos pos)
	{
		this.origin = pos;
		this.originLevel = (ServerLevel) player.level();
		this.landLevel = landLevel;
		
		int topY = MinestuckConfig.SERVER.adaptEntryBlockHeight.get() ? getTopHeight(player.serverLevel(), origin.getX(), origin.getY(), origin.getZ()) : origin.getY() + artifactRange;
		yDiff = 119 - topY; //the top block will end up being at y = 120 once in the land
		xDiff = -origin.getX();
		zDiff = -origin.getZ();
		
		playerId = IdentifierHandler.encode(player);
		creative = player.gameMode.isCreative();
	}
	
	public static void enter(ServerPlayer player)
	{
		enter(player, BlockPos.containing(player.getX(), player.getY(), player.getZ()));
	}
	
	public static void enter(ServerPlayer player, BlockPos pos)
	{
		if(player.level().dimension() == Level.NETHER)
		{
			player.sendSystemMessage(Component.translatable(WRONG_DIMENSION));
			return;
		}
		
		if(!TitleSelectionHook.performEntryCheck(player, pos))
			return;
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		if(playersEntering.contains(identifier))
		{
			player.sendSystemMessage(Component.translatable(BUSY));
			return;
		}
		
		ResourceKey<Level> land = SburbPlayerData.get(identifier, player.server).getLandDimensionIfEntered();
		
		if(land != null)
		{
			secondEntryTeleport(player, land);
			return;
		}
		
		ResourceKey<Level> landDimension = SburbHandler.prepareEntry(identifier, player.server);
		if(landDimension == null)
		{
			player.sendSystemMessage(Component.translatable(CREATION_FAILED));
			return;
		}
		
		ServerLevel landLevel = Objects.requireNonNull(player.getServer()).getLevel(landDimension);
		if(landLevel == null)
		{
			player.sendSystemMessage(Component.translatable(DIMENSION_MISSING));
			return;
		}
		
		EntryProcess process = new EntryProcess(player, landLevel, pos);
		if(!process.canModifyEntryBlocks(player, player.level(), pos))
		{
			player.sendSystemMessage(Component.translatable(NOT_ALLOWED_HERE));
			return;
		}
		
		landLevel.getChunkSource().addRegionTicket(CHUNK_TICKET_TYPE, new ChunkPos(0, 0), 0, Unit.INSTANCE);
		
		process.startTime = player.level().getGameTime() + MinestuckConfig.COMMON.entryDelay.get();
		playersEntering.add(identifier);
		activeProcesses.add(process);
		
		PacketDistributor.sendToAllPlayers(new EntryEffectPackets.Effect(player.level().dimension(), process.origin, process.artifactRange));
		LOGGER.info("Entry queued for {}", identifier);
	}
	
	private static void secondEntryTeleport(ServerPlayer player, ResourceKey<Level> land)
	{
		if(MinestuckConfig.SERVER.stopSecondEntry.get())
		{
			player.sendSystemMessage(Component.translatable(NO_REENTRY));
			return;
		}
		if(MSDimensions.isLandDimension(player.server, player.level().dimension()))
		{
			player.sendSystemMessage(Component.translatable(WRONG_DIMENSION_REENTRY));
			return;
		}
		
		ServerLevel landLevel = Objects.requireNonNull(player.getServer()).getLevel(land);
		if(landLevel == null)
		{
			player.sendSystemMessage(Component.translatable(DIMENSION_MISSING));
			return;
		}
		
		BlockPos spawn = new BlockPos(0, 0, 0);
		spawn = spawn.atY(landLevel.getChunk(spawn).getHeight(Heightmap.Types.MOTION_BLOCKING, spawn.getX(), spawn.getZ()) + 1);
		Teleport.teleportEntity(player, landLevel, spawn.getX() + 0.5F, spawn.getY(), spawn.getZ() + 0.5F, player.getYRot(), player.getXRot());
	}
	
	private static final Queue<EntryProcess> activeProcesses = new ArrayDeque<>();
	private static final Set<PlayerIdentifier> playersEntering = new HashSet<>();
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Pre event)
	{
		if(activeProcesses.isEmpty())
			return;
		
		long gameTime = event.getServer().overworld().getGameTime();
		int perProcessBudget = Math.max(MIN_BLOCKS_PER_TICK_PER_PROCESS, BLOCKS_PER_TICK_TOTAL / activeProcesses.size());
		
		Iterator<EntryProcess> iterator = activeProcesses.iterator();
		while(iterator.hasNext())
		{
			EntryProcess process = iterator.next();
			
			try
			{
				process.advance(gameTime, perProcessBudget);
			} catch(Exception e)
			{
				LOGGER.error("Exception during entry process for {}", process.playerId, e);
				process.notifyException();
				process.phase = Phase.DONE;
			}
			
			if(process.phase == Phase.DONE)
			{
				process.cleanup();
				iterator.remove();
			}
		}
	}
	
	private void advance(long gameTime, int budget)
	{
		if(phase != Phase.WAITING && phase != Phase.DONE && playerId.getPlayer(landLevel.getServer()) != player)
		{
			LOGGER.warn("Player disconnected during entry, aborting entry process for {}", playerId);
			phase = Phase.DONE;
			return;
		}
		
		switch(phase)
		{
			case WAITING -> tickWaiting(gameTime);
			case CHECKING -> tickChecking(budget);
			case COPYING -> tickCopying(budget);
			case TELEPORTING -> tickTeleporting();
			case REMOVING -> tickRemoving(budget);
			case DONE ->
			{
			}
		}
	}
	
	private void tickWaiting(long gameTime)
	{
		if(gameTime < startTime)
			return;
		
		landLevel.getChunkSource().removeRegionTicket(CHUNK_TICKET_TYPE, new ChunkPos(0, 0), 0, Unit.INSTANCE);
		ticketRemoved = true;
		PacketDistributor.sendToAllPlayers(new EntryEffectPackets.Clear());
		
		player = playerId.getPlayer(landLevel.getServer());
		if(player == null)
		{
			LOGGER.warn("Player left before entry was completed. Cancelling entry.");
			phase = Phase.DONE;
			return;
		}
		
		LOGGER.info("Checking entry block conditions");
		blockIterator = EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange).iterator();
		foundComputer = false;
		phase = Phase.CHECKING;
	}
	
	private void tickChecking(int budget)
	{
		int processed = 0;
		while(blockIterator.hasNext() && processed < budget)
		{
			BlockPos pos = blockIterator.next();
			processed++;
			
			if(!originLevel.isInWorldBounds(pos))
				continue;
			
			BlockState block = originLevel.getBlockState(pos);
			BlockEntity be = originLevel.getBlockEntity(pos);
			
			if(!creative && (block.is(Blocks.COMMAND_BLOCK) || block.is(Blocks.CHAIN_COMMAND_BLOCK) || block.is(Blocks.REPEATING_COMMAND_BLOCK)))
			{
				player.displayClientMessage(Component.translatable(COMMAND_BLOCK_DENIED), false);
				phase = Phase.DONE;
				return;
			} else if(block.is(MSBlocks.SKAIANET_DENIER.get()))
			{
				player.displayClientMessage(Component.translatable(SKAIANET_DENIED, player.getDisplayName().getString(), pos.toShortString()), false);
				phase = Phase.DONE;
				return;
			} else if(be instanceof ComputerBlockEntity computer)
			{
				if(computer.getOwner() != null && !computer.getOwner().appliesTo(player))
				{
					player.displayClientMessage(Component.translatable(NOT_YOUR_COMPUTER), false);
					phase = Phase.DONE;
					return;
				}
				
				foundComputer = true;
			}
		}
		
		if(!blockIterator.hasNext())
		{
			if(!foundComputer && MinestuckConfig.SERVER.needComputer.get())
			{
				player.displayClientMessage(Component.translatable(NEEDS_COMPUTER), false);
				phase = Phase.DONE;
				return;
			}
			
			LOGGER.info("Entry starting");
			blockIterator = EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange).iterator();
			phase = Phase.COPYING;
		}
	}
	
	private void tickCopying(int budget)
	{
		int processed = 0;
		while(blockIterator.hasNext() && processed < budget)
		{
			BlockPos sourcePos = blockIterator.next().immutable();
			processed++;
			
			if(!originLevel.isInWorldBounds(sourcePos))
				continue;
			
			BlockPos targetPos = sourcePos.offset(xDiff, yDiff, zDiff);
			
			LevelChunk sourceChunk = originLevel.getChunkAt(sourcePos);
			BlockState block = sourceChunk.getBlockState(sourcePos);
			
			if(block.is(Blocks.BEDROCK) || block.is(Blocks.NETHER_PORTAL))
				block = Blocks.AIR.defaultBlockState();
			
			BlockCopier.copyBlock(sourceChunk, sourcePos, block, landLevel.getChunkAt(targetPos), targetPos);
		}
		
		if(!blockIterator.hasNext())
		{
			blockIterator = null;
			phase = Phase.TELEPORTING;
		}
	}
	
	private void tickTeleporting()
	{
		try
		{
			wasInsideEntryArea = player.level() == originLevel && player.distanceToSqr(origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5) <= (double) artifactRange * artifactRange;
			
			if(Teleport.teleportEntity(player, landLevel) == null)
			{
				player.sendSystemMessage(Component.translatable(TELEPORT_FAILED));
				phase = Phase.DONE;
				return;
			}
			
			entityTeleportBB = player.getBoundingBox().inflate(artifactRange + 0.5);
			entities = originLevel.getEntities(player, entityTeleportBB);
			moveOrCopyEntities(entities, landLevel);
			
			blockIterator = EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange).iterator();
			phase = Phase.REMOVING;
		} catch(Exception e)
		{
			LOGGER.error("Exception when {} tried to enter their land.", player.getName().getString(), e);
			notifyException();
			phase = Phase.DONE;
		}
	}
	
	private void tickRemoving(int budget)
	{
		int processed = 0;
		while(blockIterator.hasNext() && processed < budget)
		{
			BlockPos pos = blockIterator.next();
			processed++;
			
			if(!originLevel.isInWorldBounds(pos))
				continue;
			
			removeBlockEntity(originLevel, pos, creative);
			
			if(MinestuckConfig.SERVER.entryCrater.get() && !originLevel.getBlockState(pos).is(Blocks.BEDROCK))
			{
				originLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
		
		if(!blockIterator.hasNext())
		{
			blockIterator = null;
			finishEntry();
		}
	}
	
	private void finishEntry()
	{
		try
		{
			if(wasInsideEntryArea)
				player.teleportTo(player.getX() + xDiff, player.getY() + yDiff, player.getZ() + zDiff);
			else
				player.teleportTo(origin.getX() + xDiff + 0.5, origin.getY() + yDiff, origin.getZ() + zDiff + 0.5);
			
			//Remove entities that were generated in the process of teleporting entities and removing blocks.
			// This is usually caused by "anchored" blocks being updated between the removal of their anchor and their own removal.
			if(!creative || MinestuckConfig.SERVER.entryCrater.get())
			{
				removeDroppedEntities(player, originLevel, entityTeleportBB, entities);
			}
			
			placeGates(landLevel);
			
			MSExtraData.get(landLevel).addPostEntryTask(new PostEntryTask(landLevel.dimension(), origin.getX() + xDiff, origin.getY() + yDiff, origin.getZ() + zDiff, artifactRange));
			
			SburbHandler.onEntry(player.server, player);
			
			LOGGER.info("Entry finished in {}ms", System.currentTimeMillis() - creationTime);
		} catch(Exception e)
		{
			LOGGER.error("Exception when {} tried to enter their land.", player.getName().getString(), e);
			notifyException();
		} finally
		{
			phase = Phase.DONE;
		}
	}
	
	private void notifyException()
	{
		if(player != null)
		{
			player.sendSystemMessage(Component.translatable(EXCEPTION, (landLevel.getServer().isDedicatedServer() ? "Check the console for the error message." : "Notify the server owner about this.")).withStyle(ChatFormatting.RED));
		}
	}
	
	private void cleanup()
	{
		playersEntering.remove(playerId);
		
		if(!ticketRemoved)
		{
			landLevel.getChunkSource().removeRegionTicket(CHUNK_TICKET_TYPE, new ChunkPos(0, 0), 0, Unit.INSTANCE);
			ticketRemoved = true;
		}
	}
	
	private static void removeDroppedEntities(ServerPlayer player, ServerLevel level0, AABB entityTeleportBB, List<Entity> entities)
	{
		List<Entity> removalList = level0.getEntities(player, entityTeleportBB);
		
		//We check if the old list contains the entity, because that means it was there before the entities were teleported and blocks removed.
		// This can be caused by them being outside the Entry radius but still within the AABB,
		// Or by the player being in creative mode, or having entryCrater disabled, etc.
		// Ultimately, this means that the entity has already been taken care of as much as it needs to be, and it is inappropriate to remove the entity.
		removalList.removeAll(entities);
		
		Iterator<Entity> iterator = removalList.iterator();
		if(MinestuckConfig.SERVER.entryCrater.get())
		{
			while(iterator.hasNext())
			{
				iterator.next().remove(Entity.RemovalReason.CHANGED_DIMENSION);
			}
		} else
		{
			while(iterator.hasNext())
			{
				Entity e = iterator.next();
				if(e instanceof ItemEntity)
					e.remove(Entity.RemovalReason.CHANGED_DIMENSION);
			}
		}
	}
	
	private void moveOrCopyEntities(List<Entity> entities, ServerLevel level1)
	{
		//The fudge here is to ensure that the AABB will always contain every entity meant to be moved.
		// As entities outside the radius will be excluded from transport anyway, this is fine.
		Iterator<Entity> iterator = entities.iterator();
		while(iterator.hasNext())
		{
			Entity entity = iterator.next();
			if(origin.distToCenterSqr(entity.getX(), entity.getY(), entity.getZ()) <= (double) artifactRange * artifactRange)
			{
				if(entity instanceof KernelspriteEntity kernelsprite)
					kernelsprite.setBoundOrigin(new BlockPos(0, GateHandler.GATE_HEIGHT_1, 0));
				
				if(MinestuckConfig.SERVER.entryCrater.get() || entity instanceof Player || !creative && entity instanceof ItemEntity)
				{
					try
					{
						if(entity instanceof Player && ServerEditHandler.getData((Player) entity) != null)
							ServerEditHandler.reset(ServerEditHandler.getData((Player) entity));
						else
						{
							Teleport.teleportEntity(entity, level1, entity.getX() + xDiff, entity.getY() + yDiff, entity.getZ() + zDiff);
						}
						//These entities should no longer be in the world, and this list is later used for entities that *should* remain.
						iterator.remove();
					} catch(RuntimeException e)
					{
						LOGGER.error("Exception while teleporting entity during entry {}:", entity, e);
					}
				} else    //Copy instead of teleport
				{
					Entity newEntity = entity.getType().create(level1);
					if(newEntity != null)
					{
						newEntity.restoreFrom(entity);
						newEntity.setPos(newEntity.getX() + xDiff, newEntity.getY() + yDiff, newEntity.getZ() + zDiff);
						level1.addFreshEntity(newEntity);
					}
				}
			}
		}
	}
	
	/**
	 * Determines if it is appropriate to remove the block entity in the specified location,
	 * and removes both the block entity and its corresponding block if so.
	 * This method is expressly designed to prevent drops from appearing when the block is removed.
	 * It will also deliberately trigger block updates based on the removal of the block entity's block.
	 *
	 * @param level    The world where the block entity is located
	 * @param pos      The position at which the block entity is located
	 * @param creative Whether or not creative-mode rules should be employed
	 */
	private static void removeBlockEntity(ServerLevel level, BlockPos pos, boolean creative)
	{
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if(blockEntity != null)
		{
			if(MinestuckConfig.SERVER.entryCrater.get() || !creative)
			{
				Block block = level.getBlockState(pos).getBlock();
				try
				{
					level.removeBlockEntity(pos);
					level.removeBlock(pos, true);
				} catch(Exception e)
				{
					LOGGER.warn("Exception encountered when removing block entity {} during entry:", block, e);
				}
			} else
			{
				//Avoid duplicating computer data when a computer is kept in the overworld
				if(blockEntity instanceof ComputerBlockEntity computer)
				{
					computer.getSburbClientData().ifPresent(SburbClientData::handleBeingDuplicated);
					computer.getSburbServerData().ifPresent(SburbServerData::handleBeingDuplicated);
				}
				else if(blockEntity instanceof TransportalizerBlockEntity)
					level.removeBlockEntity(pos);
			}
		}
	}
	
	private boolean canModifyEntryBlocks(Player player, Level level, BlockPos pos)
	{
		for(BlockPos bp : EntryBlockIterator.getHorizontal(pos.getX(), pos.getY(), pos.getZ(), artifactRange))
		{
			if(!level.mayInteract(player, bp))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Gives the Y-value of the highest non-air block within artifact range of the coordinates provided in the given world.
	 */
	private int getTopHeight(ServerLevel level, int x, int y, int z)
	{
		int maxY = y;
		for(BlockPos.MutableBlockPos pos : EntryBlockIterator.getHorizontal(x, y, z, artifactRange))
		{
			int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 1;
			if(height > maxY)
				maxY = height;
		}
		
		return maxY;
	}
	
	public static void placeGates(ServerLevel level)
	{
		GateBlock.placeGate(level, new BlockPos(0, GateHandler.GATE_HEIGHT_1, 0), GateHandler.Type.GATE_1, 0);
		GateBlock.placeGate(level, new BlockPos(0, GateHandler.GATE_HEIGHT_2, 0), GateHandler.Type.GATE_2, 0);
	}
}