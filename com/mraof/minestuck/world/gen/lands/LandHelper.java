package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Title;

public class LandHelper {
	
	private static ArrayList landAspects = new ArrayList<LandAspect>();
	private static Hashtable landNames = new Hashtable<String,LandAspect>();
	private Random random;
	
	public LandHelper(long seed) {
		random = new Random(seed);
	}
	
	/**
	 * Adds a new Land aspect to the table of random aspects to generate.
	 * @param newAspect
	 */
	public static void registerLandAspect(LandAspect newAspect) {
		landAspects.add(newAspect);
		landNames.put(newAspect.getPrimaryName(),newAspect);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public LandAspect getLandAspect(Title playerTitle) {
		while (true) {
			LandAspect newAspect = (LandAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getRarity(playerTitle) < random.nextFloat()) {
				return newAspect;
			}
		}
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title. Used for getting a second aspect, as it will make sure not to repeat the aspect given.
	 * @param playerTitle
	 * @param firstAspect
	 * @return
	 */
	public LandAspect getLandAspect(Title playerTitle,LandAspect firstAspect) {
		while (true) {
			LandAspect newAspect = (LandAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getRarity(playerTitle) < random.nextLong() && newAspect != firstAspect) {
				return newAspect;
			}
		}
	}
	
	/**
	 * Given two aspects, pick one ot random. Used in finding which aspect conrols what part of world gen.
	 */
	public LandAspect pickOne(LandAspect aspect1,LandAspect aspect2) {
		if (random.nextBoolean()) {
			return aspect1;
		} else {
			return aspect2;
		}
	}
	
	/**
	 * Returns a ArrayList that is a random combination of the two input ArrayLists.
	 */
	public ArrayList pickSubset(ArrayList list1, ArrayList list2) {
		ArrayList result = new ArrayList();
		for (Object obj : list1) {
			if (random.nextBoolean())
				result.add(obj);
		}
		for (Object obj : list2) {
			if (random.nextBoolean())
				result.add(obj);
		}
		return result;
	}
	
	public static NBTBase toNBT(LandAspect aspect1,LandAspect aspect2) {
		NBTTagCompound tag = new NBTTagCompound("LandData");
		tag.setString("aspect1",aspect1.getPrimaryName());
		tag.setString("aspect2",aspect2.getPrimaryName());
		return tag;
	}
	
	public static LandAspect fromName(String name) {
		return (LandAspect)landNames.get(name);
		
	}
	
	public static int createLand() {

		int newLandId = Minestuck.landDimensionIdStart;
		
		while (true) {
			if (DimensionManager.getWorld(newLandId) == null) {
				break;
			} else {
				newLandId++;
			}
		}
		
		DimensionManager.registerDimension(newLandId, Minestuck.landProviderTypeId);
		
		return newLandId;
	}
}
