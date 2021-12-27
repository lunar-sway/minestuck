package com.mraof.minestuck.inventory.slot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class InputSlot extends SlotItemHandler
{
	
	public Item item;
	
	public InputSlot(IItemHandler inventory, int index, int xPosition, int yPosition, Item item)
	{
		super(inventory, index, xPosition, yPosition);
		this.item = item;
	}
	
	@Override
	public boolean mayPlace(ItemStack itemStack)
	{
		return (itemStack.getItem() == item);
	}
	
}