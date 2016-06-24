package com.mraof.minestuck.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotInput extends Slot
{
	
	public Item item;
	
	public SlotInput(IInventory inventory, int index, int xPosition, int yPosition, Item item)
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