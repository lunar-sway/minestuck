package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.sounds.SoundEvent;

public interface IMusicPlaying {
	void setCurrentMusic(SoundEvent music, EnumCassetteType cassetteType);
	
	SoundEvent getCurrentMusic();
	
	EnumCassetteType getCassetteType();
}
