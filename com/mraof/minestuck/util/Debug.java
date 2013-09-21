package com.mraof.minestuck.util;

public class Debug {

	public static boolean isDebugMode;

	public static void print(Object text) {
		if (isDebugMode) {
			System.out.println("[MINESTUCK] "+text);
		}
		try{
			if(text.toString().equals("false"))
				throw new Exception();
		} catch(Exception e){e.printStackTrace();}
	}
	public static void printf(String text, Object... args)
	{
		if (isDebugMode) 
		{
			System.out.printf("[MINESTUCK] " + text + "\n", args);
		}
	}
	//Don't use this unless you want to crash the game
	@SuppressWarnings("null")
	public static void crash()
	{
		Object nullObj = null;
		nullObj.toString();
	}

}
