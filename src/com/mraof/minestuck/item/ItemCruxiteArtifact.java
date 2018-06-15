package com.mraof.minestuck.item;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockComputerOn;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class ItemCruxiteArtifact extends Item implements Teleport.ITeleporter
{
	private int x;
	private int y;
	private int yDiff;
	private int topY;
	private int z;
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
						c = SburbHandler.getConnectionForDimension(player.dimension);	//This is viable as we know the player was not in any Land dimension prior to the Entry attempt.
						if(c != null)
						{
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
		if(player instanceof EntityPlayerMP)
		{
			blockMoves = new HashSet<BlockMove>();
			
			//((EntityPlayerMP) entity).addStat(MinestuckAchievementHandler.enterMedium);
			Debug.infof("Starting entry for player %s", player.getName());
			x = origin.getX();
			y = origin.getY();
			z = origin.getZ();
			
			this.origin = origin;
			
			creative = ((EntityPlayerMP) player).interactionManager.isCreative();
			
			topY = MinestuckConfig.adaptEntryBlockHeight ? getTopHeight(worldserver0, x, y, z) : y + artifactRange;
			yDiff = 127 - topY;
			
			Debug.debug("Placing blocks...");
			long time = System.currentTimeMillis();
			int bl = 0;
			boolean foundComputer = false;
			for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
			{
				int zWidth = (int) Math.sqrt((artifactRange+0.5) * (artifactRange+0.5) - (blockX - x) * (blockX - x));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					Chunk chunk2 = worldserver0.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
					int height = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2));
					for(int blockY = Math.max(0, y - height); blockY <= Math.min(topY, y + height); blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						BlockPos pos1 = pos.up(yDiff);
						IBlockState block = worldserver0.getBlockState(pos);
						TileEntity te = worldserver0.getTileEntity(pos);
						long t = System.currentTimeMillis();
						
						Block gotBlock = block.getBlock();
						
						if(gotBlock == Blocks.BEDROCK || gotBlock == Blocks.PORTAL)
						{
							blockMoves.add(new BlockMove(chunk2, blockX, blockY + yDiff, blockY, blockZ, Blocks.AIR.getDefaultState()));
						}
						else if(!creative && (gotBlock == Blocks.COMMAND_BLOCK || gotBlock == Blocks.CHAIN_COMMAND_BLOCK || gotBlock == Blocks.REPEATING_COMMAND_BLOCK))
						{
							((EntityPlayerMP) player).sendStatusMessage(new TextComponentString("You are not allowed to move command blocks."), false);
							return false;
						} else if(gotBlock == MinestuckBlocks.blockComputerOn)
						{
							if(((TileEntityComputer)te).owner.getPlayer() != player)
							{
								((EntityPlayerMP) player).sendStatusMessage(new TextComponentString("You are not allowed to move other players' computers."), false);
								return false;
							} else
							{
								foundComputer = true;
							}
						} else
						{
							blockMoves.add(new BlockMove(chunk2, blockX, blockY + yDiff, blockY, blockZ));
						}
						bl += System.currentTimeMillis() - t;
					}
					for(int blockY = Math.min(topY, y + height) + yDiff + 1; blockY < 256; blockY++)
					{
						blockMoves.add(new BlockMove(chunk2, blockX, blockY + yDiff, blockY, blockZ, Blocks.AIR.getDefaultState()));
					}
				}
			}
			
			if(foundComputer == false && MinestuckConfig.needComputer)
			{
				((EntityPlayerMP) player).sendStatusMessage(new TextComponentString("You need to bring your computer if you want to play Sburb!"), false);
				return false;
			}
			
			int total = (int) (System.currentTimeMillis() - time);
			Debug.debugf("Total: %d, block: %d", total, bl);
			
			return true;
		}
		return false;
	}
	
	@Override
	public void finalizeDestination(Entity player, WorldServer worldserver0, WorldServer worldserver1)
	{
		if(player instanceof EntityPlayerMP)
		{
			Debug.debug("Loading spawn chunks...");
			for(int chunkX = ((x - artifactRange) >> 4) - 1; chunkX <= ((x + artifactRange) >> 4) + 2; chunkX++)	//Prevent anything to generate on the piece that we move
				for(int chunkZ = ((z - artifactRange) >> 4) - 1; chunkZ <= ((z + artifactRange) >> 4) + 2; chunkZ++)	//from the overworld.
					worldserver1.getChunkProvider().provideChunk(chunkX, chunkZ);
			
			MinestuckDimensionHandler.setSpawn(worldserver1.provider.getDimension(), new BlockPos(x, y + yDiff, z));	//Set again, but with a more precise now that the y-coordinate is properly decided.
			
			for(BlockMove move : blockMoves)
			{
				move.copy(worldserver1.getChunkFromBlockCoords(move.getPosDest()));
			}
			
			Debug.debug("Teleporting entities...");
			AxisAlignedBB entityTeleportBB = player.getEntityBoundingBox().grow((double)artifactRange, artifactRange, (double)artifactRange);
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
							Teleport.teleportEntity(e, worldserver1.provider.getDimension(), null, e.posX, e.posY + yDiff, e.posZ);
						}
					}
					else	//Copy instead of teleport
					{
						Entity newEntity = EntityList.newEntity(player.getClass(), worldserver1);
						if (newEntity != null)
						{
							NBTTagCompound nbttagcompound = new NBTTagCompound();
							player.writeToNBT(nbttagcompound);
							nbttagcompound.removeTag("Dimension");
							newEntity.readFromNBT(nbttagcompound);
							newEntity.dimension = worldserver1.provider.getDimension();
							newEntity.setPosition(newEntity.posX, newEntity.posY + yDiff, newEntity.posZ);
							worldserver1.spawnEntity(newEntity);
						}
					}
				}
			}
			
			Debug.debug("Removing old blocks...");
			for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
			{
				int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
				boolean isEdgeX = Math.abs(blockX-x)==artifactRange;
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
					int height = (int) (Math.sqrt(artifactRange * artifactRange - radius*radius));
					int minY =  y - height;
					minY = minY < 0 ? 0 : minY;
					int maxY = MinestuckConfig.entryCrater ? Math.min(topY, y + height) + 1 : 256;
					boolean isEdgeZ = Math.abs(blockZ-z)==zWidth;
					for(int blockY = minY; blockY < maxY; blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						TileEntity tileEntity = worldserver0.getTileEntity(pos);
						if(MinestuckConfig.entryCrater)
						{
							if(worldserver0.getBlockState(pos).getBlock() != Blocks.BEDROCK)
							{
								if(tileEntity != null)
								{
									BlockPos pos1 = pos.up(yDiff);
									NBTTagCompound nbt = new NBTTagCompound();
									tileEntity.writeToNBT(nbt);
									nbt.setInteger("y", pos1.getY());
									TileEntity te1 = TileEntity.create(worldserver1, nbt);
									worldserver1.removeTileEntity(pos1);
									worldserver1.setTileEntity(pos1, te1);
									if(tileEntity instanceof TileEntityComputer)
										SkaianetHandler.movingComputer((TileEntityComputer) tileEntity, (TileEntityComputer) te1);

									try {
										worldserver0.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
									} catch (NullPointerException e) {e.printStackTrace();}
								} else if(isEdgeX || isEdgeZ || blockY == minY || blockY == maxY-1)
								{
									worldserver0.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
								} else
								{
									worldserver0.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
								}
							}
						} else
						{
							if(tileEntity != null)
								if(!creative)
									worldserver0.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
								else if(tileEntity instanceof TileEntityComputer)	//Avoid duplicating computer data when a computer is kept in the overworld
									((TileEntityComputer) tileEntity).programData = new NBTTagCompound();
								else if(tileEntity instanceof TileEntityTransportalizer)
									worldserver0.removeTileEntity(pos);
						}
					}
				}
			}
			
			player.setPositionAndUpdate(player.posX, player.posY + yDiff, player.posZ);
			
			SkaianetHandler.clearMovingList();
			
