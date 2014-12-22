package com.mraof.minestuck.util;


public final class TitleHelper {
	
	public static EnumClass getClassFromInt(int i){
		return EnumClass.values()[i];
	}
	
	public static EnumAspect getAspectFromInt(int i){
		return EnumAspect.values()[i];
	}
	
	public static int getIntFromClass(EnumClass e){
		EnumClass[] list = EnumClass.values();
		for(int i = 0; i < list.length; i++)
			if(list[i].equals(e))
				return i;
		return -1;
	}
	
	public static int getIntFromAspect(EnumAspect e){
		EnumAspect[] list = EnumAspect.values();
		for(int i = 0; i < list.length; i++)
			if(list[i].equals(e))
				return i;
		return -1;
	}
	
	public static Title randomTitle()
	{
		return new Title(EnumClass.getRandomClass(null), EnumAspect.TIME/*EnumAspect.getRandomAspect(null)*/);
	}
	
}
