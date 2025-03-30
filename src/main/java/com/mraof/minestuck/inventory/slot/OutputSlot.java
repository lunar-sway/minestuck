package com.mraof.minestuck.inventory.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class OutputSlot extends SlotItemHandler
{
	
	public OutputSlot(IItemHandler inventory, int index, int xPosition, int yPosition)
	{
		super(inventory, index, xPosition, yPosition);
	}
	
	@Override
	public boolean mayPlace(ItemStack itemStack)
	{
		return false;
	}
}