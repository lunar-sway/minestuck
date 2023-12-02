package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
		MobEffect effect = event.getEffectInstance().getEffect();
		
		if(ForgeRegistries.MOB_EFFECTS.tags() == null)
			return;
		
		boolean inWhitelist = ForgeRegistries.MOB_EFFECTS.tags().getTag(MSTags.Effects.SOPOR_SICKNESS_WHITELIST).contains(effect);
		
		//effect will be cancelled if the entity has Sopor Sickness and the effect is in the whitelist tag
		if(entity.hasEffect(MSEffects.SOPOR_SICKNESS.get()) && inWhitelist)
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
