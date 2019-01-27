package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.terrain.*;
import com.mraof.minestuck.world.lands.title.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLLog;

import java.util.*;

public class LandAspectRegistry
{
	
	private static ArrayList<TerrainLandAspect> landAspects = new ArrayList<TerrainLandAspect>();
	private static Hashtable<EnumAspect, ArrayList<TitleLandAspect>> titleAspects = new Hashtable<EnumAspect, ArrayList<TitleLandAspect>>();
	private static Hashtable<String, TerrainLandAspect> landNames = new Hashtable<String,TerrainLandAspect>();
	private static Hashtable<String, TitleLandAspect> landNames2 = new Hashtable<String,TitleLandAspect>();
	private static TitleLandAspect nullAspect = new LandAspectNull();
	public static TitleLandAspect frogAspect = new LandAspectFrogs();
	public static TitleLandAspect bucketAspect = new LandAspectBuckets();
	
	private Random random;
	
	public static void registerLandAspects()
	{
		registerLandAspect(new LandAspectForest());
		registerLandAspect(new LandAspectFrost());
		registerLandAspect(new LandAspectFungi());
		registerLandAspect(new LandAspectHeat());
		registerLandAspect(new LandAspectRock());
		registerLandAspect(new LandAspectSand());
		registerLandAspect(new LandAspectSandstone());
		registerLandAspect(new LandAspectShade());
		registerLandAspect(new LandAspectWood());
		registerLandAspectHidden(new LandAspectRain());
		registerLandAspect(new LandAspectRainbow());
		registerLandAspect(new LandAspectFlora());
		registerLandAspect(new LandAspectEnd());
		
		registerLandAspect(new LandAspectWind(), EnumAspect.BREATH);
		registerLandAspect(new LandAspectLight(), EnumAspect.LIGHT);
		registerLandAspect(new LandAspectClockwork(), EnumAspect.TIME);
		registerLandAspect(new LandAspectSilence(), EnumAspect.VOID);
		registerLandAspect(new LandAspectThunder(), EnumAspect.DOOM);
		registerLandAspect(new LandAspectPulse(), EnumAspect.BLOOD);
		registerLandAspect(new LandAspectThought(), EnumAspect.MIND);
		//registerLandAspect(new LandAspectBuckets(), EnumAspect.SPACE);
		registerLandAspect(new LandAspectFrogs(), EnumAspect.SPACE);
		registerLandAspect(new LandAspectCake(), EnumAspect.HEART);
		registerLandAspect(new LandAspectRabbits(), EnumAspect.LIFE);
		registerLandAspect(new LandAspectMonsters(), EnumAspect.RAGE);
		registerLandAspect(new LandAspectTowers(), EnumAspect.HOPE);
		

		landNames2.put(nullAspect.getPrimaryName(), nullAspect);
		landNames2.put(frogAspect.getPrimaryName(), frogAspect);
		landNames2.put(bucketAspect.getPrimaryName(), bucketAspect);
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
	
	public static void registerLandAspectHidden(TerrainLandAspect newAspect)
	{
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
		for(int i = 0; i < useCount.length; i++)
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
	
	public static Collection<String> getNamesTerrain()
	{
		return landNames.keySet();
	}
	
	public static Collection<String> getNamesTitle()
	{
		return landNames2.keySet();
	}
	
	public static boolean containsTitleLandAspect(EnumAspect titleAspect, TitleLandAspect landAspect)
	{
		return titleAspects.get(titleAspect).contains(landAspect);
	}
	
	public static TitleLandAspect getSingleLandAspect(EnumAspect aspect)
	{
		if(titleAspects.get(aspect).size() == 1)
			return titleAspects.get(aspect).get(0);
		else return null;
	}
	
	/**
	 * Registers a new dimension for a land. Returns the ID of the nearest open land ID.
	 * @param player The player whose Land is being created
	 * @param teleport The teleporter in charge of carrying the player into the Land.
	 * If this value is null, the Land will be created, and the player will be left behind.
	 * <code>ItemCruxiteArtifact</code> is the recommended teleporter for Entry.
	 * @return Returns the dimension of the player's Land, or -1 if Entry fails.
	 */
	public static int createLand(EntityPlayer player, Teleport.ITeleporter teleport)
	{
		
		int newLandId = MinestuckDimensionHandler.landDimensionIdStart;
		
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
		}
		
		int id = SkaianetHandler.enterMedium((EntityPlayerMP)player, newLandId, teleport);
		
		if(id == -1)
			return -1;	//Something happened at skaianet preventing the Land from being made
		
		if(id != newLandId)			//This happens iff the player has a "home dimension" Land already registered
			newLandId = id;
		
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
