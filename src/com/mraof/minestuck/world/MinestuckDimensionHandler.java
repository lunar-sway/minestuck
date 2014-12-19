package com.mraof.minestuck.world;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.LandRegisterPacket;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.LandAspectRegistry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLLog;

public class MinestuckDimensionHandler
{
	
	private static Hashtable<Byte, LandAspectRegistry.AspectCombination> lands = new Hashtable<Byte, LandAspectRegistry.AspectCombination>();
	
	public static void unregisterDimensions()
	{
		for(Iterator<Byte> iterator = lands.keySet().iterator(); iterator.hasNext();)
		{
			byte b = iterator.next();
			if(DimensionManager.isDimensionRegistered(b))
			{
				DimensionManager.unregisterDimension(b);
			}
			BiomeGenBase.getBiomeGenArray()[b] = null;
		}
		lands.clear();
	}
	
	public static void saveData(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(Map.Entry<Byte, LandAspectRegistry.AspectCombination> entry : lands.entrySet())
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
			byte dim = tagCompound.getByte("dimID");
			String type = tagCompound.getString("type");
			if(type == "land")
			{
				String name1 = tagCompound.getString("aspect1");
				String name2 = tagCompound.getString("aspect2");
				LandAspectRegistry.AspectCombination aspects = new LandAspectRegistry.AspectCombination(LandAspectRegistry.fromName(name1), LandAspectRegistry.fromName2(name2));
				
				lands.put(dim, aspects);
				DimensionManager.registerDimension(dim, Minestuck.landProviderTypeId);
			}
		}
	}
	
	public static void registerLandDimension(byte dimensionId, LandAspectRegistry.AspectCombination landAspects)
	{
		if(landAspects == null)
			throw new IllegalArgumentException("May not register a land aspect combination that is null");
		if(!lands.containsKey(dimensionId) && !DimensionManager.isDimensionRegistered(dimensionId))
		{
			lands.put(dimensionId, landAspects);
			DimensionManager.registerDimension(dimensionId, Minestuck.landProviderTypeId);
		}
		else FMLLog.warning("[Minestuck] Did not register land dimension with id %d.", dimensionId);
	}
	
	public static LandAspectRegistry.AspectCombination getAspects(byte dimensionId)
	{
		LandAspectRegistry.AspectCombination aspects = lands.get(dimensionId);
		
		if(aspects == null)
		{
			FMLLog.warning("[Minestuck] Tried to access land aspect for dimension %d, but didn't find any!", dimensionId);
		}
		
		return aspects;
	}
	
	public static boolean isLandDimension(byte dimensionId)
	{
		return lands.containsKey(dimensionId);
	}
	
	public static Set<Map.Entry<Byte, LandAspectRegistry.AspectCombination>> getLandSet()
	{
		return lands.entrySet();
	}
	
	public static void onLandPacket(LandRegisterPacket packet)
	{
		lands.clear();
		
		for(int i = 0; i < packet.ids.size(); i++)
		{
			byte id = packet.ids.get(i);
			lands.put(id, packet.aspects.get(i));
			if(!DimensionManager.isDimensionRegistered(id))
				DimensionManager.registerDimension(id, Minestuck.landProviderTypeId);
		}
	}
	
	public static void worldTick(World world)
	{
		if(isLandDimension((byte)world.provider.getDimensionId()))
		{
			ChunkProviderLands chunkProvider = (ChunkProviderLands) world.provider.createChunkGenerator();
			int weatherType = chunkProvider.weatherType;
			if(weatherType != -1 && (weatherType & 4) != 0)
			{
				if(!world.isRaining())
					world.getWorldInfo().setRaining(true);
				if((weatherType & 2) != 0 && !world.isThundering())
					world.getWorldInfo().setThundering(true);
			}
		}
	}
	
}
