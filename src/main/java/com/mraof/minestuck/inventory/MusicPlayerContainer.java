package com.mraof.minestuck.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MusicPlayerContainer extends AbstractContainerMenu
{
	public MusicPlayerContainer(int windowId, Inventory playerInventory)
	{
		super(MSContainerTypes.MUSIC_PLAYER, windowId);
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return true;
	}
}
