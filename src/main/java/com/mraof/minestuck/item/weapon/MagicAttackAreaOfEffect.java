package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MagicAttackAreaOfEffect extends MagicAttackRightClickEffect
{
	
	protected MagicAttackAreaOfEffect(int distance, int damage, Supplier<MobEffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.Type type)
	{
		super(distance, damage, effect, sound, pitch, type);
	}
}
