package com.mraof.minestuck.item;

import java.util.Iterator;
import java.util.List;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
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
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.PostEntryTask;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.GateHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;

public abstract class ItemCruxiteArtifact extends Item implements ITeleporter
{
	
	public ItemCruxiteArtifact() 
	{
		this.setCreativeTab(Minestuck.tabMinestuck);
		setUnlocalizedName("cruxiteArtifact");
		this.maxStackSize = 1;
		setHasSubtypes(true);
	}
	
	public void onArtifactActivated(World world, EntityPlayer player)
	{
		try
		{
			if(!world.isRemote && player.worldObj.provider.getDimension() != -1)
			{
				if(!SburbHandler.shouldEnterNow(player))
					return;
				
				SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode(player), true);
				
				if(c == null || !c.enteredGame() || !MinestuckDimensionHandler.isLandDimension(player.worldObj.provider.getDimension()))
				{
					
					int destinationId;
					if(c != null && c.enteredGame())
						destinationId = c.getClientDimension();
					else destinationId = LandAspectRegistry.createLand(player);
					
					if(destinationId == -1)	//Something bad happened further down and the problem should be written in the server console
					{
						player.addChatComponentMessage(new TextComponentString("Something went wrong during entry. More details in the server console."));
						return;
					}
					
					if(!Teleport.teleportEntity(player, destinationId, this))
					{
						Debug.warn("Was not able to teleport player "+player.getName()+" into the medium! Likely caused by mod collision.");
						player.addChatComponentMessage(new TextComponentString("Was not able to teleport you into the medium! Likely caused by mod collision."));
					}
					else MinestuckPlayerTracker.sendLandEntryMessage(player);
				}
			}
		} catch(Exception e)
		{
			Debug.logger.error("Exception when "+player.getName()+" tried to enter their land.", e);
			player.addChatMessage(new TextComponentString("[Minestuck] Something went wrong during entry. "+ (Minestuck.isServerRunning?"Check the console for the error message.":"Notify the server owner about this.")).setChatStyle(new Style().setColor(TextFormatting.RED)));
		}
	}
	
	public void makeDestination(Entity entity, WorldServer worldserver0, WorldServer worldserver1)
	{
		if(entity instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) entity).addStat(MinestuckAchievementHandler.enterMedium);
			Debug.infof("Starting entry for player %s", entity.getName());
			int x = (int) entity.posX;
			if(entity.posX < 0) x--;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			if(entity.posZ < 0) z--;
			
			boolean creative = ((EntityPlayerMP) entity).interactionManager.isCreative();
			
			int topY = MinestuckConfig.adaptEntryBlockHeight ? getTopHeight(worldserver0, x, y, z) : y + artifactRange;
			int yDiff = 128 - topY;
			MinestuckDimensionHandler.setSpawn(worldserver1.provider.getDimension(), new BlockPos(x, y + yDiff, z));	//Set again, but with a more precise now that the y-coordinate is properly decided.
			
			Debug.debug("Loading spawn chunks...");
			for(int chunkX = ((x - artifactRange) >> 4) - 1; chunkX <= ((x + artifactRange) >> 4) + 2; chunkX++)	//Prevent anything to generate on the piece that we move
				for(int chunkZ = ((z - artifactRange) >> 4) - 1; chunkZ <= ((z + artifactRange) >> 4) + 2; chunkZ++)	//from the overworld.
					worldserver1.getChunkProvider().loadChunk(chunkX, chunkZ);
			
			Debug.debug("Placing blocks...");
			long time = System.currentTimeMillis();
			int bl = 0;
			for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
			{
				int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					Chunk chunk = worldserver1.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
					Chunk chunk2 = worldserver0.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
					int height = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2));
					for(int blockY = Math.max(0, y - height); blockY <= Math.min(topY, y + height); blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						BlockPos pos1 = pos.up(yDiff);
						IBlockState block = worldserver0.getBlockState(pos);
						TileEntity te = worldserver0.getTileEntity(pos);
						long t = System.currentTimeMillis();
						if(block.getBlock() != Blocks.bedrock && block.getBlock() != Blocks.portal)
						{
							copyBlockDirect(chunk, chunk2, blockX & 15, blockY + yDiff, blockY, blockZ & 15);
						}
						bl += System.currentTimeMillis() - t;
						if((te) != null)
						{
							TileEntity te1 = null;
							try {
								te1 = te.getClass().newInstance();
							} catch (Exception e) {e.printStackTrace();	continue;}
							NBTTagCompound nbt = new NBTTagCompound();
							te.writeToNBT(nbt);
							nbt.setInteger("y", pos1.getY());
							te1.readFromNBT(nbt);
							worldserver1.removeTileEntity(pos1);
							worldserver1.setTileEntity(pos1, te1);
							if(te instanceof TileEntityComputer)
								SkaianetHandler.movingComputer((TileEntityComputer) te, (TileEntityComputer) te1);
						}
					}
					for(int blockY = Math.min(topY, y + height) + yDiff; blockY < 256; blockY++)
						worldserver1.setBlockState(new BlockPos(blockX, blockY, blockZ), Blocks.air.getDefaultState(), 0);
				}
			}
			
			int total = (int) (System.currentTimeMillis() - time);
			Debug.debugf("Total: %d, block: %d", total, bl);
			
			Debug.debug("Teleporting entities...");
			List<?> list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand((double)artifactRange, artifactRange, (double)artifactRange));
			Iterator<?> iterator = list.iterator();
			
			entity.setPositionAndUpdate(entity.posX, entity.posY + yDiff, entity.posZ);
			while (iterator.hasNext())
			{
				Entity e = (Entity)iterator.next();
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
					Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
					if (newEntity != null)
					{
						NBTTagCompound nbttagcompound = new NBTTagCompound();
						entity.writeToNBT(nbttagcompound);
						nbttagcompound.removeTag("Dimension");
						newEntity.readFromNBT(nbttagcompound);
						newEntity.dimension = worldserver1.provider.getDimension();
						newEntity.setPosition(newEntity.posX, newEntity.posY + yDiff, newEntity.posZ);
						worldserver1.spawnEntityInWorld(newEntity);
					}
				}
			}
			
			Debug.debug("Removing old blocks...");
			for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
			{
				int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
					int height = (int) (Math.sqrt(artifactRange * artifactRange - radius*radius));
					int minY =  y - height;
					minY = minY < 0 ? 0 : minY;
					int maxY = MinestuckConfig.entryCrater ? Math.min(topY, y + height) + 1 : 256;
					for(int blockY = minY; blockY < maxY; blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						if(MinestuckConfig.entryCrater)
						{
							if(worldserver0.getBlockState(pos).getBlock() != Blocks.bedrock)
								worldserver0.setBlockState(pos, Blocks.air.getDefaultState(), 2);
						} else
						{
							TileEntity tileEntity = worldserver0.getTileEntity(pos);
							if(tileEntity != null)
								if(!creative)
									worldserver0.setBlockState(pos, Blocks.air.getDefaultState(), 2);
								else if(tileEntity instanceof TileEntityComputer)	//Avoid duplicating computer data when a computer is kept in the overworld
									((TileEntityComputer) worldserver0.getTileEntity(pos)).programData = new NBTTagCompound();
								else if(tileEntity instanceof TileEntityTransportalizer)
									worldserver0.removeTileEntity(pos);
						}
					}
				}
			}
			SkaianetHandler.clearMovingList();
			
			if(!(creative && MinestuckConfig.entryCrater))
			{
				Debug.debug("Removing entities created from removing blocks...");	//Normally only items in containers
				list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand((double)artifactRange, artifactRange, (double)artifactRange));
				iterator = list.iterator();
				while (iterator.hasNext())
					if(MinestuckConfig.entryCrater)
						((Entity)iterator.next()).setDead();
					else
					{
						Entity e = (Entity) iterator.next();
						if(e instanceof EntityItem)
							e.setDead();
					}
			}
			
			Debug.debug("Placing gates...");
			
			GateHandler.findGatePlacement(worldserver1);
			placeGate(1, new BlockPos(x, GateHandler.gateHeight1, z), worldserver1);
			placeGate(2, new BlockPos(x, GateHandler.gateHeight2, z), worldserver1);
			
			ServerEventHandler.tickTasks.add(new PostEntryTask(worldserver1.provider.getDimension(), x, y + yDiff, z, artifactRange, (byte) 0));
			
			Debug.info("Entry finished");
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
	
	private static void copyBlockDirect(Chunk c, Chunk c2, int x, int y, int y2, int z)
	{
		int j = y & 15, j2 = y2 & 15;
		ExtendedBlockStorage blockStorage = getBlockStorage(c, y >> 4);
		ExtendedBlockStorage blockStorage2 = getBlockStorage(c2, y2 >> 4);
		
		blockStorage.set(x, j, z, blockStorage2.get(x, j2, z));
		blockStorage.getBlocklightArray().set(x, j, z, blockStorage2.getBlocklightArray().get(x, j2, z));
		blockStorage.getSkylightArray().set(x, j, z, blockStorage2.getSkylightArray().get(x, j2, z));
	}
	
	private static ExtendedBlockStorage getBlockStorage(Chunk c, int y)
	{
		ExtendedBlockStorage blockStorage = c.getBlockStorageArray()[y];
		if(blockStorage == null)
			blockStorage = c.getBlockStorageArray()[y] = new ExtendedBlockStorage(y << 4, !c.getWorld().provider.getHasNoSky());
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
	
}