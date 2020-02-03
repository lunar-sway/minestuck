package com.mraof.minestuck.item;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.CreateEntityType;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;

public abstract class CruxiteArtifactItem extends Item
{
	//TODO item classes should not be a container for non-final fields such as these. The functionality of this class need to be restructured!
	// Eventually we'd want cruxite artifacts of kinds other than items, so it's a good idea to move this to a more general class.
	private int xDiff;
	private int yDiff;
	private int zDiff;
	private int topY;
	private BlockPos origin;
	private boolean creative;
	private HashSet<BlockMove> blockMoves;

	public CruxiteArtifactItem(Properties properties)
	{
		super(properties);
	}
	
	public void onArtifactActivated(ServerPlayerEntity player)
	{
		try
		{
			if(player.world.getDimension().getType() != DimensionType.THE_NETHER)
			{
				if(!SburbHandler.shouldEnterNow(player))
					return;
				
				PlayerIdentifier identifier = IdentifierHandler.encode(player);
				SburbConnection c = SkaianetHandler.get(player.world).getMainConnection(identifier, true);
				
				//Only performs Entry if you have no connection, haven't Entered, or you're not in a Land and additional Entries are permitted.
				if(c == null || !c.hasEntered() || !MinestuckConfig.stopSecondEntry.get() && !MSDimensions.isLandDimension(player.world.getDimension().getType()))
				{
					if(!canModifyEntryBlocks(player.world, player))
					{
						player.sendMessage(new StringTextComponent("You are not allowed to enter here."));
						return;
					}
					
					if(c != null && c.hasEntered())
					{
						ServerWorld landWorld = Objects.requireNonNull(player.getServer()).getWorld(c.getClientDimension());
						if(landWorld == null)
						{
							return;
						}
						
						//Teleports the player to their home in the Medium, without any bells or whistles.
						BlockPos pos = landWorld.getDimension().getSpawnPoint();
						Teleport.teleportEntity(player, landWorld, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, player.rotationYaw, player.rotationPitch);
						
						return;
					}
					
					DimensionType landDimension = SkaianetHandler.get(player.world).prepareEntry(identifier);
					if(landDimension == null)
					{
						player.sendMessage(new StringTextComponent("Something went wrong while creating your Land. More details in the server console."));
					}
					else
					{
						ServerWorld oldWorld = (ServerWorld) player.world;
						ServerWorld newWorld = Objects.requireNonNull(player.getServer()).getWorld(landDimension);
						if(newWorld == null)
						{
							return;
						}
						
						if(this.prepareDestination(player.getPosition(), player, oldWorld))
						{
							moveBlocks(oldWorld, newWorld);
							if(Teleport.teleportEntity(player, newWorld) != null)
							{
								finalizeDestination(player, oldWorld, newWorld);
								SkaianetHandler.get(player.world).onEntry(identifier);
							} else
							{
								player.sendMessage(new StringTextComponent("Entry failed. Unable to teleport you!"));
							}
						}
					}
				}
			}
		} catch(Exception e)
		{
			Debug.logger.error("Exception when "+player.getName()+" tried to enter their land.", e);
			player.sendMessage(new StringTextComponent("[Minestuck] Something went wrong during entry. "+ (player.getServer().isDedicatedServer()?"Check the console for the error message.":"Notify the server owner about this.")).setStyle(new Style().setColor(TextFormatting.RED)));
		}
	}
	
