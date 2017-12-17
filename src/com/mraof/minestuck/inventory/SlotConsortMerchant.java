package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotConsortMerchant extends Slot
{
	public SlotConsortMerchant(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public ItemStack decrStackSize(int amount)
	{
		return ItemStack.EMPTY;
	}
}