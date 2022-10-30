package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.block.EnumCassetteType;
import net.minecraft.world.item.ItemStack;

public class MusicPlaying implements IMusicPlaying
{
	private EnumCassetteType cassetteType = EnumCassetteType.NONE;
	private ItemStack currentCassettePlayer = ItemStack.EMPTY;
	
	@Override
	public void setCassetteType(EnumCassetteType cassetteType)
	{
		this.cassetteType = cassetteType;
	}
	
	@Override
	public void setCurrentMusicPlayer(ItemStack itemStack)
	{
		this.currentCassettePlayer = itemStack;
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
