package com.mraof.minestuck.world.lands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLLog;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;

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
		registerLandAspect(new LandAspectSandstone());
		registerLandAspect(new LandAspectForest());
		registerLandAspect(new LandAspectRock());
		
		registerLandAspect(new LandAspectWind(), EnumAspect.BREATH);
		registerLandAspect(new LandAspectLight(), EnumAspect.LIGHT);
		registerLandAspect(new LandAspectClockwork(), EnumAspect.TIME);
		registerLandAspect(new LandAspectSilence(), EnumAspect.VOID);
		registerLandAspect(new LandAspectThunder(), EnumAspect.DOOM);
		registerLandAspect(new LandAspectPulse(), EnumAspect.BLOOD);
		registerLandAspect(new LandAspectThought(), EnumAspect.MIND);
		registerLandAspect(new LandAspectBuckets(), EnumAspect.SPACE);	//buckets -> containers -> space, right?
		registerLandAspect(new LandAspectCake(), EnumAspect.HEART);
		registerLandAspect(new LandAspectRabbits(), EnumAspect.LIFE);
		registerLandAspect(new LandAspectMonsters(), EnumAspect.RAGE);
		registerLandAspect(new LandAspectTowers(), EnumAspect.HOPE);
		
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
	public static void registerLandAspect(TerrainAspect newAspect)
	{
		landAspects.add(newAspect);
		landNames.put(newAspect.getPrimaryName(),newAspect);
		for(ILandAspect variant : newAspect.getVariations())
			landNames.put(variant.getPrimaryName(), (TerrainAspect) variant);
	}
	
	public static void registerLandAspect(TitleAspect newAspect, EnumAspect titleAspect)
	{
		if(!titleAspects.containsKey(titleAspect))
			titleAspects.put(titleAspect, new ArrayList<TitleAspect>());
		titleAspects.get(titleAspect).add(newAspect);
		landNames2.put(newAspect.getPrimaryName(), newAspect);
		for(ILandAspect variant : newAspect.getVariations())
			landNames2.put(variant.getPrimaryName(), (TitleAspect) variant);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public TerrainAspect getLandAspect(TitleAspect aspect2, List<TerrainAspect> usedAspects)
	{
		ArrayList<TerrainAspect> availableAspects = new ArrayList<TerrainAspect>();
		for(TerrainAspect aspect : landAspects)
			if(aspect2.isAspectCompatible(aspect))
				availableAspects.add(aspect);
		
		if(availableAspects.isEmpty())
			throw new IllegalStateException("No land aspect is compatible with the title aspect "+aspect2.getPrimaryName()+"!");
		
		return selectRandomAspect(availableAspects, usedAspects);
	}
	
	public TitleAspect getTitleAspect(EnumAspect titleAspect, List<TitleAspect> usedAspects)
	{
		ArrayList<TitleAspect> aspectList = titleAspects.get(titleAspect);
		if(aspectList == null || aspectList.isEmpty())
			return nullAspect;
		
		return selectRandomAspect(aspectList, usedAspects);
	}
	
	private <A extends ILandAspect> A selectRandomAspect(List<A> aspectList, List<A> usedAspects)
	{
		if(aspectList.size() == 1)
			return getRandomVariant(aspectList.get(0));
		
		int[] useCount = new int[aspectList.size()];
		for(A usedAspect : usedAspects)
		{
			usedAspect = (A) usedAspect.getPrimaryVariant();
			int index = aspectList.indexOf(usedAspect);
			if(index != -1)
				useCount[index]++;
		}
		
		ArrayList<A> list = new ArrayList<A>();
		for(int i = 0; i < useCount.length; i++)	//Check for unused aspects
			if(useCount[i] == 0)
				list.add(aspectList.get(i));
		
		if(list.size() > 0)
			return getRandomVariant(list.get(random.nextInt(list.size())));
		
		double randCap = 0;
		for(int i = 0; 0 < useCount.length; i++)
			randCap += 1D/useCount[i];
		
		double rand = random.nextDouble()*randCap;
		
		for(int i = 0; i < useCount.length; i++)
			if(rand < 1D/useCount[i])
			{
				return getRandomVariant(aspectList.get(i));
			}
			else rand -= 1D/useCount[i];
		
		return null;	//Should not happen
	}
	
	private <A extends ILandAspect> A getRandomVariant(A aspect)
	{
		List<ILandAspect> variations = aspect.getVariations();
		return (A) variations.get(random.nextInt(variations.size()));
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
			if (!DimensionManager.isDimensionRegistered(newLandId))
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
		else MinestuckDimensionHandler.setSpawn(newLandId, new BlockPos(player.posX, 128 - MinestuckConfig.artifactRange, player.posZ));
		
		MinestuckPlayerTracker.updateLands();
		
		if(!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("LandId", newLandId);
		return newLandId;
	}
	
	/**
	 * Returns one random element from a list.
	 */
	public <T> T pickElement(T[] list)
	{
		return list[random.nextInt(list.length)];
	}
	
	public static class AspectCombination
	{
		public AspectCombination(TerrainAspect terrainAspect, TitleAspect titleAspect)
		{
			if(terrainAspect == null || titleAspect == null)
				throw new IllegalArgumentException("Parameters may not be null");
			this.aspectTerrain = terrainAspect;
			this.aspectTitle = titleAspect;
		}
		public TerrainAspect aspectTerrain;
		public TitleAspect aspectTitle;
	}
	
}
