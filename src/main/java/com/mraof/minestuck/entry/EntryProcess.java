package com.mraof.minestuck.entry;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class EntryProcess
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Set<EntryBlockProcessing> blockProcessors = new HashSet<>();
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void addBlockProcessing(EntryBlockProcessing processing)
	{
		blockProcessors.add(processing);
	}
	
	private final int artifactRange = MinestuckConfig.SERVER.artifactRange.get();
	
	private int xDiff;
	private int yDiff;
	private int zDiff;
	private int topY;
	private BlockPos origin;
	private boolean creative;
	private HashSet<BlockMove> blockMoves;
	
	public void onArtifactActivated(ServerPlayer player)
	{
		try
		{
			if(player.level.dimension() != Level.NETHER)
			{
				if(!TitleSelectionHook.performEntryCheck(player))
					return;
				
				PlayerIdentifier identifier = IdentifierHandler.encode(player);
				Optional<SburbConnection> c = SkaianetHandler.get(player.level).getPrimaryConnection(identifier, true);
				
				//Only performs Entry if you have no connection, haven't Entered, or you're not in a Land and additional Entries are permitted.
				if(!c.isPresent() || !c.get().hasEntered() || !MinestuckConfig.SERVER.stopSecondEntry.get() && !MSDimensions.isLandDimension(player.server, player.level.dimension()))
				{
					if(!canModifyEntryBlocks(player.level, player))
					{
						player.sendMessage(new TextComponent("You are not allowed to enter here."), Util.NIL_UUID);    //TODO translation key
						return;
					}
					
					if(c.isPresent() && c.get().hasEntered())
					{
						ServerLevel landWorld = Objects.requireNonNull(player.getServer()).getLevel(c.get().getClientDimension());
						if(landWorld == null)
						{
							return;
						}
						
						//Teleports the player to their home in the Medium, without any bells or whistles.
						BlockPos pos = new BlockPos(0, 100, 0);
						Teleport.teleportEntity(player, landWorld, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, player.getYRot(), player.getXRot());
						
						return;
					}
					
					ResourceKey<Level> landDimension = SkaianetHandler.get(player.level).prepareEntry(identifier);
					if(landDimension == null)
					{
						player.sendMessage(new TextComponent("Something went wrong while creating your Land. More details in the server console."), Util.NIL_UUID);
					} else
					{
						ServerLevel oldLevel = (ServerLevel) player.level;
						ServerLevel newLevel = Objects.requireNonNull(player.getServer()).getLevel(landDimension);
						if(newLevel == null)
						{
							return;
						}
						
						if(this.prepareDestination(player.blockPosition(), player, oldLevel))
						{
							moveBlocks(oldLevel, newLevel);
							if(Teleport.teleportEntity(player, newLevel) != null)
							{
								finalizeDestination(player, oldLevel, newLevel);
								SkaianetHandler.get(player.level).onEntry(identifier);
							} else
							{
								player.sendMessage(new TextComponent("Entry failed. Unable to teleport you!"), Util.NIL_UUID);
							}
						}
					}
				}
			}
		} catch(Exception e)
		{
			LOGGER.error("Exception when {} tried to enter their land.", player.getName().getString(), e);
			player.sendMessage(new TextComponent("[Minestuck] Something went wrong during entry. " + (player.getServer().isDedicatedServer() ? "Check the console for the error message." : "Notify the server owner about this.")).withStyle(ChatFormatting.RED), Util.NIL_UUID);
		}
	}
	
	private boolean prepareDestination(BlockPos origin, ServerPlayer player, ServerLevel level)
	{
		
		blockMoves = new HashSet<>();
		
		LOGGER.info("Starting entry for player {}", player.getName().getString());
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();
		this.origin = origin;
		
		creative = player.gameMode.isCreative();
		
		topY = MinestuckConfig.SERVER.adaptEntryBlockHeight.get() ? getTopHeight(level, x, y, z) : y + artifactRange;
		yDiff = 119 - topY; //the top block will end up being at y = 120 once in the land
		xDiff = 0 - x;
		zDiff = 0 - z;
		
		LOGGER.debug("Loading block movements...");
		long time = System.currentTimeMillis();
		int bl = 0;
		boolean foundComputer = false;
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt((artifactRange + 0.5) * (artifactRange + 0.5) - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
			{
				LevelChunk c = level.getChunk(blockX >> 4, blockZ >> 4);
				
				int height = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2F));
				
				int blockY;
				for(blockY = Math.max(level.getMinBuildHeight(), y - height); blockY <= Math.min(topY, y + height); blockY++)
				{
					BlockPos pos = new BlockPos(blockX, blockY, blockZ);
					BlockPos pos1 = pos.offset(xDiff, yDiff, zDiff);
					BlockState block = level.getBlockState(pos);
					BlockEntity be = level.getBlockEntity(pos);
					
					Block gotBlock = block.getBlock();
					
					if(gotBlock == Blocks.BEDROCK || gotBlock == Blocks.NETHER_PORTAL)
					{
						blockMoves.add(new BlockMove(c, pos, pos1, Blocks.AIR.defaultBlockState(), true));
						continue;
					} else if(!creative && (gotBlock == Blocks.COMMAND_BLOCK || gotBlock == Blocks.CHAIN_COMMAND_BLOCK || gotBlock == Blocks.REPEATING_COMMAND_BLOCK))
					{
						player.displayClientMessage(new TextComponent("You are not allowed to move command blocks."), false);
						return false;
					} else if(be instanceof ComputerBlockEntity)        //If the block is a computer
					{
						if(!((ComputerBlockEntity) be).owner.equals(IdentifierHandler.encode(player)))    //You can't Enter with someone else's computer
						{
							player.displayClientMessage(new TextComponent("You are not allowed to move other players' computers."), false);
							return false;
						}
						
						foundComputer = true;    //You have a computer in range. That means you're taking your computer with you when you Enter. Smart move.
					}
					
					//Shouldn't this line check if the block is an edge block?
					blockMoves.add(new BlockMove(c, pos, pos1, block, false));
				}
				
				//What does this code accomplish?
				for(blockY += yDiff; blockY <= 255; blockY++)
				{
					//The first BlockPos isn't used for this operation.
					blockMoves.add(new BlockMove(c, BlockPos.ZERO, new BlockPos(blockX + xDiff, blockY, blockZ + zDiff), Blocks.AIR.defaultBlockState(), false));
				}
			}
		}
		
		if(!foundComputer && MinestuckConfig.SERVER.needComputer.get())
		{
			player.displayClientMessage(new TextComponent("There is no computer in range."), false);
			return false;
		}
		
		return true;
	}
	
	private void moveBlocks(ServerLevel level0, ServerLevel level1)
	{
		//This is split into two sections because moves that require block updates should happen after the ones that don't.
		//This helps to ensure that "anchored" blocks like torches still have the blocks they are anchored to when they update.
		//Some blocks like this (confirmed for torches, rails, and glowystone) will break themselves if they update without their anchor.
		LOGGER.debug("Moving blocks...");
		HashSet<BlockMove> blockMoves2 = new HashSet<>();
		for(BlockMove move : blockMoves)
		{
			if(move.update)
				move.copy(level1, level1.getChunk(move.dest));
			else
				blockMoves2.add(move);
		}
		for(BlockMove move : blockMoves2)
		{
			move.copy(level1, level1.getChunk(move.dest));
		}
		blockMoves2.clear();
	}
	
	private void finalizeDestination(Entity player, ServerLevel level0, ServerLevel level1)
	{
		if(player instanceof ServerPlayer)
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
			
			LOGGER.debug("Removing original blocks");
			for(BlockMove move : blockMoves)
			{
				removeBlockEntity(level0, move.source, creative);    //Block entities need special treatment
				
				if(MinestuckConfig.SERVER.entryCrater.get() && level0.getBlockState(move.source).getBlock() != Blocks.BEDROCK)
				{
					if(move.update)
						level0.setBlock(move.source, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
					else
						level0.setBlock(move.source, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
				}
			}
			blockMoves.clear();
			
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
				String name = level.getBlockState(pos).getBlock().getRegistryName().toString();
				try
				{
					level.removeBlockEntity(pos);
					level.removeBlock(pos, true);
				} catch(Exception e)
				{
					LOGGER.warn("Exception encountered when removing block entity " + name + " during entry:", e);
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
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				if(!level.mayInteract(player, new BlockPos(blockX, y, blockZ)))
					return false;
		}
		
		return true;
	}
	
	private static void copyBlockDirect(LevelAccessor levelAccessor, ChunkAccess cSrc, ChunkAccess cDst, int xSrc, int ySrc, int zSrc, int xDst, int yDst, int zDst)
	{
		BlockPos dest = new BlockPos(xDst, yDst, zDst);
		LevelChunkSection blockStorageSrc = getBlockStorage(cSrc, ySrc);
		LevelChunkSection blockStorageDst = getBlockStorage(cDst, yDst);
		int y = yDst;
		xSrc &= 15; ySrc &= 15; zSrc &= 15; xDst &= 15; yDst &= 15; zDst &= 15;
		
		boolean isEmpty = blockStorageDst.hasOnlyAir();
		BlockState state = blockStorageSrc.getBlockState(xSrc, ySrc, zSrc);
		blockStorageDst.setBlockState(xDst, yDst, zDst, state);
		if(isEmpty != blockStorageDst.hasOnlyAir())
			levelAccessor.getChunkSource().getLightEngine().updateSectionStatus(dest, blockStorageDst.hasOnlyAir());    //I assume this adds or removes a light storage section here depending on if it is needed (because a section with just air doesn't have to be regarded)
		
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING).update(xDst, y, zDst, state);
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(xDst, y, zDst, state);
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR).update(xDst, y, zDst, state);
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE).update(xDst, y, zDst, state);
	}
	
	private static LevelChunkSection getBlockStorage(ChunkAccess c, int y)
	{
		return c.getSection(c.getSectionIndex(y));
	}
	
	/**
	 * Gives the Y-value of the highest non-air block within artifact range of the coordinates provided in the given world.
	 */
	private int getTopHeight(ServerLevel level, int x, int y, int z)
	{
		LOGGER.debug("Getting maxY..");
		int maxY = y;
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
			{
				int height = (int) (Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2F)));
				for(int blockY = Math.min(255, y + height); blockY > maxY; blockY--)
					if(!level.isEmptyBlock(new BlockPos(blockX, blockY, blockZ)))
					{
						maxY = blockY;
						break;
					}
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
	
	private static class BlockMove
	{
		private final LevelChunk chunkFrom;
		private final BlockPos source;
		private final BlockPos dest;
		private final BlockState block;
		private final boolean update;
		
		BlockMove(LevelChunk c, BlockPos src, BlockPos dst, BlockState b, boolean u)
		{
			chunkFrom = c;
			source = src;
			dest = dst;
			block = b;
			update = u;
		}
		
		void copy(ServerLevel level, ChunkAccess chunkTo)
		{
			if(chunkTo.getBlockState(dest).getBlock() == Blocks.BEDROCK)
			{
				return;
			}
			
			if(update)
			{
				chunkTo.setBlockState(dest, block, true);
			} else if(block == Blocks.AIR.defaultBlockState())
			{
				level.setBlock(dest, block, 0);
			} else
			{
				copyBlockDirect(level, chunkFrom, chunkTo, source.getX(), source.getY(), source.getZ(), dest.getX(), dest.getY(), dest.getZ());
			}
			
			BlockEntity blockEntity = chunkFrom.getBlockEntity(source, LevelChunk.EntityCreationType.CHECK);
			BlockEntity newBE = null;
			if(blockEntity != null)
			{
				CompoundTag nbt = blockEntity.saveWithId();
				nbt.putInt("x", dest.getX());
				nbt.putInt("y", dest.getY());
				nbt.putInt("z", dest.getZ());
				newBE = BlockEntity.loadStatic(dest, block, nbt);
				if(newBE != null)
					level.setBlockEntity(newBE);
				else
					LOGGER.warn("Unable to create a new block entity {} when teleporting blocks to the medium!", blockEntity.getType().getRegistryName());
				
			}
			
			for(EntryBlockProcessing processing : blockProcessors)
			{
				processing.copyOver((ServerLevel) chunkFrom.getLevel(), source, level, dest, block, blockEntity, newBE);
			}
		}
	}
}