	public boolean prepareDestination(BlockPos origin, ServerPlayerEntity player, ServerWorld worldserver0)
	{
		
		blockMoves = new HashSet<>();
		
		Debug.infof("Starting entry for player %s", player.getName());
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();
		this.origin = origin;
		
		creative = player.interactionManager.isCreative();
		SburbConnection conn = SkaianetHandler.get(worldserver0).getMainConnection(IdentifierHandler.encode(player), true);
		
		topY = MinestuckConfig.adaptEntryBlockHeight.get() ? getTopHeight(worldserver0, x, y, z) : y + artifactRange.get();
		yDiff = 127 - topY;
		xDiff = 0 - x;
		zDiff = 0 - z;
		
		Debug.debug("Loading block movements...");
		long time = System.currentTimeMillis();
		int bl = 0;
		boolean foundComputer = false;
		for(int blockX = x - artifactRange.get(); blockX <= x + artifactRange.get(); blockX++)
		{
			int zWidth = (int) Math.sqrt((artifactRange.get()+0.5) * (artifactRange.get()+0.5) - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
			{
				Chunk c = worldserver0.getChunk(blockX >> 4, blockZ >> 4);
				
				int height = (int) Math.sqrt(artifactRange.get() * artifactRange.get() - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2F));
				
				int blockY;
				for(blockY = Math.max(0, y - height); blockY <= Math.min(topY, y + height); blockY++)
				{
					BlockPos pos = new BlockPos(blockX, blockY, blockZ);
					BlockPos pos1 = pos.add(xDiff, yDiff, zDiff);
					BlockState block = worldserver0.getBlockState(pos);
					TileEntity te = worldserver0.getTileEntity(pos);
					
					Block gotBlock = block.getBlock();
					
					if(gotBlock == Blocks.BEDROCK || gotBlock == Blocks.NETHER_PORTAL)
					{
						blockMoves.add(new BlockMove(c, pos, pos1, Blocks.AIR.getDefaultState(), true));
						continue;
					}
					else if(!creative && (gotBlock == Blocks.COMMAND_BLOCK || gotBlock == Blocks.CHAIN_COMMAND_BLOCK || gotBlock == Blocks.REPEATING_COMMAND_BLOCK))
					{
						player.sendStatusMessage(new StringTextComponent("You are not allowed to move command blocks."), false);
						return false;
					} else if(te instanceof ComputerTileEntity)		//If the block is a computer
					{
						if(!((ComputerTileEntity)te).owner.equals(IdentifierHandler.encode((PlayerEntity) player)))	//You can't Enter with someone else's computer
						{
							player.sendStatusMessage(new StringTextComponent("You are not allowed to move other players' computers."), false);
							return false;
						}
						
						foundComputer = true;	//You have a computer in range. That means you're taking your computer with you when you Enter. Smart move.
					}
					
					//Shouldn't this line check if the block is an edge block?
					blockMoves.add(new BlockMove(c, pos, pos1, block, false));
				}
				
				//What does this code accomplish?
				for(blockY += yDiff; blockY <= 255; blockY++)
				{
					//The first BlockPos isn't used for this operation.
					blockMoves.add(new BlockMove(c, BlockPos.ZERO, new BlockPos(blockX + xDiff, blockY, blockZ + zDiff), Blocks.AIR.getDefaultState(), false));
				}
			}
		}
		
		if(foundComputer == false && MinestuckConfig.needComputer.get())
		{
			player.sendStatusMessage(new StringTextComponent("There is no computer in range."), false);
			return false;
		}
		
		return true;
	}
	
	public void moveBlocks(ServerWorld worldserver0, ServerWorld worldserver1)
	{
		//This is split into two sections because moves that require block updates should happen after the ones that don't.
		//This helps to ensure that "anchored" blocks like torches still have the blocks they are anchored to when they update.
		//Some blocks like this (confirmed for torches, rails, and glowystone) will break themselves if they update without their anchor.
		Debug.debug("Moving blocks...");
		HashSet<BlockMove> blockMoves2 = new HashSet<>();
		for(BlockMove move : blockMoves)
		{
			if(move.update)
				move.copy(worldserver1, worldserver1.getChunk(move.dest));
			else
				blockMoves2.add(move);
		}
		for(BlockMove move : blockMoves2)
		{
			move.copy(worldserver1, worldserver1.getChunk(move.dest));
		}
		blockMoves2.clear();
	}
	
