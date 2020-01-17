package com.mraof.minestuck.item;

import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;

public class ModAxeItem extends AxeItem
{
	public ModAxeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}
}