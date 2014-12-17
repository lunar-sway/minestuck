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
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class LandHelper {
	
	private static ArrayList<PrimaryAspect> landAspects = new ArrayList<PrimaryAspect>();
	private static Hashtable<EnumAspect, ArrayList<SecondaryAspect>> titleAspects = new Hashtable<EnumAspect, ArrayList<SecondaryAspect>>();
	private static Hashtable<String, PrimaryAspect> landNames = new Hashtable<String,PrimaryAspect>();
	private static Hashtable<String, SecondaryAspect> landNames2 = new Hashtable<String,SecondaryAspect>();
	private static SecondaryAspect nullAspect = new LandAspectNull();
	
	private Random random;
	
	public LandHelper(long seed) {
		random = new Random(seed);
	}
	
	/**
	 * Adds a new Land aspect to the table of random aspects to generate.
	 * @param newAspect
	 */
	public static void registerLandAspect(PrimaryAspect newAspect) {
		landAspects.add(newAspect);
		landNames.put(newAspect.getPrimaryName(),newAspect);
	}
	
	public static void registerLandAspect(SecondaryAspect newAspect, EnumAspect titleAspect)
	{
		if(!titleAspects.containsKey(titleAspect))
			titleAspects.put(titleAspect, new ArrayList<SecondaryAspect>());
		titleAspects.get(titleAspect).add(newAspect);
		landNames2.put(newAspect.getPrimaryName(), newAspect);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public PrimaryAspect getLandAspect() {
		while (true) {
			PrimaryAspect newAspect = (PrimaryAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getRarity() < random.nextFloat()) {
				return newAspect;
			}
		}
	}
	
	public SecondaryAspect getLandAspect(EnumAspect titleAspect)
	{
		ArrayList<SecondaryAspect> aspectList = titleAspects.get(titleAspect);
		if(aspectList.isEmpty())
			return nullAspect;
		while (true)
		{
			SecondaryAspect newAspect = aspectList.get(random.nextInt(aspectList.size()));
			if (newAspect.getRarity() < random.nextLong())
			{
				return newAspect;
			}
		}
	}
	
	/**
	 * Given two aspects, pick one ot random. Used in finding which aspect conrols what part of world gen.
	 */
	public PrimaryAspect pickOne(PrimaryAspect aspect1,PrimaryAspect aspect2) {
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
	public static NBTTagCompound toNBT(PrimaryAspect aspect1, SecondaryAspect aspect2) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("aspect1",aspect1.getPrimaryName());
		tag.setString("aspect2",aspect2.getPrimaryName());
		return tag;
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static PrimaryAspect fromName(String name) {
		return (PrimaryAspect)landNames.get(name);
		
	}
	
	/**
	 * Gets a land aspect from it's primary name. Used in loading from NBT.
	 */
	public static SecondaryAspect fromName2(String name) {
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
			if (DimensionManager.getWorld(newLandId) == null && !MinestuckSaveHandler.lands.contains((byte)newLandId)) {
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
		else
		{
			DimensionManager.registerDimension(newLandId, Minestuck.landProviderTypeId);
			Debug.print("Creating land with id of: " + newLandId);
			//MinestuckSaveHandler.lands.add((byte) newLandId);
			MinestuckPlayerTracker.updateLands();
		}
		
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
		public AspectCombination(PrimaryAspect aspect1, SecondaryAspect aspect2)
		{
			this.aspect1 = aspect1;
			this.aspect2 = aspect2;
		}
		public PrimaryAspect aspect1;
		public SecondaryAspect aspect2;
	}
	
	
}
