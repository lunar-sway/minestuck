package com.mraof.minestuck.item;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemBeverage extends ItemFood
{
	public ItemBeverage(int healAmount, float saturationModifier, boolean meat, Properties builder)
	{
		super(healAmount, saturationModifier, meat, builder);
	}
	
	@Override
	public EnumAction getUseAction(ItemStack stack)
	{
        return EnumAction.DRINK;
    }
}