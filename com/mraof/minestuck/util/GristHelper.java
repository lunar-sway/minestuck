package com.mraof.minestuck.util;

import java.util.Random;

public class GristHelper {
	private static Random random = new Random();
	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist() {
		while (true) {
			//Debug.print("Index is " + index);
			GristType randGrist = GristType.values()[random.nextInt(GristType.allGrists)];
			if (randGrist.getRarity() > random.nextFloat()) {
				return randGrist;
			}
		}
	}
	
	/**
	 * Returns a GristSet representing the drops from an underling, given the underling's type and a static loot multiplier.
	 */
	public static GristSet getRandomDrop(GristType primary,int multiplier) {
		GristSet set = new GristSet();
		set.addGrist(GristType.Build,random.nextInt(20)*multiplier);
		set.addGrist(primary,random.nextInt(10)*multiplier);
		set.addGrist(getPrimaryGrist(),random.nextInt(5)*multiplier);
		return set;
		
	}
}
