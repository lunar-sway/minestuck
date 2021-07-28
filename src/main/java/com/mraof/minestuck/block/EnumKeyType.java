package com.mraof.minestuck.block;

import net.minecraft.util.IStringSerializable;

public enum EnumKeyType implements IStringSerializable
{
	TIER_ONE,
	TIER_TWO;
	
	@Override
	public String getName()
	{
		return name().toLowerCase();
	}
}
