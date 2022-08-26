package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.sounds.SoundEvent;

public class MusicPlaying implements IMusicPlaying {
	
	private SoundEvent currentMusic;
	private EnumCassetteType cassetteType;
	
	@Override
	public void setCurrentMusic(SoundEvent music, EnumCassetteType cassetteType)
	{
		currentMusic = music;
		this.cassetteType = cassetteType;
	}
	
	@Override
	public SoundEvent getCurrentMusic()
	{
		return currentMusic;
	}
	
	@Override
	public EnumCassetteType getCassetteType()
	{
		return cassetteType;
	}
}
