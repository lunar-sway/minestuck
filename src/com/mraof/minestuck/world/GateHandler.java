package com.mraof.minestuck.world;

import java.util.Map;

import com.mraof.minestuck.util.Debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class GateHandler
{
	
	public static final int gateHeight1 = 144, gateHeight2 = 192;
	
	static Map<Integer, BlockPos> gateData;
	
	public static void teleport(int gateId, int dim, EntityPlayer player)
	{
		//TODO
	}
	
	public static void findGatePlacement(World world)
	{
		int dim = world.provider.getDimensionId();
		if(MinestuckDimensionHandler.isLandDimension(dim) && !gateData.containsKey(dim))
		{
			BlockPos spawn = MinestuckDimensionHandler.getSpawn(dim);
			
			//TODO
			
		}
	}
	
	public static BlockPos getGatePos(int gateId, int dim)
	{
		if(!MinestuckDimensionHandler.isLandDimension(dim))
			return null;
		
		if(gateId == -1)
			return gateData.get(dim);
		else if(gateId == 1 || gateId == 2)
		{
			World world = DimensionManager.getWorld(dim);
			if(world == null) {
				DimensionManager.initDimension(dim);
				world = DimensionManager.getWorld(dim);
			}
			
			BlockPos spawn = world.provider.getSpawnPoint();
			int y;
			if(gateId == 1)
				y = gateHeight1;
			else y = gateHeight2;
			return new BlockPos(spawn.getX(), y, spawn.getZ());
		}
		
		return null;
	}
	
	public static void setDefiniteGatePos(int gateId, int dim, BlockPos newPos)
	{
		if(gateId == -1)
		{
			BlockPos oldPos = gateData.get(dim);
			if(oldPos.getY() != -1)
			{
				Debug.print("Trying to set position for a gate that should already be generated!");
				return;
			}
			
			gateData.put(dim, newPos);
		}
		else Debug.print("Trying to set position for a gate that should already be generated/doesn't exist!");
	}
	
	static void saveData(NBTTagList nbtList)
	{
		for(int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			if(nbt.getString("type").equals("land"))
			{
				int dim = nbt.getInteger("dimID");
				if(gateData.containsKey(dim))
				{
					BlockPos gatePos = gateData.get(dim);
					nbt.setInteger("gateX", gatePos.getX());
					nbt.setInteger("gateY", gatePos.getY());
					nbt.setInteger("gateZ", gatePos.getZ());
				}
			}
		}
	}
	
	static void loadData(NBTTagList nbtList)
	{
		for(int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			if(nbt.getString("type").equals("land") && nbt.hasKey("gateX"))
			{
				int dim = nbt.getInteger("dimID");
				BlockPos pos = new BlockPos(nbt.getInteger("gateX"), nbt.getInteger("gateY"), nbt.getInteger("gateZ"));
				gateData.put(dim, pos);
			}
		}
	}
	
}