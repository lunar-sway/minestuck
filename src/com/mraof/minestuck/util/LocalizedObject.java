package com.mraof.minestuck.util;

import net.minecraft.util.StatCollector;

public class LocalizedObject
{
	String message;
	Object[] params;
	public LocalizedObject(String message, Object... params)
	{
		this.message = message;
		this.params = params;
	}
	@Override
	public String toString()
	{
		return StatCollector.translateToLocalFormatted(message, params);
	}
}