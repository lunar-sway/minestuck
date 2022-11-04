package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface IMusicPlaying
{
	void setMusicPlaying(ItemStack cassettePlayer, EnumCassetteType cassetteType);
	
	EnumCassetteType getCassetteType();
	
	ItemStack getCurrentMusicPlayer();
}
