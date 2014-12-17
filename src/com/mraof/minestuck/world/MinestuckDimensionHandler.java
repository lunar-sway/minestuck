package com.mraof.minestuck.world;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.mraof.minestuck.world.gen.lands.LandHelper.AspectCombination;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.DimensionManager;

public class MinestuckDimensionHandler
{
	
	public static Hashtable<Byte, AspectCombination> lands = new Hashtable<Byte, AspectCombination>();
	
	public static void onServerAboutToStart()
	{
		for(Iterator<Byte> iterator = lands.keySet().iterator(); iterator.hasNext();)
		{
			byte b = iterator.next();
			if(DimensionManager.isDimensionRegistered(b))
			{
				DimensionManager.unregisterDimension(b);
			}
		}
		lands.clear();
	}
	
	public static void saveData(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(Map.Entry<Byte, AspectCombination> entry : lands.entrySet())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setByte("dimID", entry.getKey());
			tagCompound.setString("type", "land");
			tagCompound.setString("aspect1", entry.getValue().aspect1.getPrimaryName());
			tagCompound.setString("aspect2", entry.getValue().aspect2.getPrimaryName());
			list.appendTag(tagCompound);
		}
		nbt.setTag("dimensionData", list);
	}
	
	public static void loadData(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("dimensionData", new NBTTagCompound().getId());
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tagCompound = list.getCompoundTagAt(i);
			
		}
	}
	
}
