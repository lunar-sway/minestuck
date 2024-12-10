package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;

public record HieroglyphCode(List<Block> hieroglyphs)
{
	public static final Codec<HieroglyphCode> CODEC = BuiltInRegistries.BLOCK.byNameCodec().listOf()
			.xmap(HieroglyphCode::new, HieroglyphCode::hieroglyphs);
	public static final StreamCodec<RegistryFriendlyByteBuf, HieroglyphCode> STREAM_CODEC = ByteBufCodecs.registry(Registries.BLOCK).apply(ByteBufCodecs.list())
			.map(HieroglyphCode::new, HieroglyphCode::hieroglyphs);
	
	public static final HieroglyphCode EMPTY = new HieroglyphCode(Collections.emptyList());
}
