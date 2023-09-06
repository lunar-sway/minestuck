package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoporSicknessEffect extends MobEffect
{
	protected SoporSicknessEffect()
	{
		super(MobEffectCategory.NEUTRAL, 0x47453d);
	}
	
	@Override
	public void applyEffectTick(LivingEntity target, int effectLevel)
	{
		if (target.level().isClientSide())
		{
			if(target.getHealth() > 5.0F)
			{
				target.hurt(target.damageSources().generic(), 0.05F);
			}
		}
		super.applyEffectTick(target, effectLevel);
	}
	
	public static boolean canAcquireAffect(MobEffectInstance effectInstance, LivingEntity target)
	{
		MobEffectEvent.Applicable event = new MobEffectEvent.Applicable(target, effectInstance);
		if (event.getResult() != Event.Result.DEFAULT) return event.getResult() == Event.Result.ALLOW;
		
		MobEffectCategory mobEffect = effectInstance.getEffect().getCategory();
		return mobEffect != MobEffectCategory.HARMFUL && mobEffect != MobEffectCategory.BENEFICIAL;
	}
	
	@SubscribeEvent
	public static void effectApplicabilityEvent(MobEffectEvent.Applicable event)
	{
		if(event.getEntity().hasEffect(MSEffects.SOPOR_SICKNESS.get()))
		{
			SoporSicknessEffect.canAcquireAffect(event.getEffectInstance(), event.getEntity());
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier)
	{
		return true;
	}
}
