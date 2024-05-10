package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public interface InventoryTickEffect
{
	void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected);
	
	InventoryTickEffect BREATH_SLOW_FALLING = passiveAspectEffect(EnumAspect.BREATH, () -> new MobEffectInstance(MobEffects.SLOW_FALLING, 2, 2));
	
	InventoryTickEffect DROP_WHEN_IN_WATER = (stack, level, entityIn, itemSlot, isSelected) -> {
		if(isSelected && entityIn.isInWater() && entityIn instanceof LivingEntity living)
		{
			stack.hurtAndBreak(70, living, entity -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			ItemEntity weapon = new ItemEntity(entityIn.level(), entityIn.getX(), entityIn.getY(), entityIn.getZ(), stack.copy());
			weapon.getItem().setCount(1);
			weapon.setPickUpDelay(40);
			entityIn.level().addFreshEntity(weapon);
			stack.shrink(1);
			
			entityIn.hurt(level.damageSources().lightningBolt(), 5);
		}
	};
	
	//TODO divide into useful components similarly to in OnHitEffect, such as requireAspect(aspect, tickEffect), whenSelected(tickEffect), potionEffect(effect)
	static InventoryTickEffect passiveAspectEffect(EnumAspect aspect, Supplier<MobEffectInstance> effect)
	{
		return (stack, worldIn, entityIn, itemSlot, isSelected) -> {
			if(isSelected && entityIn instanceof ServerPlayer player
					&& Title.isPlayerOfAspect(player, aspect))
			{
				player.addEffect(effect.get());
			}
		};
	}
}