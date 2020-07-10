package com.mraof.minestuck.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.PickaxeItem;

public class ModPickaxeItem extends PickaxeItem
{
	public ModPickaxeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}
}