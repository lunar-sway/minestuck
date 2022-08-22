package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;

public enum EnumCassetteType implements StringRepresentable
{
	NONE(null),
	ELEVEN(SoundEvents.MUSIC_DISC_11),
	THIRTEEN(SoundEvents.MUSIC_DISC_13),
	BLOCKS(SoundEvents.MUSIC_DISC_BLOCKS),
	CAT(SoundEvents.MUSIC_DISC_CAT),
	CHIRP(SoundEvents.MUSIC_DISC_CHIRP),
	DANCE_STAB_DANCE(MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE),
	EMISSARY_OF_DANCE(MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE),
	FAR(SoundEvents.MUSIC_DISC_FAR),
	MALL(SoundEvents.MUSIC_DISC_MALL),
	MELLOHI(SoundEvents.MUSIC_DISC_MELLOHI),
	PIGSTEP(SoundEvents.MUSIC_DISC_PIGSTEP),
	RETRO_BATTLE_THEME(MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME),
	STAL(SoundEvents.MUSIC_DISC_STAL),
	STRAD(SoundEvents.MUSIC_DISC_STRAD),
	WAIT(SoundEvents.MUSIC_DISC_WAIT),
	WARD(SoundEvents.MUSIC_DISC_WARD);
	private final SoundEvent soundEvent;
	EnumCassetteType(SoundEvent soundEvent)
	{
		this.soundEvent = soundEvent;
	}
	
	@Override
	public String getSerializedName()
	{
		return name().toLowerCase();
	}
	
	public SoundEvent getSoundEvent()
	{
		return this.soundEvent;
	}
}
