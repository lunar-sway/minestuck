package com.mraof.minestuck.util;

import java.util.EnumSet;


public final class TitleHelper
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
