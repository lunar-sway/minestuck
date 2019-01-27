package com.mraof.minestuck.item;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.PostEntryTask;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public abstract class ItemCruxiteArtifact extends Item implements Teleport.ITeleporter
{
	private int xDiff;
	private int yDiff;
	private int zDiff;
	private int topY;
	private BlockPos origin;
	private boolean creative;
	private HashSet<BlockMove> blockMoves;
	
	public ItemCruxiteArtifact() 
	{
		this.setCreativeTab(TabMinestuck.instance);
		setUnlocalizedName("cruxiteArtifact");
		this.maxStackSize = 1;
		setHasSubtypes(true);
	}
	
	public void onArtifactActivated(EntityPlayer player)
	{
		try
		{
			if(!player.world.isRemote && player.world.provider.getDimension() != -1)
			{
				if(!SburbHandler.shouldEnterNow(player))
					return;
				
				SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode(player), true);
				
				//Only preforms Entry if you have no connection, haven't Entered, or you're not in a Land and additional Entries are permitted.
				if(c == null || !c.enteredGame() || !MinestuckConfig.stopSecondEntry && !MinestuckDimensionHandler.isLandDimension(player.world.provider.getDimension()))
				{
					if(!canModifyEntryBlocks(player.world, player))
					{
						player.sendMessage(new TextComponentString("You are not allowed to enter here."));
						return;
					}
					
					if(c != null && c.enteredGame())
					{
						World newWorld = player.getServer().getWorld(c.getClientDimension());
						if(newWorld == null)
						{
							return;
						}
						
						//Teleports the player to their home in the Medium, without any bells or whistles.
						BlockPos pos = newWorld.provider.getRandomizedSpawnPoint();
						Teleport.teleportEntity(player, c.getClientDimension(), null, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
						
						return;
					}
					
					//Teleportation code is now called from enterMedium(), which is called from createLand.
					//createLand will return -1 if Entry fails for any reason, including the teleporter being null or returning false in prepareDestination().
					//Whatever the problem is, relevant information should be printed to the console.
					if(LandAspectRegistry.createLand(player, this) == -1)
					{
						player.sendMessage(new TextComponentString("Something went wrong creating your Land. More details in the server console."));
					}
					else
					{
						c = SburbHandler.getConnectionForDimension(player.dimension);
						if(c != null && c.getClientIdentifier().equals(IdentifierHandler.encode(player)))
						{
							MinestuckCriteriaTriggers.CRUXITE_ARTIFACT.trigger((EntityPlayerMP) player);
							MinestuckPlayerTracker.sendLandEntryMessage(player);
						} else
						{
							player.sendMessage(new TextComponentString("Entry failed!"));
						}
					}
					
					return;
				}
			}
		} catch(Exception e)
		{
			Debug.logger.error("Exception when "+player.getName()+" tried to enter their land.", e);
			player.sendMessage(new TextComponentString("[Minestuck] Something went wrong during entry. "+ (Minestuck.isServerRunning?"Check the console for the error message.":"Notify the server owner about this.")).setStyle(new Style().setColor(TextFormatting.RED)));
		}
	}
	
	@Override
	public boolean prepareDestination(BlockPos origin, Entity player, WorldServer worldserver0)
	{
		if(!(player instanceof EntityPlayerMP))
		{
			return false;
		}
		
		blockMoves = new HashSet<BlockMove>();
		
		Debug.infof("Starting entry for player %s", player.getName());
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();
		this.origin = origin;
		
		creative = ((EntityPlayerMP) player).interactionManager.isCreative();
		SburbConnection conn = SkaianetHandler.getMainConnection(IdentifierHandler.encode((EntityPlayer) player), true);
		
		topY = MinestuckConfig.adaptEntryBlockHeight ? getTopHeight(worldserver0, x, y, z) : y + artifactRange;
		yDiff = 127 - topY;
		xDiff = 0 - x;
		zDiff = 0 - z;
		
		Debug.debug("Loading block movements...");
		long time = System.currentTimeMillis();
		int bl = 0;
		boolean foundComputer = false;
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt((artifactRange+0.5) * (artifactRange+0.5) - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
			{
				Chunk c = worldserver0.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
				
				int height = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2));
				
				int blockY;
				for(blockY = Math.max(0, y - height); blockY <= Math.min(topY, y + height); blockY++)
				{
					BlockPos pos = new BlockPos(blockX, blockY, blockZ);
					BlockPos pos1 = pos.add(xDiff, yDiff, zDiff);
					IBlockState block = worldserver0.getBlockState(pos);
					TileEntity te = worldserver0.getTileEntity(pos);
					
					Block gotBlock = block.getBlock();
					
					if(gotBlock == Blocks.BEDROCK || gotBlock == Blocks.PORTAL)
					{
						blockMoves.add(new BlockMove(c, pos, pos1, Blocks.AIR.getDefaultState(), true));
						continue;
					}
					else if(!creative && (gotBlock == Blocks.COMMAND_BLOCK || gotBlock == Blocks.CHAIN_COMMAND_BLOCK || gotBlock == Blocks.REPEATING_COMMAND_BLOCK))
					{
						((EntityPlayerMP) player).sendStatusMessage(new TextComponentString("You are not allowed to move command blocks."), false);
						return false;
					} else if(te instanceof TileEntityComputer)		//If the block is a computer
					{
						if(!((TileEntityComputer)te).owner.equals(IdentifierHandler.encode((EntityPlayer) player)))	//You can't Enter with someone else's computer
						{
							((EntityPlayerMP) player).sendStatusMessage(new TextComponentString("You are not allowed to move other players' computers."), false);
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
					blockMoves.add(new BlockMove(c, BlockPos.ORIGIN, new BlockPos(blockX + xDiff, blockY, blockZ + zDiff), Blocks.AIR.getDefaultState(), false));
				}
			}
		}
		
		if(foundComputer == false && MinestuckConfig.needComputer)
		{
			((EntityPlayerMP) player).sendStatusMessage(new TextComponentString("There is no computer in range."), false);
			return false;
		}
		
		return true;
	}
	
	@Override
	public void finalizeDestination(Entity player, WorldServer worldserver0, WorldServer worldserver1)
	{
		if(player instanceof EntityPlayerMP)
		{
			int x = origin.getX();
			int y = origin.getY();
			int z = origin.getZ();
			
			Debug.debug("Loading spawn chunks...");
			for(int chunkX = ((x + xDiff - artifactRange) >> 4) - 1; chunkX <= ((x + xDiff + artifactRange) >> 4) + 2; chunkX++)		//Prevent anything generating on the piece that we move
				for(int chunkZ = ((z + zDiff - artifactRange) >> 4) - 1; chunkZ <= ((z + zDiff + artifactRange) >> 4) + 2; chunkZ++)	//from the overworld.
					worldserver1.getChunkProvider().provideChunk(chunkX, chunkZ);
			
			//Set again, but with a more precise value now that the y-coordinate is properly decided.
			MinestuckDimensionHandler.setSpawn(worldserver1.provider.getDimension(), new BlockPos(x + xDiff, y + yDiff, z + zDiff));
			
			//This is split into two sections because moves that require block updates should happen after the ones that don't.
			//This helps to ensure that "anchored" blocks like torches still have the blocks they are anchored to when they update.
			//Some blocks like this (confirmed for torches, rails, and glowystone) will break themselves if they update without their anchor.
			Debug.debug("Moving blocks...");
			HashSet<BlockMove> blockMoves2 = new HashSet<BlockMove>();
			for(BlockMove move : blockMoves)
			{
				if(!move.update)
					move.copy(worldserver1.getChunkFromBlockCoords(move.dest));
				else
					blockMoves2.add(move);
			}
			for(BlockMove move : blockMoves2)
			{
				move.copy(worldserver1.getChunkFromBlockCoords(move.dest));
			}
			blockMoves2.clear();
			
			Debug.debug("Teleporting entities...");
			//The fudge here is to ensure that the AABB will always contain every entity meant to be moved.
			// As entities outside the radius will be excluded from transport anyway, this is fine.
			AxisAlignedBB entityTeleportBB = player.getEntityBoundingBox().grow(artifactRange + 0.5);
			List<Entity> list = worldserver0.getEntitiesWithinAABBExcludingEntity(player, entityTeleportBB);
			Iterator<Entity> iterator = list.iterator();
			while (iterator.hasNext())
			{
				Entity e = iterator.next();
				if(origin.distanceSqToCenter(e.posX, e.posY, e.posZ) <= artifactRange*artifactRange)
				{
					if(MinestuckConfig.entryCrater || e instanceof EntityPlayer || !creative && e instanceof EntityItem)
					{
						if(e instanceof EntityPlayer && ServerEditHandler.getData((EntityPlayer) e) != null)
							ServerEditHandler.reset(ServerEditHandler.getData((EntityPlayer) e));
						else
						{
							Teleport.teleportEntity(e, worldserver1.provider.getDimension(), null, e.posX + xDiff, e.posY + yDiff, e.posZ + zDiff);
						}
						//These entities should no longer be in the world, and this list is later used for entities that *should* remain.
						iterator.remove();
					}
					else	//Copy instead of teleport
					{
						Entity newEntity = EntityList.newEntity(e.getClass(), worldserver1);
						if (newEntity != null)
						{
							NBTTagCompound nbttagcompound = new NBTTagCompound();
							e.writeToNBT(nbttagcompound);
							nbttagcompound.removeTag("Dimension");
							newEntity.readFromNBT(nbttagcompound);
							newEntity.dimension = worldserver1.provider.getDimension();
							newEntity.setPosition(newEntity.posX + xDiff, newEntity.posY + yDiff, newEntity.posZ + zDiff);
							worldserver1.spawnEntity(newEntity);
						}
					}
				}
			}
			
			for(BlockMove move : blockMoves)
			{
				removeTileEntity(worldserver0, move.source, creative);	//Tile entities need special treatment
				
				if(MinestuckConfig.entryCrater && worldserver0.getBlockState(move.source).getBlock() != Blocks.BEDROCK)
				{
					if(move.update)
						worldserver0.setBlockState(move.source, Blocks.AIR.getDefaultState(), 3);
					else
						worldserver0.setBlockState(move.source, Blocks.AIR.getDefaultState(), 2);
				}
			}
			blockMoves.clear();
			
			player.setPositionAndUpdate(player.posX + xDiff, player.posY + yDiff, player.posZ + zDiff);
			
			SkaianetHandler.clearMovingList();
			
			//Remove entities that were generated in the process of teleporting entities and removing blocks.
			// This is usually caused by "anchored" blocks being updated between the removal of their anchor and their own removal.
			if(!creative || MinestuckConfig.entryCrater)
			{
				Debug.debug("Removing entities left in the crater...");
				List<Entity> removalList = worldserver0.getEntitiesWithinAABBExcludingEntity(player, entityTeleportBB);
				
				//We check if the old list contains the entity, because that means it was there before the entities were teleported and blocks removed.
				// This can be caused by them being outside the Entry radius but still within the AABB,
				// Or by the player being in creative mode, or having entryCrater disabled, etc.
				// Ultimately, this means that the entity has already been taken care of as much as it needs to be, and it is inappropriate to remove the entity.
				removalList.removeAll(list);
				
				iterator = removalList.iterator();
				if(MinestuckConfig.entryCrater)
				{
					while (iterator.hasNext())
					{
						iterator.next().setDead();
					}
				} else
				{
					while (iterator.hasNext())
					{
						Entity e = iterator.next();
						if(e instanceof EntityItem)
							e.setDead();
					}
				}
			}
			
			Debug.debug("Placing gates...");
			
			GateHandler.findGatePlacement(worldserver1);
			placeGate(1, new BlockPos(x + xDiff, GateHandler.gateHeight1, z + zDiff), worldserver1);
			placeGate(2, new BlockPos(x + xDiff, GateHandler.gateHeight2, z + zDiff), worldserver1);
			
			ServerEventHandler.tickTasks.add(new PostEntryTask(worldserver1.provider.getDimension(), x + xDiff, y + yDiff, z + zDiff, artifactRange, (byte) 0));
			
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
	private static void removeTileEntity(WorldServer worldserver0, BlockPos pos, boolean creative)
	{
		TileEntity tileEntity = worldserver0.getTileEntity(pos);
		if(tileEntity != null)
		{
			if(MinestuckConfig.entryCrater || !creative)
			{
				String name = worldserver0.getBlockState(pos).getBlock().getRegistryName().toString();
				try {
					worldserver0.removeTileEntity(pos);
					worldserver0.setBlockToAir(pos);
				} catch (NullPointerException e) {
					Logger.getGlobal().warning("Null Pointer Exception encountered when removing " + name + ". "
							+ "Notify the mod author that the block should make a null check on its tile entity when broken.");
				} catch (Exception e) {
					Logger.getGlobal().warning("Unknown Exception encountered when removing " + name + ". "
							+ "Notify a Minestuck dev of this error.");
				}
			} else
			{
				if(tileEntity instanceof TileEntityComputer)	//Avoid duplicating computer data when a computer is kept in the overworld
					((TileEntityComputer) tileEntity).programData = new NBTTagCompound();
				else if(tileEntity instanceof TileEntityTransportalizer)
					worldserver0.removeTileEntity(pos);
			}
		}
	}
	
	private static boolean canModifyEntryBlocks(World world, EntityPlayer player)
	{
		int x = (int) player.posX;
		if(player.posX < 0) x--;
		int y = (int) player.posY;
		int z = (int) player.posZ;
		if(player.posZ < 0) z--;
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				if(!world.isBlockModifiable(player, new BlockPos(blockX, y, blockZ)))
					return false;
		}
		
		return true;
	}
	
	private static void copyBlockDirect(Chunk cSrc, Chunk cDst, int xSrc, int ySrc, int zSrc, int xDst, int yDst, int zDst)
	{
		ExtendedBlockStorage blockStorageSrc = getBlockStorage(cSrc, ySrc >> 4);
		ExtendedBlockStorage blockStorageDst = getBlockStorage(cDst, yDst >> 4);
		xSrc &= 15; ySrc &= 15; zSrc &= 15; xDst &= 15; yDst &= 15; zDst &= 15;
		
		blockStorageDst.set(xDst, yDst, zDst, blockStorageSrc.get(xSrc, ySrc, zSrc));
		blockStorageDst.setBlockLight(xDst, yDst, zDst, blockStorageSrc.getBlockLight(xSrc, ySrc, zSrc));
		if(blockStorageSrc.getSkyLight() != null)
			blockStorageDst.setSkyLight(xDst, yDst, zDst, blockStorageSrc.getSkyLight(xSrc, ySrc, zSrc));
	}
	
	private static ExtendedBlockStorage getBlockStorage(Chunk c, int y)
	{
		ExtendedBlockStorage blockStorage = c.getBlockStorageArray()[y];
		if(blockStorage == null)
			blockStorage = c.getBlockStorageArray()[y] = new ExtendedBlockStorage(y << 4, c.getWorld().provider.hasSkyLight());
		return blockStorage;
	}
	
	/**
	 * Gives the Y-value of the highest non-air block within artifact range of the coordinates provided in the given world.
	 */
	private static int getTopHeight(WorldServer world, int x, int y, int z)
	{
		Debug.debug("Getting maxY..");
		int maxY = y;
		for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
		{
			int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
			for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
			{
				int height = (int) (Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2)));
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
	
	private static void placeGate(int gateCount, BlockPos pos, WorldServer world)
	{
		for(int i = 0; i < 9; i++)
			if(i == 4)
			{
				world.setBlockState(pos, MinestuckBlocks.gate.getDefaultState().cycleProperty(BlockGate.isMainComponent), 0);
				TileEntityGate tileEntity = (TileEntityGate) world.getTileEntity(pos);
				tileEntity.gateCount = gateCount;
			}
			else world.setBlockState(pos.add((i % 3) - 1, 0, i/3 - 1), MinestuckBlocks.gate.getDefaultState(), 0);
	}
	
	private class BlockMove
	{
		protected Chunk chunkFrom;
		BlockPos source;
		BlockPos dest;
		private IBlockState block = null;
		private boolean update;
		
		BlockMove(Chunk c, BlockPos src, BlockPos dst, IBlockState b, boolean u)
		{
			chunkFrom = c;
			source = src;
			dest = dst;
			block = b;
			update = u;
		}
		
		void copy(Chunk chunkTo)
		{
			if(chunkTo.getBlockState(dest).getBlock() == Blocks.BEDROCK)
			{
				return;
			}
			
			if(update)
			{
				chunkTo.setBlockState(dest, block);
			} else if(block == Blocks.AIR.getDefaultState())
			{
				chunkTo.getWorld().setBlockState(dest, block, 0);
			} else
			{
				ItemCruxiteArtifact.copyBlockDirect(chunkFrom, chunkTo, source.getX(), source.getY(), source.getZ(), dest.getX(), dest.getY(), dest.getZ());
			}
			
			TileEntity tileEntity = chunkFrom.getTileEntity(source, EnumCreateEntityType.CHECK);
			if(tileEntity != null)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				tileEntity.writeToNBT(nbt);
				nbt.setInteger("y", dest.getY());
				TileEntity te1 = TileEntity.create(chunkTo.getWorld(), nbt);
				chunkTo.addTileEntity(dest, te1);
				if(tileEntity instanceof TileEntityComputer)
					SkaianetHandler.movingComputer((TileEntityComputer) tileEntity, (TileEntityComputer) te1);
			}
		}
	}
}