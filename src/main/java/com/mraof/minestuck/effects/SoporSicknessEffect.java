package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A potion effect which negates the application of new potion effects that are added to its whitelist MobEffect tag.
 * While active it slowly injures the entity who has it. The frequency with which it damages the player depends on the amplifier value.
 * All vanilla potion effects are included in the whitelist.
 */
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
		if(target.getHealth() > 5.0F)
		{
			target.hurt(target.damageSources().generic(), 0.5F);
		}
	}
	
	@SubscribeEvent
	public static void effectApplicabilityEvent(MobEffectEvent.Applicable event)
	{
		LivingEntity entity = event.getEntity();
		MobEffectInstance effectInstance = event.getEffectInstance();
		MobEffect effect = effectInstance.getEffect();
		
		if(ForgeRegistries.MOB_EFFECTS.tags() == null)
			return;
		
		boolean inWhitelist = ForgeRegistries.MOB_EFFECTS.tags().getTag(MSTags.Effects.SOPOR_SICKNESS_WHITELIST).contains(effect);
		
		//effect will be cancelled if the entity has Sopor Sickness, the effect is in the whitelist tag, and the sopor effect amplifier matches or is greater than that of the new effect
		if(entity.hasEffect(MSEffects.SOPOR_SICKNESS.get()) && inWhitelist)
		{
			MobEffectInstance soporEffect = entity.getEffect(MSEffects.SOPOR_SICKNESS.get());
			
			if(soporEffect != null && effectInstance.getAmplifier() <= soporEffect.getAmplifier())
				event.setResult(Event.Result.DENY);
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		//starts at an interval of 76 ticks at amp 0 and maxes at an interval of 10 ticks
		int durationInterval = (int) (Math.pow((amplifier / 100D) + 0.015D, -1D) + 10);
		return (duration % durationInterval) == 0;
	}
}