package com.mraof.minestuck.block;

import net.minecraft.util.IStringSerializable;

public enum EnumKeyType implements IStringSerializable
{
	none,
	tier_1_key,
	tier_2_key;
	
	@Override
	public String getName()
	{
		return name().toLowerCase();
	}
}