	public void finalizeDestination(Entity player, ServerWorld worldserver0, ServerWorld worldserver1)
	{
		if(player instanceof ServerPlayerEntity)
		{
			int x = origin.getX();
			int y = origin.getY();
			int z = origin.getZ();
			
			Debug.debug("Teleporting entities...");
			//The fudge here is to ensure that the AABB will always contain every entity meant to be moved.
			// As entities outside the radius will be excluded from transport anyway, this is fine.
			AxisAlignedBB entityTeleportBB = player.getBoundingBox().grow(artifactRange.get() + 0.5);
			List<Entity> list = worldserver0.getEntitiesWithinAABBExcludingEntity(player, entityTeleportBB);
			Iterator<Entity> iterator = list.iterator();
			while (iterator.hasNext())
			{
				Entity e = iterator.next();
				if(origin.distanceSq(e.posX, e.posY, e.posZ, true) <= artifactRange.get()*artifactRange.get())
				{
					if(MinestuckConfig.entryCrater.get() || e instanceof PlayerEntity || !creative && e instanceof ItemEntity)
					{
						if(e instanceof PlayerEntity && ServerEditHandler.getData((PlayerEntity) e) != null)
							ServerEditHandler.reset(ServerEditHandler.getData((PlayerEntity) e));
						else
						{
							Teleport.teleportEntity(e, worldserver1, e.posX + xDiff, e.posY + yDiff, e.posZ + zDiff);
						}
						//These entities should no longer be in the world, and this list is later used for entities that *should* remain.
						iterator.remove();
					}
					else	//Copy instead of teleport
					{
						Entity newEntity = e.getType().create(worldserver1);
						if (newEntity != null)
						{
							CompoundNBT nbttagcompound = new CompoundNBT();
							e.writeWithoutTypeId(nbttagcompound);
							nbttagcompound.remove("Dimension");
							newEntity.read(nbttagcompound);
							newEntity.dimension = worldserver1.getDimension().getType();
							newEntity.setPosition(newEntity.posX + xDiff, newEntity.posY + yDiff, newEntity.posZ + zDiff);
							worldserver1.addEntity(newEntity);
						}
					}
				}
			}
			
			Debug.debug("Removing original blocks");
			for(BlockMove move : blockMoves)
			{
				removeTileEntity(worldserver0, move.source, creative);	//Tile entities need special treatment
				
				if(MinestuckConfig.entryCrater.get() && worldserver0.getBlockState(move.source).getBlock() != Blocks.BEDROCK)
				{
					if(move.update)
						worldserver0.setBlockState(move.source, Blocks.AIR.getDefaultState(), 3);
					else
						worldserver0.setBlockState(move.source, Blocks.AIR.getDefaultState(), 2);
				}
			}
			blockMoves.clear();
			
			player.setPositionAndUpdate(player.posX + xDiff, player.posY + yDiff, player.posZ + zDiff);
			
			SkaianetHandler.get(worldserver0).clearMovingList();
			
			//Remove entities that were generated in the process of teleporting entities and removing blocks.
			// This is usually caused by "anchored" blocks being updated between the removal of their anchor and their own removal.
			if(!creative || MinestuckConfig.entryCrater.get())
			{
				Debug.debug("Removing entities left in the crater...");
				List<Entity> removalList = worldserver0.getEntitiesWithinAABBExcludingEntity(player, entityTeleportBB);
				
				//We check if the old list contains the entity, because that means it was there before the entities were teleported and blocks removed.
				// This can be caused by them being outside the Entry radius but still within the AABB,
				// Or by the player being in creative mode, or having entryCrater disabled, etc.
				// Ultimately, this means that the entity has already been taken care of as much as it needs to be, and it is inappropriate to remove the entity.
				removalList.removeAll(list);
				
				iterator = removalList.iterator();
				if(MinestuckConfig.entryCrater.get())
				{
					while (iterator.hasNext())
					{
						iterator.next().remove();
					}
				} else
				{
					while (iterator.hasNext())
					{
						Entity e = iterator.next();
						if(e instanceof ItemEntity)
							e.remove();
					}
				}
			}
			
			Debug.debug("Placing gates...");
			
			placeGate(GateHandler.Type.GATE_1, new BlockPos(x + xDiff, GateHandler.gateHeight1, z + zDiff), worldserver1);
			placeGate(GateHandler.Type.GATE_2, new BlockPos(x + xDiff, GateHandler.gateHeight2, z + zDiff), worldserver1);
			
			MSExtraData.get(worldserver1).addPostEntryTask(new PostEntryTask(worldserver1.getDimension().getType(), x + xDiff, y + yDiff, z + zDiff, artifactRange.get(), (byte) 0));
			
			MSDimensions.getLandInfo(worldserver1).setSpawn(MathHelper.floor(player.posY));
			
			Debug.info("Entry finished");
		}
	}

