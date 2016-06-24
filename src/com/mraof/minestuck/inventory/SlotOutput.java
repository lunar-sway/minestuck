package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot
{
	
	public SlotOutput(IInventory inventory, int index, int xPosition, int yPosition)
	{
		super(inventory, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return false;
	}
}