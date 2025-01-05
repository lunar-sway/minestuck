package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import net.minecraft.world.item.ItemStack;

/**
 * The music playing capability keeps track of
 * the music cassette currently played by the player that the capability is attached to,
 * as well as which music player item stack that is being used to play it.
 *
 * @see MusicPlayerWeapon
 */
public final class MusicPlaying
{
	private EnumCassetteType cassetteType = EnumCassetteType.NONE;
	private ItemStack currentCassettePlayer = ItemStack.EMPTY;
	
	public void setMusicPlaying(ItemStack cassettePlayer, EnumCassetteType cassetteType)
	{
		this.currentCassettePlayer = cassettePlayer;
		this.cassetteType = cassetteType;
	}
	
	public EnumCassetteType getCassetteType()
	{
		return cassetteType;
	}
	
	public ItemStack getCurrentMusicPlayer()
	{
		return currentCassettePlayer;
	}
}
