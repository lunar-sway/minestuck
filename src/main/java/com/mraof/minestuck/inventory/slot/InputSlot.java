package com.mraof.minestuck.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InputSlot extends Slot
{
	
	public Item item;
	
	public InputSlot(IInventory inventory, int index, int xPosition, int yPosition, Item item)
	{
		super(inventory, index, xPosition, yPosition);
		this.item = item;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return (itemStack.getItem() == item);
	}
	
}