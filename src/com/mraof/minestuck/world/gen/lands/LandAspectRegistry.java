package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLLog;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.gen.lands.terrain.*;
import com.mraof.minestuck.world.gen.lands.title.*;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class LandAspectRegistry
{
	
	private static ArrayList<TerrainAspect> landAspects = new ArrayList<TerrainAspect>();
	private static Hashtable<EnumAspect, ArrayList<TitleAspect>> titleAspects = new Hashtable<EnumAspect, ArrayList<TitleAspect>>();
	private static Hashtable<String, TerrainAspect> landNames = new Hashtable<String,TerrainAspect>();
	private static Hashtable<String, TitleAspect> landNames2 = new Hashtable<String,TitleAspect>();
	private static TitleAspect nullAspect = new LandAspectNull();
	public static TitleAspect frogAspect = new LandAspectFrogs();
	
	private Random random;
	
	public static void registerLandAspects()
	{
		registerLandAspect(new LandAspectFrost());
		registerLandAspect(new LandAspectHeat());
		registerLandAspect(new LandAspectShade());
		registerLandAspect(new LandAspectSand());
		registerLandAspect(new LandAspectForest());
		registerLandAspect(new LandAspectWind(), EnumAspect.BREATH);
		registerLandAspect(new LandAspectLight(), EnumAspect.LIGHT);
		registerLandAspect(new LandAspectClockwork(), EnumAspect.TIME);
		registerLandAspect(new LandAspectSilence(), EnumAspect.VOID);
		registerLandAspect(new LandAspectThunder(), EnumAspect.DOOM);
		registerLandAspect(new LandAspectPulse(), EnumAspect.BLOOD);
		registerLandAspect(new LandAspectThought(), EnumAspect.MIND);
		registerLandAspect(new LandAspectBuckets(), EnumAspect.SPACE);	//buckets -> containers -> space, right?
		registerLandAspect(new LandAspectFear(), EnumAspect.RAGE);
		registerLandAspect(new LandAspectCake(), EnumAspect.HEART);
//		registerLandAspect(new LandAspectBunnies(), EnumAspect.LIFE);
		
		
		landNames2.put(nullAspect.getPrimaryName(), nullAspect);
		landNames2.put(frogAspect.getPrimaryName(), frogAspect);
	}
	
	public LandAspectRegistry(long seed)
	{
		random = new Random(seed);
	}
	
	/**
	 * Adds a new Land aspect to the table of random aspects to generate.
	 * @param newAspect
	 */
	public static void registerLandAspect(TerrainAspect newAspect) {
		landAspects.add(newAspect);
		landNames.put(newAspect.getPrimaryName(),newAspect);
	}
	
	public static void registerLandAspect(TitleAspect newAspect, EnumAspect titleAspect)
	{
		if(!titleAspects.containsKey(titleAspect))
			titleAspects.put(titleAspect, new ArrayList<TitleAspect>());
		titleAspects.get(titleAspect).add(newAspect);
		landNames2.put(newAspect.getPrimaryName(), newAspect);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public TerrainAspect getLandAspect(TitleAspect aspect2)
	{
		while (true)
		{
			TerrainAspect newAspect = (TerrainAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getRarity() < random.nextFloat() && aspect2.isAspectCompatible(newAspect))
			{
				return newAspect;
			}
		}
	}
	
	public TitleAspect getLandAspect(EnumAspect titleAspect)
	{
		ArrayList<TitleAspect> aspectList = titleAspects.get(titleAspect);
		if(aspectList == null || aspectList.isEmpty())
			return nullAspect;
		while (true)
		{
			TitleAspect newAspect = aspectList.get(random.nextInt(aspectList.size()));
			if (newAspect.getRarity() < random.nextLong())
			{
				return newAspect;
			}
		}
	}
	
	/**
	 * Given two aspects, pick one ot random. Used in finding which aspect conrols what part of world gen.
	 */
	public TerrainAspect pickOne(TerrainAspect aspect1,TerrainAspect aspect2) {
		if (random.nextBoolean()) {
			return aspect1;
		} else {
			return aspect2;
		}
	}
	
	/**
	 * Returns a ArrayList that is a random combination of the two input ArrayLists.
	 */
	public <T> ArrayList<T> pickSubset(List<T> listIn, int min, int max)
	{
		ArrayList<T> result = new ArrayList<T>();
		if(listIn.size() <= min)
			result.addAll(listIn);
		else
		{
			int size = min + random.nextInt(max - min + 1);
			while(result.size() < size)
			{
				T object = listIn.get(random.nextInt(listIn.size()));
				if(!result.contains(object))
					result.add(object);
			}
		}
		return result;
	}
	
	/**
	 * Converts aspect data to NBT tags for saving/loading.
	 */
	public static NBTTagCompound toNBT(TerrainAspect aspect1, TitleAspect aspect2) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("aspect1",aspect1.getPrimaryName());
		tag.setString("aspect2",aspect2.getPrimaryName());
		return tag;
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static TerrainAspect fromName(String name) {
		return (TerrainAspect)landNames.get(name);
		
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static TitleAspect fromName2(String name)
	{
		return landNames2.get(name);
	}
	
	/**
	 * Registers a new dimension for a land. Returns the ID of the nearest open land ID.
	 * @param player 
	 * 
	 */
	public static int createLand(EntityPlayer player)
	{
		
		int newLandId = Minestuck.landDimensionIdStart;
		
		while (true)
		{
			if (!DimensionManager.isDimensionRegistered(newLandId) && BiomeGenBase.getBiome(newLandId) == null)
			{
				break;
			}
			else
			{
				newLandId++;
			}
			if(newLandId > Byte.MAX_VALUE)
			{
				FMLLog.warning("[Minestuck] Ran out of land id's!");
				return player.dimension;
			}
		}
		int id = SkaianetHandler.enterMedium((EntityPlayerMP)player, newLandId);
		if(id != newLandId)	//Player already got a land, but the tag was somehow lost?
			newLandId = id;
		
		if(!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("LandId", newLandId);
		return newLandId;
	}
	
	/**
	 * Returns one random element from a list.
	 */
	public Object pickElement(Object[] list)
	{
		return list[random.nextInt(list.length)];
	}
	
	public static class AspectCombination
	{
		public AspectCombination(TerrainAspect aspect1, TitleAspect aspect2)
		{
			if(aspect1 == null || aspect2 == null)
				throw new IllegalArgumentException("Parameters may not be null");
			this.aspect1 = aspect1;
			this.aspect2 = aspect2;
		}
		public TerrainAspect aspect1;
		public TitleAspect aspect2;
	}
	
}
