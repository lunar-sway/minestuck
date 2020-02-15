package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.inventory.IInventory;

public interface ItemCombiner extends IInventory
{
	CombinationRegistry.Mode getMode();
	
	
}