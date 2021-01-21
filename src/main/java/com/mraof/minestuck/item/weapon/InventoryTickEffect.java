package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface InventoryTickEffect
{
	void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected);
	
	InventoryTickEffect DROP_WHEN_IN_WATER = (stack, worldIn, entityIn, itemSlot, isSelected) -> {
		if(isSelected && entityIn.isInWater() && entityIn instanceof LivingEntity)
		{
			stack.damageItem(70, ((LivingEntity) entityIn), entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
			ItemEntity weapon = new ItemEntity(entityIn.world, entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), stack.copy());
			weapon.getItem().setCount(1);
			weapon.setPickupDelay(40);
			entityIn.world.addEntity(weapon);
			stack.shrink(1);
			
			entityIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5);
		}
	};
}