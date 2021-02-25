package com.mraof.minestuck.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.ParametersAreNullableByDefault;

public class TimeStopEffect extends Effect
{
	protected TimeStopEffect(EffectType effectType, int liquidColorIn) {
		super(effectType, liquidColorIn);
	}
	
	@Override
	@ParametersAreNullableByDefault
	public void performEffect(LivingEntity livingEntityIn, int amplifier)
	{
		super.performEffect(livingEntityIn, amplifier);
		
		livingEntityIn.setMotion(0,0,0);
		//entityLivingBaseIn.motionX = 0;
		//entityLivingBaseIn.motionY = 0;
		//entityLivingBaseIn.motionZ = 0;
		livingEntityIn.moveForward = 0;
		livingEntityIn.moveStrafing = 0;
		livingEntityIn.moveVertical = 0;
		
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
}
