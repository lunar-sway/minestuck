package com.mraof.minestuck.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MusicPlayerContainer extends AbstractContainerMenu
{
	private static final int CASSETTE_X = 26;
	private static final int CASSETTE_Y = 25;
	
	public MusicPlayerContainer(int windowId, Inventory playerInventory)
	{
		super(MSContainerTypes.MUSIC_PLAYER, windowId);
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return true;
	}
}
