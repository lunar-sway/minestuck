package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.gen.lands.terrain.LandAspectFrost;
import com.mraof.minestuck.world.gen.lands.terrain.LandAspectHeat;
import com.mraof.minestuck.world.gen.lands.terrain.LandAspectPulse;
import com.mraof.minestuck.world.gen.lands.terrain.LandAspectSand;
import com.mraof.minestuck.world.gen.lands.terrain.LandAspectShade;
import com.mraof.minestuck.world.gen.lands.terrain.LandAspectThought;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;
import com.mraof.minestuck.world.gen.lands.title.LandAspectClockwork;
import com.mraof.minestuck.world.gen.lands.title.LandAspectFrogs;
import com.mraof.minestuck.world.gen.lands.title.LandAspectLight;
import com.mraof.minestuck.world.gen.lands.title.LandAspectNull;
import com.mraof.minestuck.world.gen.lands.title.LandAspectSilence;
import com.mraof.minestuck.world.gen.lands.title.LandAspectThunder;
import com.mraof.minestuck.world.gen.lands.title.LandAspectWind;
import com.mraof.minestuck.world.gen.lands.title.TitleAspect;
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
		registerLandAspect(new LandAspectPulse());
		registerLandAspect(new LandAspectShade());
		registerLandAspect(new LandAspectSand());
		registerLandAspect(new LandAspectThought());
		registerLandAspect(new LandAspectWind(), EnumAspect.BREATH);
		registerLandAspect(new LandAspectLight(), EnumAspect.LIGHT);
		registerLandAspect(new LandAspectClockwork(), EnumAspect.TIME);
		registerLandAspect(new LandAspectSilence(), EnumAspect.VOID);
		registerLandAspect(new LandAspectThunder(), EnumAspect.DOOM);
		
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
	@SuppressWarnings("rawtypes")
	public ArrayList pickSubset(ArrayList list1) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (Object obj : list1) {
			if (random.nextBoolean())
				result.add(obj);
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
			if (DimensionManager.getWorld(newLandId) == null && !MinestuckDimensionHandler.isLandDimension((byte)newLandId))
			{
				break;
			}
			else
			{
				newLandId++;
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
