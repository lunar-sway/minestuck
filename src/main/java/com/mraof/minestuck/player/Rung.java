package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public record Rung(int rung, int backgroundColor, int textColor, long expRequirement, long boondollars,
				   long gristCapacity, List<AspectEffect> aspectEffects, List<EcheladderAttribute> attributes)
{
	public static final Codec<Rung> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("rung").forGetter(Rung::rung),
			Codec.INT.fieldOf("background_color").forGetter(Rung::backgroundColor),
			Codec.INT.fieldOf("text_color").forGetter(Rung::textColor),
			Codec.LONG.fieldOf("exp_requirement").forGetter(Rung::expRequirement),
			Codec.LONG.fieldOf("boondollars").forGetter(Rung::boondollars),
			Codec.LONG.fieldOf("grist_capacity").forGetter(Rung::gristCapacity),
			AspectEffect.LIST_CODEC.optionalFieldOf("aspect_effects", List.of()).forGetter(Rung::aspectEffects),
			EcheladderAttribute.LIST_CODEC.optionalFieldOf("attributes", List.of()).forGetter(Rung::attributes)
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
	
	public record Effect(Holder<MobEffect> mobEffect, int amplifier)
	{
		public static final MapCodec<Effect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				MobEffect.CODEC.fieldOf("effect").forGetter(Effect::mobEffect),
				Codec.INT.optionalFieldOf("amplifier", 0).forGetter(Effect::amplifier)
		).apply(instance, Effect::new));
	}
	
	public record EcheladderAttribute(Holder<Attribute> attribute, ResourceLocation id, double rungMultiplier,
									  float startingValue, AttributeModifier.Operation operation)
	{
		public static final MapCodec<EcheladderAttribute> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Attribute.CODEC.fieldOf("attribute").forGetter(EcheladderAttribute::attribute),
				ResourceLocation.CODEC.fieldOf("id").forGetter(EcheladderAttribute::id),
				Codec.DOUBLE.fieldOf("rung_multiplier").forGetter(EcheladderAttribute::rungMultiplier),
				Codec.FLOAT.fieldOf("starting_value").forGetter(EcheladderAttribute::startingValue),
				AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(EcheladderAttribute::operation)
		).apply(instance, EcheladderAttribute::new));
		
		public static final Codec<List<EcheladderAttribute>> LIST_CODEC = CODEC.codec().listOf();
		
		public double getAmount(int rung)
		{
			return (rungMultiplier * rung) + startingValue;
		}
		
		public void updateAttribute(ServerPlayer player, int rung)
		{
			AttributeInstance attributeInstance = player.getAttribute(attribute);
			AttributeModifier attributeModifier = new AttributeModifier(id, getAmount(rung), operation);
			
			if(attributeInstance.hasModifier(attributeModifier.id()))
				attributeInstance.removeModifier(attributeModifier.id());
			attributeInstance.addPermanentModifier(attributeModifier);
		}
	}
}
