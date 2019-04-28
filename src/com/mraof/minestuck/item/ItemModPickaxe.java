package com.mraof.minestuck.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemPickaxe;

public class ItemModPickaxe extends ItemPickaxe
{
	public ItemModPickaxe(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}
}