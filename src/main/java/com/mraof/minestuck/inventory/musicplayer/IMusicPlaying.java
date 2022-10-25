package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.sounds.SoundEvent;

public interface IMusicPlaying {
	void setCassetteType(EnumCassetteType cassetteType);
	
	EnumCassetteType getCassetteType();
}
