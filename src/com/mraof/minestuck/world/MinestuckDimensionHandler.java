package com.mraof.minestuck.world;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.LandRegisterPacket;
import com.mraof.minestuck.world.biome.BiomeGenMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeGenBase.BiomeProperties;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinestuckDimensionHandler
{
	
	public static int skaiaProviderTypeId;
	public static int skaiaDimensionId;
	public static int landProviderTypeId;
	public static int landDimensionIdStart;
	public static int biomeIdStart;
	
	private static Hashtable<Integer, LandAspectRegistry.AspectCombination> lands = new Hashtable<Integer, LandAspectRegistry.AspectCombination>();
	private static Hashtable<Integer, BlockPos> spawnpoints = new Hashtable<Integer, BlockPos>();
	
	public static DimensionType landDimensionType;
	public static DimensionType skaiaDimensionType;
	
	public static void register()
	{
		//register world generators
		landDimensionType = DimensionType.register("The Medium", "_medium", landProviderTypeId, WorldProviderLands.class, MinestuckConfig.keepDimensionsLoaded);
		skaiaDimensionType = DimensionType.register("Skaia", "_skaia", skaiaProviderTypeId, WorldProviderSkaia.class, false);
		
		DimensionManager.registerDimension(skaiaDimensionId, skaiaDimensionType);
		
		BiomeGenMinestuck.mediumNormal = GameRegistry.register(new BiomeGenMinestuck(new BiomeProperties("The Medium")).setRegistryName("medium"));
		BiomeGenMinestuck.mediumOcean = GameRegistry.register(new BiomeGenMinestuck(new BiomeProperties("The Medium (Ocean)").setBaseBiome("medium")).setRegistryName("medium_ocean"));
	}
	
	public static void unregisterDimensions()
	{
		for(Iterator<Integer> iterator = lands.keySet().iterator(); iterator.hasNext();)
		{
			int b = iterator.next();
			if(DimensionManager.isDimensionRegistered(b))
			{
				DimensionManager.unregisterDimension(b);
			}
		}
		lands.clear();
		spawnpoints.clear();
		GateHandler.gateData.clear();
	}
	
	public static void saveData(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(Map.Entry<Integer, LandAspectRegistry.AspectCombination> entry : lands.entrySet())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("dimID", entry.getKey());
			tagCompound.setString("type", "land");
			tagCompound.setString("aspect1", entry.getValue().aspectTerrain.getPrimaryName());
			tagCompound.setString("aspect2", entry.getValue().aspectTitle.getPrimaryName());
			BlockPos spawn = spawnpoints.get(entry.getKey());
			tagCompound.setInteger("spawnX", spawn.getX());
			tagCompound.setInteger("spawnY", spawn.getY());
			tagCompound.setInteger("spawnZ", spawn.getZ());
			list.appendTag(tagCompound);
		}
		GateHandler.saveData(list);
		nbt.setTag("dimensionData", list);
	}
	
	public static void loadData(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("dimensionData", new NBTTagCompound().getId());
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tagCompound = list.getCompoundTagAt(i);
			int dim = tagCompound.getInteger("dimID");
			String type = tagCompound.getString("type");
			if(type.equals("land"))
			{
				String name1 = tagCompound.getString("aspect1");
				String name2 = tagCompound.getString("aspect2");
				LandAspectRegistry.AspectCombination aspects = new LandAspectRegistry.AspectCombination(LandAspectRegistry.fromNameTerrain(name1), LandAspectRegistry.fromNameTitle(name2));
				BlockPos spawn = new BlockPos(tagCompound.getInteger("spawnX"), tagCompound.getInteger("spawnY"), tagCompound.getInteger("spawnZ"));
				
				lands.put(dim, aspects);
				spawnpoints.put(dim, spawn);
				DimensionManager.registerDimension(dim, landDimensionType);
			}
		}
		GateHandler.loadData(list);
	}
	
	public static void registerLandDimension(int dimensionId, LandAspectRegistry.AspectCombination landAspects)
	{
		if(landAspects == null)
			throw new IllegalArgumentException("May not register a land aspect combination that is null");
		if(!lands.containsKey(dimensionId) && !DimensionManager.isDimensionRegistered(dimensionId))
		{
			lands.put(dimensionId, landAspects);
			DimensionManager.registerDimension(dimensionId, landDimensionType);
		}
		else FMLLog.warning("[Minestuck] Did not register land dimension with id %d.", dimensionId);
	}
	
	public static LandAspectRegistry.AspectCombination getAspects(int dimensionId)
	{
		LandAspectRegistry.AspectCombination aspects = lands.get(dimensionId);
		
		if(aspects == null)
		{
			FMLLog.warning("[Minestuck] Tried to access land aspect for dimension %d, but didn't find any!", dimensionId);
		}
		
		return aspects;
	}
	
	public static boolean isLandDimension(int dimensionId)
	{
		return lands.containsKey(dimensionId);
	}
	
	public static Set<Map.Entry<Integer, LandAspectRegistry.AspectCombination>> getLandSet()
	{
		return lands.entrySet();
	}
	
	public static void onLandPacket(LandRegisterPacket packet)
	{
		if(Minestuck.isServerRunning)
			return;
		lands.clear();
		spawnpoints.clear();
		
		lands.putAll(packet.aspectMap);
		spawnpoints.putAll(packet.spawnMap);
		
		for(int dim : lands.keySet())
		{
			if(!DimensionManager.isDimensionRegistered(dim))
				DimensionManager.registerDimension(dim, landDimensionType);
		}
	}
	
	public static BlockPos getSpawn(int dim)
	{
		return spawnpoints.get(dim);
	}
	
	public static void setSpawn(int dim, BlockPos spawnpoint)
	{
		spawnpoints.put(dim, spawnpoint);
	}
}
