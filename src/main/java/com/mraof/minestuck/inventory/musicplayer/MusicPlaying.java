package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * The music playing capability keeps track of
 * the music cassette currently played by the player that the capability is attached to,
 * as well as which music player item stack that is being used to play it.
 *
 * @see MusicPlayerWeapon
 */
public final class MusicPlaying
{
	@Nullable
	private CassetteSong cassetteType = null;
	private ItemStack currentCassettePlayer = ItemStack.EMPTY;
	
	public void setMusicPlaying(ItemStack cassettePlayer, @Nullable CassetteSong cassetteType)
	{
		this.currentCassettePlayer = cassettePlayer;
		this.cassetteType = cassetteType;
	}
	
	@Nullable
	public CassetteSong getCassetteSong()
	{
		return cassetteType;
	}
	
	public ItemStack getCurrentMusicPlayer()
	{
		return currentCassettePlayer;
	}
}
