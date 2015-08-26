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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;

import com.mraof.minestuck.block.BlockGate;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.Teleport;
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
	
	protected void onArtifactActivated(World world, EntityPlayer player)
	{
		if(!world.isRemote && player.worldObj.provider.getDimensionId() != -1)
		{
			int destinationId = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("LandId");
			
			if(!MinestuckDimensionHandler.isLandDimension(destinationId))
				destinationId = LandAspectRegistry.createLand(player);
			
			if(player.worldObj.provider.getDimensionId() != destinationId)
			{
				player.triggerAchievement(MinestuckAchievementHandler.enterMedium);
				Teleport.teleportEntity(player, destinationId, this);
				int yDiff = 128 - artifactRange - (int) player.posY;
				player.setPositionAndUpdate(player.posX, player.posY + yDiff, player.posZ);
				MinestuckPlayerTracker.sendLandEntryMessage(player);
			}
		}
	}
	
	public void makeDestination(Entity entity, WorldServer worldserver0, WorldServer worldserver1)
	{
		if(entity instanceof EntityPlayerMP && entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("LandId") == worldserver1.provider.getDimensionId())
		{
			int x = (int) entity.posX;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			
			int yDiff = 128 - artifactRange - y;
			
			Debug.print("Loading spawn chunks...");
			for(int chunkX = ((x - artifactRange) >> 4) - 1; chunkX <= ((x + artifactRange) >> 4) + 2; chunkX++)	//Prevent anything to generate on the piece that we move
				for(int chunkZ = ((z - artifactRange) >> 4) - 1; chunkZ <= ((z + artifactRange) >> 4) + 2; chunkZ++)	//from the overworld.
					worldserver1.theChunkProviderServer.loadChunk(chunkX, chunkZ);
			
			Debug.print("Teleporting entities...");
			List<?> list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand((double)artifactRange, artifactRange, (double)artifactRange));
			Iterator<?> iterator = list.iterator();
			
			while (iterator.hasNext())
			{
				Entity e = (Entity)iterator.next();
				if(MinestuckConfig.entryCrater || e instanceof EntityPlayer || e instanceof EntityItem)
				{
					e.setPosition(e.posX, e.posY + yDiff, e.posZ);
					Teleport.teleportEntity(e, worldserver1.provider.getDimensionId(), this);
				}
				else	//Copy instead of teleport
				{
					Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
					if (newEntity != null)
					{
						newEntity.copyDataFromOld(entity);
						newEntity.dimension = worldserver1.provider.getDimensionId();
						newEntity.setPosition(newEntity.posX, newEntity.posY + yDiff, newEntity.posZ);
						worldserver1.spawnEntityInWorld(newEntity);
					}
				}
			}
			Debug.print("Placing blocks...");
			int nextZWidth = 0;
			for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
			{
				int zWidth = nextZWidth;
				nextZWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x + 1) * (blockX - x + 1));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					int height = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2));
					int heightX = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x + 1) * (blockX - x + 1) + (blockZ - z) * (blockZ - z)) / 2));
					int heightZ = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z + 1) * (blockZ - z + 1)) / 2));
					int blockY;
					for(blockY = Math.max(0, y - height); blockY < Math.min(256, y + height); blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						BlockPos pos1 = pos.up(yDiff);
						IBlockState block = worldserver0.getBlockState(pos);
						TileEntity te = worldserver0.getTileEntity(pos);
						if(block.getBlock() != Blocks.air && !block.getBlock().isSolidFullCube()) //Place temp blocks to avoid things like torches breaking because of missing solid block
						{
							if(blockZ >= z - nextZWidth && blockZ <= z + nextZWidth && blockY >= y - heightX && blockY < y + heightX)
								worldserver1.setBlockState(pos1.east(), Blocks.stone.getDefaultState(), 0);
							if(blockZ < z + zWidth && blockY >= y - heightZ && blockY < y + heightZ)
								worldserver1.setBlockState(pos1.south(), Blocks.stone.getDefaultState(), 0);
						}
						if(block.getBlock() != Blocks.bedrock)
							worldserver1.setBlockState(pos1, block, 0);
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
						};
					}
					for(blockY += yDiff; blockY < 256; blockY++)
						worldserver1.setBlockState(new BlockPos(blockX, blockY, blockZ), Blocks.air.getDefaultState(), 0);
				}
			}
			
			Debug.print("Removing old blocks...");
			for(int blockX = x - artifactRange; blockX <= x + artifactRange; blockX++)
			{
				int zWidth = (int) Math.sqrt(artifactRange * artifactRange - (blockX - x) * (blockX - x));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
					int height = (int) (Math.sqrt(artifactRange * artifactRange - radius*radius));
					int minY =  y - height;
					minY = minY < 0 ? 0 : minY;
					int maxY = MinestuckConfig.entryCrater ? y + height : 256;
					for(int blockY = minY; blockY < 256; blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						if(MinestuckConfig.entryCrater)
						{
							if(worldserver0.getBlockState(pos).getBlock() != Blocks.bedrock)
								worldserver0.setBlockState(pos, Blocks.air.getDefaultState(), 2);
						} else
							if(worldserver0.getTileEntity(pos) != null)
								worldserver0.setBlockState(pos, Blocks.air.getDefaultState(), 2);
					}
				}
			}
			SkaianetHandler.clearMovingList();
			
			Debug.print("Making sure that old entities are removed...");
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
			
			Debug.print("Placing gates...");
			
			GateHandler.findGatePlacement(worldserver1);
			placeGate(1, new BlockPos(x, GateHandler.gateHeight1, z), worldserver1);
			placeGate(2, new BlockPos(x, GateHandler.gateHeight2, z), worldserver1);
			
			Debug.print("Entry finished");
		}
	}
	
	private static void placeGate(int gateCount, BlockPos pos, WorldServer world)
	{
		for(int i = 0; i < 9; i++)
			if(i == 4)
			{
				world.setBlockState(pos, Minestuck.gate.getDefaultState().cycleProperty(BlockGate.isMainComponent), 0);
				TileEntityGate tileEntity = (TileEntityGate) world.getTileEntity(pos);
				tileEntity.gateCount = gateCount;
			}
			else world.setBlockState(pos.add((i % 3) - 1, 0, i/3 - 1), Minestuck.gate.getDefaultState(), 0);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		if(stack.getMetadata() == 0)
			return -1;
		else return ColorCollector.getColor(stack.getMetadata() - 1);
	}
}
