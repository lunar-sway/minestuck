package com.mraof.minestuck.entity.carapacian;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum EnumEntityKingdom implements StringRepresentable
{
	DERSITE,
	PROSPITIAN;
	
	@Override
	public String getSerializedName()
	{
		return name().toLowerCase(Locale.ROOT);
	}
}
