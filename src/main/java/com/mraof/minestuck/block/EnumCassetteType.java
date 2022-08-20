package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;

public enum EnumCassetteType implements StringRepresentable
{
	NONE,
	ELEVEN,
	THIRTEEN,
	BLOCKS,
	CAT,
	CHIRP,
	DANCE_STAB_DANCE,
	EMISSARY_OF_DANCE,
	FAR,
	MALL,
	MELLOHI,
	PIGSTEP,
	RETRO_BATTLE_THEME,
	STAL,
	STRAD,
	WAIT,
	WARD;
	
	@Override
	public String getSerializedName()
	{
		return name().toLowerCase();
	}
	
	public static SoundEvent getSoundEvent(EnumCassetteType type)
	{
		return switch(type)
				{
					case ELEVEN -> SoundEvents.MUSIC_DISC_11;
					case THIRTEEN -> SoundEvents.MUSIC_DISC_13;
					case BLOCKS -> SoundEvents.MUSIC_DISC_BLOCKS;
					case CAT -> SoundEvents.MUSIC_DISC_CAT;
					case CHIRP -> SoundEvents.MUSIC_DISC_CHIRP;
					case DANCE_STAB_DANCE -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE;
					case EMISSARY_OF_DANCE -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE;
					case FAR -> SoundEvents.MUSIC_DISC_FAR;
					case MALL -> SoundEvents.MUSIC_DISC_MALL;
					case MELLOHI -> SoundEvents.MUSIC_DISC_MELLOHI;
					case PIGSTEP -> SoundEvents.MUSIC_DISC_PIGSTEP;
					case RETRO_BATTLE_THEME -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME;
					case STAL -> SoundEvents.MUSIC_DISC_STAL;
					case STRAD -> SoundEvents.MUSIC_DISC_STRAD;
					case WAIT -> SoundEvents.MUSIC_DISC_WAIT;
					case WARD -> SoundEvents.MUSIC_DISC_WARD;
					case NONE -> null;
				};
	}
}
