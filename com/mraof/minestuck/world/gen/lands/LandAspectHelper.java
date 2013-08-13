package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import com.mraof.minestuck.util.Title;

public class LandAspectHelper {
	
	private static ArrayList landAspects = new ArrayList();
	private static Random random = new Random();
	
	/**
	 * Adds a new Land aspect to the table of random aspects to generate.
	 * @param newAspect
	 */
	public static void registerLandAspect(LandAspect newAspect) {
		landAspects.add(newAspect);
	}
	
	/**
	 * Generates a random land aspect, weighted based on a player's title.
	 * @param playerTitle
	 * @return
	 */
	public static LandAspect getLandAspect(Title playerTitle) {
		while (true) {
			LandAspect newAspect = (LandAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getWeight(playerTitle) < random.nextLong()) {
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
	public static LandAspect getLandAspect(Title playerTitle,LandAspect firstAspect) {
		while (true) {
			LandAspect newAspect = (LandAspect)landAspects.get(random.nextInt(landAspects.size()));
			if (newAspect.getWeight(playerTitle) < random.nextLong() && newAspect != firstAspect) {
				return newAspect;
			}
		}
	}
}
