package com.mraof.minestuck.inventory.musicplayer;

import net.minecraft.sounds.SoundEvent;

public interface IMusicPlaying {
	void setCurrentMusic(SoundEvent music);
	
	SoundEvent getCurrentMusic();
}
