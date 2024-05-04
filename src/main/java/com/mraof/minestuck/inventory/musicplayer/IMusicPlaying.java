package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import net.minecraft.world.item.ItemStack;

/**
 * The music playing capability keeps track of
 * the music cassette currently played by the player that the capability is attached to,
 * as well as which music player item stack that is being used to play it.
 * This interface defines how to interact with this capability.
 *
 * @see MusicPlayerWeapon
 */
@Deprecated //todo switch to just MusicPlaying
public interface IMusicPlaying
{
	void setMusicPlaying(ItemStack cassettePlayer, EnumCassetteType cassetteType);
	
	EnumCassetteType getCassetteType();
	
	ItemStack getCurrentMusicPlayer();
}
