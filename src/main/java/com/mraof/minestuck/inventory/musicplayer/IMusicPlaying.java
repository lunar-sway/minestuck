package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface IMusicPlaying {
	void setCassetteType(EnumCassetteType cassetteType);
	
	void setCurrentMusicPlayer(ItemStack itemStack);
	
	EnumCassetteType getCassetteType();
	
	ItemStack getCurrentMusicPlayer();
}
