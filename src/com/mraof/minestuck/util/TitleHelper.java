package com.mraof.minestuck.util;

public final class TitleHelper	//I don't think we have any need for this
{
	
	public static EnumClass getClassFromInt(int i)
	{
		return EnumClass.values()[i];
	}
	
	public static EnumAspect getAspectFromInt(int i)
	{
		return EnumAspect.values()[i];
	}
	
	public static int getIntFromClass(EnumClass e)
	{
		return e.ordinal();
	}
	
	public static int getIntFromAspect(EnumAspect e)
	{
		return e.ordinal();
	}
	
}
