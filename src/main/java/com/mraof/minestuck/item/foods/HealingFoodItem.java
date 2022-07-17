package com.mraof.minestuck.item.foods;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealingFoodItem extends Item
{
    private final float healAmount;
    
    public HealingFoodItem(float healAmount, Properties properties)
    {
        super(properties);
        this.healAmount = healAmount;
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving)
    {
        entityLiving.heal(healAmount);
        return super.finishUsingItem(stack, level, entityLiving);
    }
}
