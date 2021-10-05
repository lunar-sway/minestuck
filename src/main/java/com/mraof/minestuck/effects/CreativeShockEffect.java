package com.mraof.minestuck.effects;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 * The different levels of amplification increase the range of the effect, allowing the selective limitation of both creative and non creative players.
 * 0 = cant directly cause block breakage/placement outside of creative, 1 = cant access redstone machinery gui outside of creative, 2 = cant use mobility items outside of creative,
 * 3 = cant directly cause block breakage/placement even in creative, 4 = cant access redstone machinery gui even in creative, 5 = cant use mobility items even in creative
 */
public class CreativeShockEffect extends Effect
{
	protected CreativeShockEffect()
	{
		super(EffectType.NEUTRAL, 0x47453d);
	}
	
	/**
	 * Checks whether player has creative shock effect and whether the amplifier is strong enough to limit.
	 * Will return true if amplifier is equal to or greater than relevant threshold.
	 */
	public static boolean doesCreativeShockLimit(PlayerEntity player, int survivalAmplifierThreshold, int creativeAmplifierThreshold)
	{
		if(player.isPotionActive(MSEffects.CREATIVE_SHOCK.get()))
		{
			if(player.isCreative())
			{
				return player.getActivePotionEffect(MSEffects.CREATIVE_SHOCK.get()).getAmplifier() >= creativeAmplifierThreshold;
			} else
			{
				return player.getActivePotionEffect(MSEffects.CREATIVE_SHOCK.get()).getAmplifier() >= survivalAmplifierThreshold;
			}
		}
		
		return false;
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
	{
		super.performEffect(entityLivingBaseIn, amplifier);
		
		if(!(entityLivingBaseIn instanceof PlayerEntity))
			return;
		
		PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
		
		if(doesCreativeShockLimit(player, 0, 3))
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
