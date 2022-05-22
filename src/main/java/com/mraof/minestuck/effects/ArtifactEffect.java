package com.mraof.minestuck.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/**
 * Just an effect that makes the player look like they've been through bad jpeg compression
 */
public class ArtifactEffect extends Effect
{
	public ArtifactEffect()
	{
		super(EffectType.NEUTRAL, 0xbf2413);
	}
}
