package com.mraof.minestuck.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/**
* This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 */
public class EffectBuildInhibit extends Effect
{
	public int liquidColor;
	
	protected EffectBuildInhibit(EffectType effectType, int liquidColorIn)
	{
		//super(EffectType.NEUTRAL, 0x47453d, "creative_shock");
		super(effectType, liquidColorIn);
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
	{
		super.performEffect(entityLivingBaseIn, amplifier);
		
		if(!(entityLivingBaseIn instanceof PlayerEntity))
			return;
		
		PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
		
		if(!player.isCreative())
		{
			player.abilities.allowEdit = false;
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		super.isReady(duration, amplifier);
		return (duration % 5) == 0;
	}
	
	/*@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if(!(entityLivingBaseIn instanceof PlayerEntity))
			return;
		
		PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
		
		if(!player.isCreative())
		{
			player.capabilities.allowEdit = false;
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return (duration % 5) == 0;
	}*/
}
