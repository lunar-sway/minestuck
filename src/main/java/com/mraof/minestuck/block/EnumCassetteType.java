package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
	
	public static SoundEvent getSoundEvent(EnumCassetteType type)
	{
		return switch(type)
				{
					case THIRTEEN -> SoundEvents.MUSIC_DISC_13;
					case CAT -> SoundEvents.MUSIC_DISC_CAT;
					case BLOCKS -> SoundEvents.MUSIC_DISC_BLOCKS;
					case CHIRP -> SoundEvents.MUSIC_DISC_CHIRP;
					case FAR -> SoundEvents.MUSIC_DISC_FAR;
					case MALL -> SoundEvents.MUSIC_DISC_MALL;
					case MELLOHI -> SoundEvents.MUSIC_DISC_MELLOHI;
					case EMISSARY_OF_DANCE -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE;
					case DANCE_STAB_DANCE -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE;
					case RETRO_BATTLE_THEME -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME;
					case NONE -> null;
				};
	}
}
