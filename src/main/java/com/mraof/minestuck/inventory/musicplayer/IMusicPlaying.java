package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.item.weapon.MusicPlayerItem;
import net.minecraft.world.item.ItemStack;

/**
 * This capability interface is meant to keep track of the current music being played,
 * as well as which music player is currently playing it.
 *
 * @see MusicPlayerItem
 */

public interface IMusicPlaying
{
	void setMusicPlaying(ItemStack cassettePlayer, EnumCassetteType cassetteType);
	
	EnumCassetteType getCassetteType();
	
	ItemStack getCurrentMusicPlayer();
}
