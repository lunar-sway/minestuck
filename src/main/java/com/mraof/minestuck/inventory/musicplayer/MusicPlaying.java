package com.mraof.minestuck.inventory.musicplayer;

import net.minecraft.sounds.SoundEvent;

public class MusicPlaying implements IMusicPlaying {
	
	private SoundEvent currentMusic;
	
	@Override
	public void setCurrentMusic(SoundEvent music)
	{
		currentMusic = music;
	}
	
	@Override
	public SoundEvent getCurrentMusic()
	{
		return currentMusic;
	}
}
