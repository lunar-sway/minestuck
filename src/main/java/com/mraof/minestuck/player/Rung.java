package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.List;

public record Rung(int rung, long expRequirement, long boondollars, long gristCapacity, List<AspectEffect> aspectEffects)
{
	//TODO add the rung names
	//TODO consider including attributes
	public static final Codec<Rung> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("rung").forGetter(Rung::rung),
			Codec.LONG.fieldOf("exp_requirement").forGetter(Rung::expRequirement),
			Codec.LONG.fieldOf("boondollars").forGetter(Rung::boondollars),
			Codec.LONG.fieldOf("grist_capacity").forGetter(Rung::gristCapacity),
			AspectEffect.LIST_CODEC.fieldOf("aspect_effects").forGetter(Rung::aspectEffects)
	).apply(instance, Rung::new));
	public static final Codec<List<Rung>> LIST_CODEC = Codec.list(CODEC);
	
	public record AspectEffect(EnumAspect aspect, Holder<MobEffect> effect, int amplifier)
	{
		public static final Codec<AspectEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EnumAspect.CODEC.fieldOf("aspect").forGetter(AspectEffect::aspect),
				MobEffect.CODEC.fieldOf("effect").forGetter(AspectEffect::effect),
				Codec.INT.fieldOf("amplifier").forGetter(AspectEffect::amplifier)
		).apply(instance, AspectEffect::new));
		
		public static final Codec<List<AspectEffect>> LIST_CODEC = CODEC.listOf();
	}
}
