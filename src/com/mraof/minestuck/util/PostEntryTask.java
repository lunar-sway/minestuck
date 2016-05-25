package com.mraof.minestuck.util;

import static com.mraof.minestuck.MinestuckConfig.artifactRange;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.server.FMLServerHandler;

public class PostEntryTask
{
	private static final long MIN_TIME = 20;
	
	private final int dimension;
	private final int x, y, z;
	private final int entrySize;
	private final byte entryType;	//Used if we add more ways for entry to happen
	private int index;
	
	public PostEntryTask(int dimension, int xCoord, int yCoord, int zCoord, int entrySize, byte entryType)
	{
		this.dimension = dimension;
		this.x = xCoord;
		this.y = yCoord;
		this.z = zCoord;
		this.entrySize = entrySize;
		this.entryType = entryType;
		this.index = 0;
	}
	
	public PostEntryTask(NBTTagCompound nbt)
	{
		this(nbt.getInteger("dimension"), nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"), nbt.getInteger("entrySize"), nbt.getByte("entryType"));
		this.index = nbt.getInteger("index");
	}
	
	public NBTTagCompound toNBTTagCompound()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("dimension", dimension);
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		nbt.setInteger("entrySize", entrySize);
		nbt.setByte("entryType", entryType);
		nbt.setInteger("index", index);
		
		return nbt;
	}
	
	public boolean onTick(MinecraftServer server)
	{
		WorldServer world = server.worldServerForDimension(dimension);
		
		if(world == null)
		{
			Debug.errorf("Couldn't find world for dimension %d when performing post entry preparations! Cancelling task.", dimension);
			return true;
		}
		
		int preIndex = index;
		main:
		{
			if(entryType == 0)
			{
				long time = System.currentTimeMillis() + MIN_TIME;
				int i = 0;
				for(int blockX = x - entrySize; blockX <= x + entrySize; blockX++)
				{
					int zWidth = (int) Math.sqrt(entrySize * entrySize - (blockX - x) * (blockX - x));
					for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
					{
						int height = (int) Math.sqrt(artifactRange * artifactRange - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2));
						if(blockX == x - entrySize || blockX == x + entrySize || blockZ == z - zWidth || blockZ == z + zWidth)
							for(int blockY = y - height; blockY <= Math.min(128, y + height); blockY++)
								i = updateBlock(new BlockPos(blockX, blockY, blockZ), world, i, true);
						else
						{
							i = updateBlock(new BlockPos(blockX, y - height, blockZ), world, i, true);
							for(int blockY = y - height + 1; blockY < Math.min(128, y + height); blockY++)
								i = updateBlock(new BlockPos(blockX, blockY, blockZ), world, i, false);
							i = updateBlock(new BlockPos(blockX, Math.min(128, y + height), blockZ), world, i, true);
						}
						if(time <= System.currentTimeMillis())
							break main;
					}
				}
			}
			
			Debug.infof("Completed entry block updates for dimension %d.", dimension);
			return true;
		}
		
		Debug.debugf("Updated %d blocks this tick.", index - preIndex);
		return false;
	}
	
	private int updateBlock(BlockPos pos, WorldServer world, int i, boolean blockUpdate)
	{
		if(i >= index)
		{
			if(blockUpdate)
				world.notifyNeighborsOfStateChange(pos, world.getBlockState(pos).getBlock());
			world.checkLight(pos);
			index++;
		}
		return i + 1;
	}
}