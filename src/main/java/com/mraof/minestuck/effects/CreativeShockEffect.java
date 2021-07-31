package com.mraof.minestuck.effects;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 */
public class CreativeShockEffect extends Effect
{
	protected CreativeShockEffect()
	{
		super(EffectType.NEUTRAL, 0x47453d);
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
	
	@Override
	public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, int x, int y, float z)
	{
		super.renderInventoryEffect(effect, gui, x, y, z);
	}
}
