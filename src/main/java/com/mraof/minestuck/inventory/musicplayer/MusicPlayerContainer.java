package com.mraof.minestuck.inventory.musicplayer;

import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class MusicPlayerContainer extends AbstractContainerMenu
{
	private static final int CASSETTE_X = 80;
	private static final int CASSETTE_Y = 36;
	
	public MusicPlayerContainer(int windowId, Inventory playerInventory, IItemHandler itemStackHandler)
	{
		super(MSMenuTypes.MUSIC_PLAYER.get(), windowId);
		addSlot(new SlotItemHandler(itemStackHandler, 0, CASSETTE_X, CASSETTE_Y));
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	public MusicPlayerContainer(int windowId, Inventory playerInventory)
	{
		this(windowId, playerInventory, new ItemStackHandlerMusicPlayer(1));
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return true;
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		
		if (slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem();
			itemstack = itemstackOrig.copy();
			boolean result = false;
				if(itemstackOrig.is(MSTags.Items.CASSETTES))
				{
					//Debug.print("Transferring...");
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
				}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
		}
		
		return itemstack;
	}
}
