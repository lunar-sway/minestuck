package com.mraof.minestuck.item.weapon;

import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public interface ItemUseEffect
{
	ActionResultType onItemUse(ItemUseContext context);
	
	
}