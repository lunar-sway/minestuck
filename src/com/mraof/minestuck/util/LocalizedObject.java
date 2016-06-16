package com.mraof.minestuck.util;

import net.minecraft.util.text.translation.I18n;

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
		return I18n.translateToLocalFormatted(message, params);
	}
}