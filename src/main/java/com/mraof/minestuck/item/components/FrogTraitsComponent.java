package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record FrogTraitsComponent(Optional<FrogEntity.FrogVariants> variant, Optional<Double> size, Optional<Integer> skinColor,
								  Optional<FrogEntity.BellyTypes> bellyType, Optional<Integer> bellyColor, Optional<FrogEntity.EyeTypes> eyeType, Optional<Integer> eyeColor)
{
	public static final FrogTraitsComponent RANDOM = new FrogTraitsComponent(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	
	public static final Codec<FrogTraitsComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					enumCodec(FrogEntity.FrogVariants.class).optionalFieldOf("variant").forGetter(FrogTraitsComponent::variant),
					Codec.DOUBLE.optionalFieldOf("size").forGetter(FrogTraitsComponent::size),
					Codec.INT.optionalFieldOf("skin_color").forGetter(FrogTraitsComponent::skinColor),
					enumCodec(FrogEntity.BellyTypes.class).optionalFieldOf("belly_type").forGetter(FrogTraitsComponent::bellyType),
					Codec.INT.optionalFieldOf("belly_color").forGetter(FrogTraitsComponent::bellyColor),
					enumCodec(FrogEntity.EyeTypes.class).optionalFieldOf("eye_type").forGetter(FrogTraitsComponent::eyeType),
					Codec.INT.optionalFieldOf("eye_color").forGetter(FrogTraitsComponent::eyeColor)
					
			).apply(instance, FrogTraitsComponent::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FrogTraitsComponent> STREAM_CODEC = StreamCodec.of( //StreamCodec.composite has a limit of 6 args
			(encode, component) ->
			{
				ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(FrogEntity.FrogVariants.class)).encode(encode, component.variant);
				ByteBufCodecs.optional(ByteBufCodecs.DOUBLE).encode(encode, component.size);
				ByteBufCodecs.optional(ByteBufCodecs.INT).encode(encode, component.skinColor);
				ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(FrogEntity.BellyTypes.class)).encode(encode, component.bellyType);
				ByteBufCodecs.optional(ByteBufCodecs.INT).encode(encode, component.bellyColor);
				ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(FrogEntity.EyeTypes.class)).encode(encode, component.eyeType);
				ByteBufCodecs.optional(ByteBufCodecs.INT).encode(encode, component.eyeColor);
			},
			(decode) ->
					new FrogTraitsComponent(
							ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(FrogEntity.FrogVariants.class)).decode(decode),
							ByteBufCodecs.optional(ByteBufCodecs.DOUBLE).decode(decode),
							ByteBufCodecs.optional(ByteBufCodecs.INT).decode(decode),
							ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(FrogEntity.BellyTypes.class)).decode(decode),
							ByteBufCodecs.optional(ByteBufCodecs.INT).decode(decode),
							ByteBufCodecs.optional(NeoForgeStreamCodecs.enumCodec(FrogEntity.EyeTypes.class)).decode(decode),
							ByteBufCodecs.optional(ByteBufCodecs.INT).decode(decode)
					)
	);
	
	public static FrogTraitsComponent fromFrogEntity(FrogEntity frog)
	{
		return new FrogTraitsComponent(Optional.of(frog.getFrogVariant()), Optional.of(frog.getFrogSize()), Optional.of(frog.getSkinColor()),
				Optional.ofNullable(frog.getBellyType()), Optional.of(frog.getBellyColor()),
				Optional.ofNullable(frog.getEyeType()), Optional.of(frog.getEyeColor()));
	}
	
	public FrogEntity apply(FrogEntity frog)
	{
		frog.setFrogVariant(variant.orElse(frog.getRandomFrogVariant()));
		frog.setFrogSize(size.orElse(frog.getRandom().nextDouble() + 0.6), true);
		frog.setSkinColor(skinColor.orElse(frog.random(0xFFFFFF)));
		frog.setBellyType(bellyType.orElse(randomFromEnum(frog.getRandom(), FrogEntity.BellyTypes.class)));
		frog.setBellyColor(bellyColor.orElse(frog.random(0xFFFFFF)));
		frog.setEyeType(eyeType.orElse(randomFromEnum(frog.getRandom(), FrogEntity.EyeTypes.class)));
		frog.setEyeColor(eyeColor.orElse(frog.random(0xFFFFFF)));
		
		return frog;
	}
	
	public static <T extends Enum<T>> T randomFromEnum(RandomSource randomSource, Class<T> enumClass)
	{
		return enumClass.getEnumConstants()[randomSource.nextInt(enumClass.getEnumConstants().length)];
	}
	
	public static <T extends Enum<T> & StringRepresentable> Codec<T> enumCodec(Class<T> enumClass)
	{
		return StringRepresentable.fromValues(enumClass::getEnumConstants);
	}
}
