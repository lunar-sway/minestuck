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
		if (target.getHealth() > 5.0F)
		{
			target.hurt(target.damageSources().generic(), 0.5F);
		}
	}
	
	@SubscribeEvent
	public static void effectApplicabilityEvent(MobEffectEvent.Applicable event)
	{
		LivingEntity entity = event.getEntity();
		MobEffect sopor_sickness = MSEffects.SOPOR_SICKNESS.get();
		
		if(entity.hasEffect(sopor_sickness))
		{
			event.setResult(Event.Result.DENY);
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return (duration % 40) == 0;
	}
}
