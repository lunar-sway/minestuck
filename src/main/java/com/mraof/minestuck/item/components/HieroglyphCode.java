package com.mraof.minestuck.item.components;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public record HieroglyphCode(Set<Block> hieroglyphs)
{
	public static final Codec<HieroglyphCode> CODEC = BuiltInRegistries.BLOCK.byNameCodec().listOf()
			.xmap(Set::copyOf, List::copyOf).xmap(HieroglyphCode::new, HieroglyphCode::hieroglyphs);
	public static final StreamCodec<RegistryFriendlyByteBuf, HieroglyphCode> STREAM_CODEC = ByteBufCodecs.registry(Registries.BLOCK).apply(ByteBufCodecs.list())
			.map(Set::copyOf, List::copyOf).map(HieroglyphCode::new, HieroglyphCode::hieroglyphs);
	
	public static final HieroglyphCode EMPTY = new HieroglyphCode(Collections.emptySet());
	
	public static Set<Block> getBlocks(ItemStack stack)
	{
		return stack.getOrDefault(MSItemComponents.HIEROGLYPH_CODE, EMPTY).hieroglyphs();
	}
	
	public static void setBlocks(ItemStack stack, Set<Block> hieroglyphs)
	{
		stack.set(MSItemComponents.HIEROGLYPH_CODE, new HieroglyphCode(Set.copyOf(hieroglyphs)));
	}
	
	public static boolean addBlock(ItemStack stack, Block hieroglyph)
	{
		HieroglyphCode existingCode = stack.getOrDefault(MSItemComponents.HIEROGLYPH_CODE, EMPTY);
		if(existingCode.hieroglyphs().contains(hieroglyph))
			return false;
		
		ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
		builder.add(hieroglyph);
		builder.addAll(existingCode.hieroglyphs());
		stack.set(MSItemComponents.HIEROGLYPH_CODE, new HieroglyphCode(builder.build()));
		return true;
	}
}
