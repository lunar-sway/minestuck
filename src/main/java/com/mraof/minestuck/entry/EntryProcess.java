package com.mraof.minestuck.entry;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class EntryProcess
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private EntryProcess(ServerPlayer player)
	{
		this.origin = player.blockPosition();
		
		int topY = MinestuckConfig.SERVER.adaptEntryBlockHeight.get() ? getTopHeight(player.getLevel(), origin.getX(), origin.getY(), origin.getZ()) : origin.getY() + artifactRange;
		yDiff = 119 - topY; //the top block will end up being at y = 120 once in the land
		xDiff = -origin.getX();
		zDiff = -origin.getZ();
		
		creative = player.gameMode.isCreative();
	}
	
	private final int artifactRange = MinestuckConfig.SERVER.artifactRange.get();
	
	private final int xDiff, yDiff, zDiff;
	private final BlockPos origin;
	private final boolean creative;
	
	public static void onArtifactActivated(ServerPlayer player)
	{
		try
		{
			if(player.level.dimension() == Level.NETHER)
				return;
			if(!TitleSelectionHook.performEntryCheck(player))
				return;
			
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
			
			ServerLevel oldLevel = (ServerLevel) player.level;
			ServerLevel newLevel = Objects.requireNonNull(player.getServer()).getLevel(landDimension);
			if(newLevel == null)
			{
				return;
			}
			
			EntryProcess process = new EntryProcess(player);
			if(!process.canModifyEntryBlocks(player.level, player))
			{
				player.sendSystemMessage(Component.literal("You are not allowed to enter here."));    //TODO translation key
				return;
			}
			
			if(process.prepareDestination(player, oldLevel))
			{
				process.moveBlocks(oldLevel, newLevel);
				if(Teleport.teleportEntity(player, newLevel) != null)
				{
					process.finalizeDestination(player, oldLevel, newLevel);
					SkaianetHandler.get(player.level).onEntry(identifier);
				} else
				{
					player.sendSystemMessage(Component.literal("Entry failed. Unable to teleport you!"));
				}
			}
		} catch(Exception e)
		{
			LOGGER.error("Exception when {} tried to enter their land.", player.getName().getString(), e);
			player.sendSystemMessage(Component.literal("[Minestuck] Something went wrong during entry. " + (player.getServer().isDedicatedServer() ? "Check the console for the error message." : "Notify the server owner about this.")).withStyle(ChatFormatting.RED));
		}
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
	
	private boolean prepareDestination(ServerPlayer player, ServerLevel level)
	{
		
		LOGGER.info("Starting entry for player {}", player.getName().getString());
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();
		
		LOGGER.debug("Loading block movements...");
		boolean foundComputer = false;
		for(BlockPos pos : EntryBlockIterator.get(x, y, z, artifactRange))
		{
			if(!level.isInWorldBounds(pos))
				continue;
			
			BlockState block = level.getBlockState(pos);
			BlockEntity be = level.getBlockEntity(pos);
			
			if(!creative && (block.is(Blocks.COMMAND_BLOCK) || block.is(Blocks.CHAIN_COMMAND_BLOCK) || block.is(Blocks.REPEATING_COMMAND_BLOCK)))
			{
				player.displayClientMessage(Component.literal("You are not allowed to move command blocks."), false);
				return false;
			} else if(block.is(MSBlocks.SKAIANET_DENIER.get()))
			{
				player.displayClientMessage(Component.literal("Network error (413): Skaianet - failed to Enter user " + player.getDisplayName().getString() + ". Entry denial device used at global coordinates: " + pos.toShortString()), false);
				return false;
			} else if(be instanceof ComputerBlockEntity)        //If the block is a computer
			{
				if(!((ComputerBlockEntity) be).owner.equals(IdentifierHandler.encode(player)))    //You can't Enter with someone else's computer
				{
					player.displayClientMessage(Component.literal("You are not allowed to move other players' computers."), false);
					return false;
				}
				
				foundComputer = true;    //You have a computer in range. That means you're taking your computer with you when you Enter. Smart move.
			}
		}
		
		if(!foundComputer && MinestuckConfig.SERVER.needComputer.get())
		{
			player.displayClientMessage(Component.literal("There is no computer in range."), false);
			return false;
		}
		
		return true;
	}
	
	private void moveBlocks(ServerLevel level0, ServerLevel level1)
	{
		for(BlockPos pos : EntryBlockIterator.get(origin.getX(), origin.getY(), origin.getZ(), artifactRange))
		{
			if(!level0.isInWorldBounds(pos))
				continue;
			
			LevelChunk chunk = level0.getChunkAt(pos);
			BlockPos pos1 = pos.offset(xDiff, yDiff, zDiff);
			BlockState block = level0.getBlockState(pos);
			
			if(block.is(Blocks.BEDROCK) || block.is(Blocks.NETHER_PORTAL))
				block = Blocks.AIR.defaultBlockState();
			BlockCopier.copyBlock(level1, chunk, pos, block, level1.getChunk(pos1), pos1);
		}
	}
	
	private void finalizeDestination(ServerPlayer player, ServerLevel level0, ServerLevel level1)
	{
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();
		
		LOGGER.debug("Teleporting entities...");
		//The fudge here is to ensure that the AABB will always contain every entity meant to be moved.
		// As entities outside the radius will be excluded from transport anyway, this is fine.
		AABB entityTeleportBB = player.getBoundingBox().inflate(artifactRange + 0.5);
		List<Entity> list = level0.getEntities(player, entityTeleportBB);
		Iterator<Entity> iterator = list.iterator();
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
		
		player.teleportTo(player.getX() + xDiff, player.getY() + yDiff, player.getZ() + zDiff);
		
		//Remove entities that were generated in the process of teleporting entities and removing blocks.
		// This is usually caused by "anchored" blocks being updated between the removal of their anchor and their own removal.
		if(!creative || MinestuckConfig.SERVER.entryCrater.get())
		{
			LOGGER.debug("Removing entities left in the crater...");
			List<Entity> removalList = level0.getEntities(player, entityTeleportBB);
			
			//We check if the old list contains the entity, because that means it was there before the entities were teleported and blocks removed.
			// This can be caused by them being outside the Entry radius but still within the AABB,
			// Or by the player being in creative mode, or having entryCrater disabled, etc.
			// Ultimately, this means that the entity has already been taken care of as much as it needs to be, and it is inappropriate to remove the entity.
			removalList.removeAll(list);
			
			iterator = removalList.iterator();
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
		
		LOGGER.debug("Placing gates...");
		placeGates(level1);
		
		MSExtraData.get(level1).addPostEntryTask(new PostEntryTask(level1.dimension(), x + xDiff, y + yDiff, z + zDiff, artifactRange, (byte) 0));
		
		LOGGER.info("Entry finished");
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