	/**
	 * Determines if it is appropriate to remove the tile entity in the specified location,
	 * and removes both the tile entity and its corresponding block if so.
	 * This method is expressly designed to prevent drops from appearing when the block is removed.
	 * It will also deliberately trigger block updates based on the removal of the tile entity's block.
	 * @param worldserver0 The world where the tile entity is located
	 * @param pos The position at which the tile entity is located
	 * @param creative Whether or not creative-mode rules should be employed
	 */
	private static void removeTileEntity(ServerWorld worldserver0, BlockPos pos, boolean creative)
	{
		TileEntity tileEntity = worldserver0.getTileEntity(pos);
		if(tileEntity != null)
		{
			if(MinestuckConfig.entryCrater.get() || !creative)
			{
				String name = worldserver0.getBlockState(pos).getBlock().getRegistryName().toString();
				try {
					worldserver0.removeTileEntity(pos);
					worldserver0.removeBlock(pos, true);
				} catch (NullPointerException e) {
					Logger.getGlobal().warning("Null Pointer Exception encountered when removing " + name + ". "
							+ "Notify the mod author that the block should make a null check on its tile entity when broken.");
				} catch (Exception e) {
					Logger.getGlobal().warning("Unknown Exception encountered when removing " + name + ". "
							+ "Notify a Minestuck dev of this error.");
				}
			} else
			{
				if(tileEntity instanceof ComputerTileEntity)	//Avoid duplicating computer data when a computer is kept in the overworld
					((ComputerTileEntity) tileEntity).programData = new CompoundNBT();
				else if(tileEntity instanceof TransportalizerTileEntity)
					worldserver0.removeTileEntity(pos);
			}
		}
	}
	
	private static boolean canModifyEntryBlocks(World world, PlayerEntity player)
	{
		int x = (int) player.posX;
		if(player.posX < 0) x--;
		int y = (int) player.posY;
		int z = (int) player.posZ;
		if(player.posZ < 0) z--;
		for(int blockX = x - artifactRange.get(); blockX <= x + artifactRange.get(); blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange.get() * artifactRange.get() - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				if(!world.isBlockModifiable(player, new BlockPos(blockX, y, blockZ)))
					return false;
		}
		
		return true;
	}
	
	private static void copyBlockDirect(IWorld world, IChunk cSrc, IChunk cDst, int xSrc, int ySrc, int zSrc, int xDst, int yDst, int zDst)
	{
		BlockPos dest = new BlockPos(xDst, yDst, zDst);
		ChunkSection blockStorageSrc = getBlockStorage(cSrc, ySrc >> 4);
		ChunkSection blockStorageDst = getBlockStorage(cDst, yDst >> 4);
		xSrc &= 15; ySrc &= 15; zSrc &= 15; xDst &= 15; yDst &= 15; zDst &= 15;
		
		boolean isEmpty = blockStorageDst.isEmpty();
		blockStorageDst.setBlockState(xDst, yDst, zDst, blockStorageSrc.getBlockState(xSrc, ySrc, zSrc));
		if(isEmpty != blockStorageDst.isEmpty())
			world.getChunkProvider().getLightManager().func_215567_a(dest, blockStorageDst.isEmpty());	//I assume this adds or removes a light storage section here depending on if it is needed (because a section with just air doesn't have to be regarded)
	}
	
