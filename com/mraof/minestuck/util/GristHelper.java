package com.mraof.minestuck.util;

import java.util.Random;

public class GristHelper {
	private static Random random;
	
	public static int getPrimaryGrist() {
		while (true) {
			int index = random.nextInt(GristType.allGrists);
			if (GristType.values()[index].getRarity() > random.nextFloat()) {
				return index;
			}
		}
	}
	
	
}
