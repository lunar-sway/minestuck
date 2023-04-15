package com.mraof.minestuck.entry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.EntryEffectPackets;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class EntryProcess
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final TicketType<Unit> CHUNK_TICKET_TYPE = TicketType.create("entry", (_left, _right) -> 0);
	
	private final PlayerIdentifier playerId;
	private final ServerLevel landLevel;
	private final int artifactRange = MinestuckConfig.SERVER.artifactRange.get();
	
	private final int xDiff, yDiff, zDiff;
	private final BlockPos origin;
	private final boolean creative;
	
	private EntryProcess(ServerPlayer player, ServerLevel landLevel)
	{
		this.origin = player.blockPosition();
		this.landLevel = landLevel;
		
		int topY = MinestuckConfig.SERVER.adaptEntryBlockHeight.get() ? getTopHeight(player.getLevel(), origin.getX(), origin.getY(), origin.getZ()) : origin.getY() + artifactRange;
		yDiff = 119 - topY; //the top block will end up being at y = 120 once in the land
		xDiff = -origin.getX();
		zDiff = -origin.getZ();
		
		playerId = IdentifierHandler.encode(player);
		creative = player.gameMode.isCreative();
	}
	
	public static void onArtifactActivated(ServerPlayer player)
	{
		long time = System.currentTimeMillis();
		if(player.level.dimension() == Level.NETHER)
			return;
		if(!TitleSelectionHook.performEntryCheck(player))
			return;
		if(waitingProcess != null)
		{
			player.sendSystemMessage(Component.literal("Someone else is already entering."));
			return;
		}
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Optional<SburbConnection> c = SkaianetHandler.get(player.level).getPrimaryConnection(identifier, true);
		
		if(c.isPresent() && c.get().hasEntered())
		{
			secondEntryTeleport(player, c.get().getClientDimension());
			return;
		}
		
		ResourceKey<Level> landDimension = SkaianetHandler.get(player.level).prepareEntry(identifier);
		if(landDimension == null)
		{
			player.sendSystemMessage(Component.literal("Something went wrong while creating your Land. More details in the server console."));
			return;
		}
		
		ServerLevel landLevel = Objects.requireNonNull(player.getServer()).getLevel(landDimension);
		if(landLevel == null)
			return;
		
		EntryProcess process = new EntryProcess(player, landLevel);
		if(!process.canModifyEntryBlocks(player.level, player))
		{
			player.sendSystemMessage(Component.literal("You are not allowed to enter here."));    //TODO translation key
			return;
		}
		
		landLevel.getChunkSource().addRegionTicket(CHUNK_TICKET_TYPE, new ChunkPos(0, 0), 0, Unit.INSTANCE);
		
		waitingProcess = process;
		startTime = player.level.getGameTime() + MinestuckConfig.SERVER.entryDelay.get();
		MSPacketHandler.sendToAll(new EntryEffectPackets.Effect(player.level.dimension(), process.origin, process.artifactRange));
		LOGGER.info("Entry prep done in {}ms", System.currentTimeMillis() - time);
	}
	
	private static void secondEntryTeleport(ServerPlayer player, ResourceKey<Level> land)
	{
		if(MinestuckConfig.SERVER.stopSecondEntry.get() || MSDimensions.isLandDimension(player.server, player.level.dimension()))
			return;
		
		ServerLevel landWorld = Objects.requireNonNull(player.getServer()).getLevel(land);
		if(landWorld == null)
			return;
		
		//Teleports the player to their home in the Medium, without any bells or whistles.
		BlockPos pos = new BlockPos(0, 100, 0);
		Teleport.teleportEntity(player, landWorld, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, player.getYRot(), player.getXRot());
	}
	
	private static EntryProcess waitingProcess;
	private static long startTime;
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START)
		{
			//noinspection resource
			if(waitingProcess != null && startTime <= event.getServer().overworld().getGameTime())
			{
				waitingProcess.landLevel.getChunkSource().removeRegionTicket(CHUNK_TICKET_TYPE, new ChunkPos(0, 0), 0, Unit.INSTANCE);
				waitingProcess.runEntry();
				waitingProcess = null;
				MSPacketHandler.sendToAll(new EntryEffectPackets.Clear());
			}
		}
	}
	
	private void runEntry()
	{
		long time = System.currentTimeMillis();
		ServerPlayer player = playerId.getPlayer(landLevel.getServer());
		if(player == null)
		{
			LOGGER.warn("Player left before entry was completed. Cancelling entry.");
			return;
		}
		
		try
		{
			ServerLevel oldLevel = (ServerLevel) player.level;
			
			LOGGER.info("Checking entry block conditions");
			
			if(doesBlocksStopEntry(player, oldLevel))
				return;
			
			LOGGER.info("Entry starting");
			copyBlocks(oldLevel, landLevel);
			
			if(Teleport.teleportEntity(player, landLevel) == null)
			{
				player.sendSystemMessage(Component.literal("Entry failed. Unable to teleport you!"));
				return;
			}
			
			finalizeEntry(player, oldLevel, landLevel);
			SkaianetHandler.get(player.level).onEntry(playerId);
			LOGGER.info("Entry finished in {}ms", System.currentTimeMillis() - time);
			
		} catch(Exception e)
		{
			LOGGER.error("Exception when {} tried to enter their land.", player.getName().getString(), e);
			player.sendSystemMessage(Component.literal("[Minestuck] Something went wrong during entry. " + (landLevel.getServer().isDedicatedServer() ? "Check the console for the error message." : "Notify the server owner about this.")).withStyle(ChatFormatting.RED));
		}
	}
	
	private boolean doesBlocksStopEntry(ServerPlayer player, ServerLevel level)
	{
		boolean foundComputer = false;
		for(BlockPos pos : EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange))
		{
			if(!level.isInWorldBounds(pos))
				continue;
			
			BlockState block = level.getBlockState(pos);
			BlockEntity be = level.getBlockEntity(pos);
			
			if(!creative && (block.is(Blocks.COMMAND_BLOCK) || block.is(Blocks.CHAIN_COMMAND_BLOCK) || block.is(Blocks.REPEATING_COMMAND_BLOCK)))
			{
				player.displayClientMessage(Component.literal("You are not allowed to move command blocks."), false);
				return true;
			} else if(block.is(MSBlocks.SKAIANET_DENIER.get()))
			{
				player.displayClientMessage(Component.literal("Network error (413): Skaianet - failed to Enter user " + player.getDisplayName().getString() + ". Entry denial device used at global coordinates: " + pos.toShortString()), false);
				return true;
			} else if(be instanceof ComputerBlockEntity computer)
			{
				if(!computer.owner.appliesTo(player))
				{
					player.displayClientMessage(Component.literal("You are not allowed to move other players' computers."), false);
					return true;
				}
				
				foundComputer = true;
			}
		}
		
		if(!foundComputer && MinestuckConfig.SERVER.needComputer.get())
		{
			player.displayClientMessage(Component.literal("There is no computer in range."), false);
			return true;
		}
		
		return false;
	}
	
	private void copyBlocks(ServerLevel sourceLevel, ServerLevel targetLevel)
	{
		for(BlockPos sourcePos : EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange))
		{
			if(!sourceLevel.isInWorldBounds(sourcePos))
				continue;
			
			sourcePos = sourcePos.immutable();
			BlockPos targetPos = sourcePos.offset(xDiff, yDiff, zDiff);
			
			LevelChunk sourceChunk = sourceLevel.getChunkAt(sourcePos);
			BlockState block = sourceChunk.getBlockState(sourcePos);
			
			if(block.is(Blocks.BEDROCK) || block.is(Blocks.NETHER_PORTAL))
				block = Blocks.AIR.defaultBlockState();
			
			BlockCopier.copyBlock(sourceChunk, sourcePos, block, targetLevel.getChunkAt(targetPos), targetPos);
		}
	}
	
	private void finalizeEntry(ServerPlayer player, ServerLevel level0, ServerLevel level1)
	{
		AABB entityTeleportBB = player.getBoundingBox().inflate(artifactRange + 0.5);
		List<Entity> entities = level0.getEntities(player, entityTeleportBB);
		moveOrCopyEntities(entities, level1);
		
		removeOriginalBlocks(level0);
		
		player.teleportTo(player.getX() + xDiff, player.getY() + yDiff, player.getZ() + zDiff);
		
		//Remove entities that were generated in the process of teleporting entities and removing blocks.
		// This is usually caused by "anchored" blocks being updated between the removal of their anchor and their own removal.
		if(!creative || MinestuckConfig.SERVER.entryCrater.get())
		{
			removeDroppedEntities(player, level0, entityTeleportBB, entities);
		}
		
		placeGates(level1);
		
		MSExtraData.get(level1).addPostEntryTask(new PostEntryTask(level1.dimension(), origin.getX() + xDiff, origin.getY() + yDiff, origin.getZ() + zDiff, artifactRange, (byte) 0));
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
	
	private void removeOriginalBlocks(ServerLevel level0)
	{
		for(BlockPos pos : EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange))
		{
			if(!level0.isInWorldBounds(pos))
				continue;
			
			removeBlockEntity(level0, pos, creative);
			
			if(MinestuckConfig.SERVER.entryCrater.get() && !level0.getBlockState(pos).is(Blocks.BEDROCK))
			{
				level0.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
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
			Entity e = iterator.next();
			if(origin.distToCenterSqr(e.getX(), e.getY(), e.getZ()) <= artifactRange * artifactRange)
			{
				if(MinestuckConfig.SERVER.entryCrater.get() || e instanceof Player || !creative && e instanceof ItemEntity)
				{
					if(e instanceof Player && ServerEditHandler.getData((Player) e) != null)
						ServerEditHandler.reset(ServerEditHandler.getData((Player) e));
					else
					{
						Teleport.teleportEntity(e, level1, e.getX() + xDiff, e.getY() + yDiff, e.getZ() + zDiff);
					}
					//These entities should no longer be in the world, and this list is later used for entities that *should* remain.
					iterator.remove();
				} else    //Copy instead of teleport
				{
					Entity newEntity = e.getType().create(level1);
					if(newEntity != null)
					{
						newEntity.restoreFrom(e);
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
				if(blockEntity instanceof ComputerBlockEntity)    //Avoid duplicating computer data when a computer is kept in the overworld
					((ComputerBlockEntity) blockEntity).programData = new CompoundTag();
				else if(blockEntity instanceof TransportalizerBlockEntity)
					level.removeBlockEntity(pos);
			}
		}
	}
	
	private boolean canModifyEntryBlocks(Level level, Player player)
	{
		int x = (int) player.getX();
		if(player.getX() < 0) x--;
		int y = (int) player.getY();
		int z = (int) player.getZ();
		if(player.getZ() < 0) z--;
		for(BlockPos pos : EntryBlockIterator.getHorizontal(x, y, z, artifactRange))
		{
			if(!level.mayInteract(player, pos))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Gives the Y-value of the highest non-air block within artifact range of the coordinates provided in the given world.
	 */
	private int getTopHeight(ServerLevel level, int x, int y, int z)
	{
		LOGGER.debug("Getting maxY..");
		int maxY = y;
		for(BlockPos.MutableBlockPos pos : EntryBlockIterator.getHorizontal(x, y, z, artifactRange))
		{
			int height = EntryBlockIterator.yReach(pos, artifactRange, x, z);
			for(int blockY = Math.min(level.getMaxBuildHeight(), y + height); blockY > maxY; blockY--)
				if(!level.isEmptyBlock(pos.setY(blockY)))
				{
					maxY = blockY;
					break;
				}
		}
		
		LOGGER.debug("maxY: {}", maxY);
		return maxY;
	}
	
	public static void placeGates(ServerLevel level)
	{
		GateBlock.placeGate(level, new BlockPos(0, GateHandler.GATE_HEIGHT_1, 0), GateHandler.Type.GATE_1, 0);
		GateBlock.placeGate(level, new BlockPos(0, GateHandler.GATE_HEIGHT_2, 0), GateHandler.Type.GATE_2, 0);
	}
}