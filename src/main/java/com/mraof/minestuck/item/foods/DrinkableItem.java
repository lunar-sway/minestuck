package com.mraof.minestuck.item.foods;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class DrinkableItem extends Item
{
    public DrinkableItem(Properties properties)
	{
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
	{
        return UseAnim.DRINK;
    }
}