//			if(!creative || MinestuckConfig.entryCrater)
//			{
//				Debug.debug("Removing entities created from removing blocks...");	//Normally only items in containers
//				list = worldserver0.getEntitiesWithinAABBExcludingEntity(player, entityTeleportBB);
//				iterator = list.iterator();
//				while (iterator.hasNext())
//					if(MinestuckConfig.entryCrater)
//						iterator.next().setDead();
//					else
//					{
//						Entity e = iterator.next();
//						if(e instanceof EntityItem)
//							e.setDead();
//					}
//			}
			ServerEventHandler.tickTasks.add(new PostEntryTask(worldserver1.provider.getDimension(), x, y + yDiff, z, artifactRange, (byte) 0));
			Debug.info("Entry finished");
			
			Debug.debug("Placing gates...");
			
			GateHandler.findGatePlacement(worldserver1);
			placeGate(1, new BlockPos(player.posX, GateHandler.gateHeight1, player.posZ), worldserver1);
			placeGate(2, new BlockPos(player.posX, GateHandler.gateHeight2, player.posZ), worldserver1);
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
				if(world.isBlockModifiable(player, new BlockPos(blockX, y, blockZ)))
					return false;
		}
		
		return true;
	}
	
	private static void copyBlockDirect(Chunk chunkDest, Chunk chunkFrom, int x, int y, int y2, int z)
	{
		int j = y & 15, j2 = y2 & 15;
		ExtendedBlockStorage blockStorage = getBlockStorage(chunkDest, y >> 4);
		ExtendedBlockStorage blockStorage2 = getBlockStorage(chunkFrom, y2 >> 4);
		
		blockStorage.set(x, j, z, blockStorage2.get(x, j2, z));
		blockStorage.setBlockLight(x, j, z, blockStorage2.getBlockLight(x, j2, z));
		if(blockStorage2.getSkyLight() != null)
			blockStorage.setSkyLight(x, j, z, blockStorage2.getSkyLight(x, j2, z));
	}
	
	private static ExtendedBlockStorage getBlockStorage(Chunk c, int y)
	{
		ExtendedBlockStorage blockStorage = c.getBlockStorageArray()[y];
		if(blockStorage == null)
			blockStorage = c.getBlockStorageArray()[y] = new ExtendedBlockStorage(y << 4, c.getWorld().provider.hasSkyLight());
		return blockStorage;
	}
	
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
		private int x;
		private int y1;
		private int y2;
		private int z;
		private IBlockState block = null;

		BlockMove(Chunk c, int x, int y, int y2, int z)
		{
			this(c, x, y, y2, z, null);
		}
		
		BlockMove(Chunk c, int x, int y, int y2, int z, IBlockState b)
		{
			chunkFrom = c;
			this.x = x;
			this.y1 = y;
			this.y2 = y2;
			this.z = z;
			block = b;
		}
		
		BlockPos getPosDest()
		{
			return new BlockPos(x, y2, z);
		}
		
		void copy(Chunk chunkTo)
		{
			if(block != null)
			{
				chunkTo.setBlockState(new BlockPos(x, y2, z), block);
			} else
			{
				ItemCruxiteArtifact.copyBlockDirect(chunkTo, chunkFrom, x & 15, y1, y2, z & 15);
			}
		}
	}
}