	private static ChunkSection getBlockStorage(IChunk c, int y)
	{
		ChunkSection section = c.getSections()[y];
		if(section == Chunk.EMPTY_SECTION)
			section = c.getSections()[y] = new ChunkSection(y << 4);
		return section;
	}
	
	/**
	 * Gives the Y-value of the highest non-air block within artifact range of the coordinates provided in the given world.
	 */
	private static int getTopHeight(ServerWorld world, int x, int y, int z)
	{
		Debug.debug("Getting maxY..");
		int maxY = y;
		for(int blockX = x - artifactRange.get(); blockX <= x + artifactRange.get(); blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange.get() * artifactRange.get() - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
			{
				int height = (int) (Math.sqrt(artifactRange.get() * artifactRange.get() - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2)));
				for(int blockY = Math.min(255, y + height); blockY > maxY; blockY--)
					if(!world.isAirBlock(new BlockPos(blockX, blockY, blockZ)))
					{
						maxY = blockY;
						break;
					}
			}
		}
		
		Debug.debug("maxY: "+ maxY);
		return maxY;
	}
	
	private static void placeGate(GateHandler.Type gateType, BlockPos pos, ServerWorld world)
	{
		for(int i = 0; i < 9; i++)
			if(i == 4)
			{
				world.setBlockState(pos, MSBlocks.GATE.getDefaultState().cycle(GateBlock.MAIN), 0);
				GateTileEntity tileEntity = (GateTileEntity) world.getTileEntity(pos);
				tileEntity.gateType = gateType;
			}
			else world.setBlockState(pos.add((i % 3) - 1, 0, i/3 - 1), MSBlocks.GATE.getDefaultState(), 0);
	}
	
	private class BlockMove
	{
		protected Chunk chunkFrom;
		BlockPos source;
		BlockPos dest;
		private BlockState block = null;
		private boolean update;
		
		BlockMove(Chunk c, BlockPos src, BlockPos dst, BlockState b, boolean u)
		{
			chunkFrom = c;
			source = src;
			dest = dst;
			block = b;
			update = u;
		}
		
		void copy(ServerWorld world, IChunk chunkTo)
		{
			if(chunkTo.getBlockState(dest).getBlock() == Blocks.BEDROCK)
			{
				return;
			}
			
			if(update)
			{
				chunkTo.setBlockState(dest, block, true);
			} else if(block == Blocks.AIR.getDefaultState())
			{
				world.setBlockState(dest, block, 0);
			} else
			{
				CruxiteArtifactItem.copyBlockDirect(world, chunkFrom, chunkTo, source.getX(), source.getY(), source.getZ(), dest.getX(), dest.getY(), dest.getZ());
			}
			
			TileEntity tileEntity = chunkFrom.getTileEntity(source, CreateEntityType.CHECK);
			if(tileEntity != null)
			{
				CompoundNBT nbt = new CompoundNBT();
				tileEntity.write(nbt);
				nbt.putInt("x", dest.getX());
				nbt.putInt("y", dest.getY());
				nbt.putInt("z", dest.getZ());
				TileEntity te1 = TileEntity.create(nbt);
				if(te1 != null)
					chunkTo.addTileEntity(dest, te1);
				else Debug.warnf("Unable to create a new tile entity %s when teleporting blocks to the medium!", tileEntity.getType().getRegistryName());
				if(tileEntity instanceof ComputerTileEntity)
					SkaianetHandler.get(world).movingComputer((ComputerTileEntity) tileEntity, (ComputerTileEntity) te1);
			}
		}
	}
}
