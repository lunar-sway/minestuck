package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public enum EnumCassetteType implements StringRepresentable
{
	NONE(() -> null, -1),
	ELEVEN(() -> SoundEvents.MUSIC_DISC_11, 79),
	THIRTEEN(() -> SoundEvents.MUSIC_DISC_13, 71),
	BLOCKS(() -> SoundEvents.MUSIC_DISC_BLOCKS, 85),
	CAT(() -> SoundEvents.MUSIC_DISC_CAT, 112),
	CHIRP(() -> SoundEvents.MUSIC_DISC_CHIRP, 110),
	DANCE_STAB_DANCE(() -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, 165),
	EMISSARY_OF_DANCE(() -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, 135),
	FAR(() -> SoundEvents.MUSIC_DISC_FAR, 130),
	MALL(() -> SoundEvents.MUSIC_DISC_MALL, 115),
	MELLOHI(() -> SoundEvents.MUSIC_DISC_MELLOHI, 91),
	PIGSTEP(() -> SoundEvents.MUSIC_DISC_PIGSTEP, 113),
	RETRO_BATTLE_THEME(() -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, 0), //TODO Find bpm for retro battle theme
	STAL(() -> SoundEvents.MUSIC_DISC_STAL, 105),
	STRAD(() -> SoundEvents.MUSIC_DISC_STRAD, 94),
	WAIT(() -> SoundEvents.MUSIC_DISC_WAIT, 114),
	WARD(() -> SoundEvents.MUSIC_DISC_WARD, 107);
	
	private final Supplier<SoundEvent> soundEvent;
	private final float bpm;
	
	EnumCassetteType(Supplier<SoundEvent> soundEvent, int bpm)
	{
		this.soundEvent = soundEvent;
		this.bpm = bpm;
	}
	
	@Override
	public String getSerializedName()
	{
		return name().toLowerCase();
	}
	
	public SoundEvent getSoundEvent()
	{
		return this.soundEvent.get();
	}
	
	public float getAttackSpeed()
	{
		return (this.bpm /60) * -1.5f + 1;
	}
}
