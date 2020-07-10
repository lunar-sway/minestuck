package com.mraof.minestuck.block;

import net.minecraft.util.IStringSerializable;

public enum EnumDowelType implements IStringSerializable
{
	NONE,
	DOWEL,
	CARVED_DOWEL;
	
	
	@Override
	public String getName()
	{
		return name().toLowerCase();
	}
}
