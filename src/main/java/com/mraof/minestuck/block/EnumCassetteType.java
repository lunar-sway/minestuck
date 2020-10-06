package com.mraof.minestuck.block;

import net.minecraft.util.IStringSerializable;

public enum EnumCassetteType implements IStringSerializable
{
	NONE,
	THIRTEEN,
	CAT,
	BLOCKS,
	CHIRP,
	FAR,
	MALL,
	MELLOHI,
	EMISSARY_OF_DANCE,
	DANCE_STAB_DANCE,
	RETRO_BATTLE_THEME;
	
	@Override
	public String getString()
	{
		return name().toLowerCase();
	}
}
