package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public record Rung(int rung, int backgroundColor, int textColor, long expRequirement, long boondollars, long gristCapacity, Optional<RungCondition> rungCondition)
{
	public static final Codec<Rung> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("rung").forGetter(Rung::rung),
			Codec.INT.fieldOf("background_color").forGetter(Rung::backgroundColor),
			Codec.INT.fieldOf("text_color").forGetter(Rung::textColor),
			Codec.LONG.fieldOf("exp_requirement").forGetter(Rung::expRequirement),
			Codec.LONG.fieldOf("boondollars").forGetter(Rung::boondollars),
			Codec.LONG.fieldOf("grist_capacity").forGetter(Rung::gristCapacity),
			RungCondition.CODEC.optionalFieldOf("rungCondition").forGetter(Rung::rungCondition)
	).apply(instance, Rung::new));
	public static final Codec<List<Rung>> LIST_CODEC = Codec.list(CODEC);
	
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
				Codec.DOUBLE.fieldOf("change_per_rung").forGetter(EcheladderAttribute::rungMultiplier),
				Codec.FLOAT.fieldOf("value").forGetter(EcheladderAttribute::startingValue),
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
	
	public record RungCondition(Condition condition, String description)
	{
		public static final Codec<RungCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Condition.PlayerOnlyCondition.CODEC.fieldOf("condition").forGetter(RungCondition::condition),
				Codec.STRING.fieldOf("description").forGetter(RungCondition::description)
		).apply(instance, RungCondition::new));
		
		public Component translatableDescription()
		{
			return Component.translatable(description);
		}
		
		public boolean canInitiateRung(ServerPlayer player)
		{
			if(condition instanceof Condition.PlayerOnlyCondition playerOnlyCondition)
				return playerOnlyCondition.test(player);
			
			return false;
		}
	}
	
	public record DisplayData(int backgroundColor, int textColor, long gristCapacity, DisplayAttributes attributes)
	{
		public static final StreamCodec<FriendlyByteBuf, DisplayData> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, DisplayData::backgroundColor,
				ByteBufCodecs.INT, DisplayData::textColor,
				ByteBufCodecs.VAR_LONG, DisplayData::gristCapacity,
				DisplayAttributes.STREAM_CODEC, DisplayData::attributes,
				DisplayData::new);
	}
	
	public record DisplayAttributes(double attackBonus, double healthBoost,
									double underlingDamageMod, double underlingProtectionMod)
	{
		public static final StreamCodec<FriendlyByteBuf, DisplayAttributes> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.DOUBLE, DisplayAttributes::attackBonus,
				ByteBufCodecs.DOUBLE, DisplayAttributes::healthBoost,
				ByteBufCodecs.DOUBLE, DisplayAttributes::underlingDamageMod,
				ByteBufCodecs.DOUBLE, DisplayAttributes::underlingProtectionMod,
				DisplayAttributes::new);
	}
}
