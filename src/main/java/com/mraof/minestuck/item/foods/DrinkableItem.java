package com.mraof.minestuck.item.foods;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class DrinkableItem extends Item {
    public DrinkableItem(Properties properties){
        super(properties);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack){
        return UseAction.DRINK;
    }
}
