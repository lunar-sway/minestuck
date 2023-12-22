package com.mraof.minestuck.item.foods;

import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoporSlimeItem extends Item
{
	private final int duration;
	
	public SoporSlimeItem(Properties pProperties, int duration)
	{
		super(pProperties);
		this.duration = duration;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving)
	{
		if(entityLiving.hasEffect(MSEffects.SOPOR_SICKNESS.get()))
		{
			MobEffectInstance oldSoporEffect = entityLiving.getEffect(MSEffects.SOPOR_SICKNESS.get());
			int newAmplifier = oldSoporEffect != null ? oldSoporEffect.getAmplifier() + 1 : 0; //amplification stacks with several pies
			entityLiving.addEffect(new MobEffectInstance(MSEffects.SOPOR_SICKNESS.get(), duration, newAmplifier));
		} else
			entityLiving.addEffect(new MobEffectInstance(MSEffects.SOPOR_SICKNESS.get(), duration, 0));
		
		return super.finishUsingItem(stack, level, entityLiving);
	}
}