package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.world.item.ItemStack;

public class MusicPlaying implements IMusicPlaying
{
	private EnumCassetteType cassetteType = EnumCassetteType.NONE;
	private ItemStack currentCassettePlayer = ItemStack.EMPTY;
	
	@Override
	public void setMusicPlaying(ItemStack cassettePlayer, EnumCassetteType cassetteType)
	{
		this.currentCassettePlayer = cassettePlayer;
		this.cassetteType = cassetteType;
	}
	
	@Override
	public EnumCassetteType getCassetteType()
	{
		return cassetteType;
	}
	
	@Override
	public ItemStack getCurrentMusicPlayer()
	{
		return currentCassettePlayer;
	}
}
