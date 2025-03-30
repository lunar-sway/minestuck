package com.mraof.minestuck.inventory.slot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

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