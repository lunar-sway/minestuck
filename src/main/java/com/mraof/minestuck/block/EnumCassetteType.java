package com.mraof.minestuck.block;

import net.minecraft.util.StringRepresentable;

public enum EnumCassetteType implements StringRepresentable
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
	public String getSerializedName()
	{
		return name().toLowerCase();
	}
}
