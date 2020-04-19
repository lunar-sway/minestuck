package com.mraof.minestuck.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HealingFoodItem extends Item
{
    private final float healAmount;
    
    public HealingFoodItem(float healAmount, Properties properties)
    {
        super(properties);
        this.healAmount = healAmount;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        entityLiving.heal(healAmount);
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
