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
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;

public class LandAspectRegistry
{
	
	private static ArrayList<TerrainLandAspect> landAspects = new ArrayList<TerrainLandAspect>();
	private static Hashtable<EnumAspect, ArrayList<TitleLandAspect>> titleAspects = new Hashtable<EnumAspect, ArrayList<TitleLandAspect>>();
	private static Hashtable<String, TerrainLandAspect> landNames = new Hashtable<String,TerrainLandAspect>();
	private static Hashtable<String, TitleLandAspect> landNames2 = new Hashtable<String,TitleLandAspect>();
	private static TitleLandAspect nullAspect = new LandAspectNull();
	public static TitleLandAspect frogAspect = new LandAspectFrogs();
	
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
	public static void registerLandAspect(TerrainLandAspect newAspect)
	{
		landAspects.add(newAspect);
		landNames.put(newAspect.getPrimaryName(),newAspect);
		for(ILandAspect variant : newAspect.getVariations())
			landNames.put(variant.getPrimaryName(), (TerrainLandAspect) variant);
	}
	
	public static void registerLandAspect(TitleLandAspect newAspect, EnumAspect titleAspect)
	{
		if(!titleAspects.containsKey(titleAspect))
			titleAspects.put(titleAspect, new ArrayList<TitleLandAspect>());
		titleAspects.get(titleAspect).add(newAspect);
		landNames2.put(newAspect.getPrimaryName(), newAspect);
		for(ILandAspect variant : newAspect.getVariations())
			landNames2.put(variant.getPrimaryName(), (TitleLandAspect) variant);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public TerrainLandAspect getTerrainAspect(TitleLandAspect aspect2, List<TerrainLandAspect> usedAspects)
	{
		ArrayList<TerrainLandAspect> availableAspects = new ArrayList<TerrainLandAspect>();
		for(TerrainLandAspect aspect : landAspects)
			if(aspect2.isAspectCompatible(aspect))
				availableAspects.add(aspect);
		
		if(availableAspects.isEmpty())
			throw new IllegalStateException("No land aspect is compatible with the title aspect "+aspect2.getPrimaryName()+"!");
		
		return selectRandomAspect(availableAspects, usedAspects);
	}
	
	public TitleLandAspect getTitleAspect(TerrainLandAspect aspectTerrain, EnumAspect titleAspect, List<TitleLandAspect> usedAspects)
	{
		ArrayList<TitleLandAspect> aspectList = titleAspects.get(titleAspect);
		ArrayList<TitleLandAspect> availableAspects = new ArrayList<TitleLandAspect>();
		if(aspectList == null || aspectList.isEmpty())
			return nullAspect;
		
		if(aspectTerrain == null)
			availableAspects.addAll(aspectList);
		else for(TitleLandAspect aspect : aspectList)
			if(aspect.isAspectCompatible(aspectTerrain))
				availableAspects.add(aspect);
		
		if(availableAspects.isEmpty())
		{
			Debug.debugf("Failed to find a title land aspect compatible with \"%s\". Forced to use a poorly compatible land aspect instead.");
			availableAspects.addAll(aspectList);
		}
		
		return selectRandomAspect(availableAspects, usedAspects);
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
		int size = min + random.nextInt(max - min + 1);
		ArrayList<T> result = new ArrayList<T>();
		if(listIn.size() <= size)
			result.addAll(listIn);
		else
		{
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
	public static NBTTagCompound toNBT(TerrainLandAspect aspect1, TitleLandAspect aspect2) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("aspect1",aspect1.getPrimaryName());
		tag.setString("aspect2",aspect2.getPrimaryName());
		return tag;
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static TerrainLandAspect fromNameTerrain(String name) {
		return (TerrainLandAspect)landNames.get(name);
		
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static TitleLandAspect fromNameTitle(String name)
	{
		return landNames2.get(name);
	}
	
	public static boolean containsTitleLandAspect(EnumAspect titleAspect, TitleLandAspect landAspect)
	{
		return titleAspects.get(titleAspect).contains(landAspect);
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
		else
		{
			int x = (int) player.posX;
			if(player.posX < 0) x--;
			int z = (int) player.posZ;
			if (player.posZ < 0) z--;
			MinestuckDimensionHandler.setSpawn(newLandId, new BlockPos(x, 128 - MinestuckConfig.artifactRange, z));
		}
		
		MinestuckPlayerTracker.updateLands();
		
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
		public AspectCombination(TerrainLandAspect terrainAspect, TitleLandAspect titleAspect)
		{
			if(terrainAspect == null || titleAspect == null)
				throw new IllegalArgumentException("Parameters may not be null");
			this.aspectTerrain = terrainAspect;
			this.aspectTitle = titleAspect;
		}
		public TerrainLandAspect aspectTerrain;
		/**
		 * Not to be confused with EnumAspect.
		 */
		public TitleLandAspect aspectTitle;
	}
	
}
