package com.mraof.minestuck.inventory.slot;

import com.mraof.minestuck.api.uranium.UraniumPower;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class UraniumPowerSlot extends SlotItemHandler
{
	public UraniumPowerSlot(IItemHandler inventory, int index, int xPosition, int yPosition)
	{
		super(inventory, index, xPosition, yPosition);
	}
	
	@Override
	public boolean mayPlace(ItemStack itemStack)
	{
		return UraniumPower.hasUraniumPower(itemStack);
	}
}
