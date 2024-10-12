package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public record PosterComponent(TagKey<PaintingVariant> variantPool, ResourceLocation backSprite)
{
	public static final ResourceLocation DEFAULT_BACK_SPRITE = Minestuck.id("textures/painting/back/poster.png");
	
	private static final Codec<TagKey<PaintingVariant>> VARIANT_CODEC = TagKey.codec(Registries.PAINTING_VARIANT);
	private static final Codec<PosterComponent> FULL_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			VARIANT_CODEC.fieldOf("pool").forGetter(PosterComponent::variantPool),
			ResourceLocation.CODEC.fieldOf("back_sprite").orElse(DEFAULT_BACK_SPRITE).forGetter(PosterComponent::backSprite)
	).apply(instance, PosterComponent::new));
	
	public static final Codec<PosterComponent> CODEC = Codec.withAlternative(FULL_CODEC, VARIANT_CODEC, PosterComponent::withDefaultBack);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, PosterComponent> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			poster -> poster.variantPool.location(),
			ResourceLocation.STREAM_CODEC,
			PosterComponent::backSprite,
			(poolLoc, backSprite) -> new PosterComponent(TagKey.create(Registries.PAINTING_VARIANT, poolLoc), backSprite)
	);
	
	public static PosterComponent withDefaultBack(TagKey<PaintingVariant> variantPool)
	{
		return new PosterComponent(variantPool, DEFAULT_BACK_SPRITE);
	}
}
