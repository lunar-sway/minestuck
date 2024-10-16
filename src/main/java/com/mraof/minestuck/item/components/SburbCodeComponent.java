package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SburbCodeComponent(boolean paradoxCode, List<ResourceLocation> hieroglyphs)
{
	public static final Codec<List<ResourceLocation>> HIEROGLYPHS_CODEC = Codec.list(ResourceLocation.CODEC);
	public static final Codec<SburbCodeComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.BOOL.fieldOf("hasParadoxInfo").forGetter(SburbCodeComponent::paradoxCode),
					HIEROGLYPHS_CODEC.fieldOf("recordedHieroglyphs").forGetter(SburbCodeComponent::hieroglyphs)
			).apply(instance, SburbCodeComponent::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, SburbCodeComponent> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			SburbCodeComponent::paradoxCode,
			ByteBufCodecs.fromCodec(HIEROGLYPHS_CODEC),
			SburbCodeComponent::hieroglyphs,
			SburbCodeComponent::new
	);
	
	public static final SburbCodeComponent DEFAULT_COMPONENT = new SburbCodeComponent(true, List.of());
}
