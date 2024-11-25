package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockTypes;

import java.util.List;

public record SburbCodeComponent(boolean paradoxCode, List<Block> hieroglyphs)
{
	public static final Codec<List<Block>> HIEROGLYPHS_CODEC = Codec.list(BuiltInRegistries.BLOCK.byNameCodec());
	public static final Codec<SburbCodeComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.BOOL.fieldOf("has_paradox_info").forGetter(SburbCodeComponent::paradoxCode),
					HIEROGLYPHS_CODEC.fieldOf("recorded_hieroglyphs").forGetter(SburbCodeComponent::hieroglyphs)
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
