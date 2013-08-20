package com.mraof.minestuck.util;

public class Debug {

	public static boolean isDebugMode;

	public static void print(Object text) {
		if (isDebugMode) {
			System.out.println("[MINESTUCK] "+text);
		}
	}
	
}
