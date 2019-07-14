package com.mraof.minestuck.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemPickaxe;

public class ModPickaxeItem extends ItemPickaxe
{
	public ModPickaxeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}
}