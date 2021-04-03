package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Supplier;

public interface InventoryTickEffect
{
	void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected);
	
	InventoryTickEffect BREATH_SLOW_FALLING = passiveAspectEffect(EnumAspect.BREATH, () -> new EffectInstance(Effects.SLOW_FALLING, 2, 2));
	
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
	
	InventoryTickEffect TEST_DISABLE = (stack, worldIn, entityIn, itemSlot, isSelected) -> {
		if(entityIn instanceof PlayerEntity)
		{
			PlayerEntity playerEntity = (PlayerEntity) entityIn;
			if(playerEntity.isActiveItemStackBlocking())
			{
				Debug.debugf("Active stack is blocking");
				
				if(stack.canDisableShield(playerEntity.getActiveItemStack(), null, playerEntity))
				{
					Debug.debugf("canDisableShield");
					playerEntity.disableShield(true);
				}
			}
		}
	};
	
	//TODO divide into useful components similarly to in OnHitEffect, such as requireAspect(aspect, tickEffect), whenSelected(tickEffect), potionEffect(effect)
	static InventoryTickEffect passiveAspectEffect(EnumAspect aspect, Supplier<EffectInstance> effect)
	{
		return (stack, worldIn, entityIn, itemSlot, isSelected) -> {
			if(isSelected && entityIn instanceof ServerPlayerEntity)
			{
				Title title = PlayerSavedData.getData((ServerPlayerEntity) entityIn).getTitle();
				if(title != null && title.getHeroAspect() == aspect)
					((ServerPlayerEntity) entityIn).addPotionEffect(effect.get());
			}
		};
	}
}