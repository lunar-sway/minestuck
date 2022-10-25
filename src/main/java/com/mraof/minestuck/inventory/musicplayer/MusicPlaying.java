package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.sounds.SoundEvent;

public class MusicPlaying implements IMusicPlaying {
	private EnumCassetteType cassetteType = EnumCassetteType.NONE;
	
	@Override
	public void setCassetteType(EnumCassetteType cassetteType)
	{
		this.cassetteType = cassetteType;
	}
	
	@Override
	public EnumCassetteType getCassetteType()
	{
		return cassetteType;
	}
}
