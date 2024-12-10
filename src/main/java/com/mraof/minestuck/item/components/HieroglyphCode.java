package com.mraof.minestuck.item.components;

import com.google.common.collect.ImmutableList;
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

public record HieroglyphCode(List<Block> hieroglyphs)
{
	public static final Codec<HieroglyphCode> CODEC = BuiltInRegistries.BLOCK.byNameCodec().listOf()
			.xmap(HieroglyphCode::new, HieroglyphCode::hieroglyphs);
	public static final StreamCodec<RegistryFriendlyByteBuf, HieroglyphCode> STREAM_CODEC = ByteBufCodecs.registry(Registries.BLOCK).apply(ByteBufCodecs.list())
			.map(HieroglyphCode::new, HieroglyphCode::hieroglyphs);
	
	public static final HieroglyphCode EMPTY = new HieroglyphCode(Collections.emptyList());
	
	public static Set<Block> getBlocks(ItemStack stack)
	{
		HieroglyphCode hieroglyphCodes = stack.get(MSItemComponents.HIEROGLYPH_CODE);
		return hieroglyphCodes != null ? Set.copyOf(hieroglyphCodes.hieroglyphs()) : Set.of();
	}
	
	public static void setBlocks(ItemStack stack, Set<Block> hieroglyphs)
	{
		stack.set(MSItemComponents.HIEROGLYPH_CODE, new HieroglyphCode(List.copyOf(hieroglyphs)));
	}
	
	public static boolean addBlock(ItemStack stack, Block hieroglyph)
	{
		HieroglyphCode existingCode = stack.getOrDefault(MSItemComponents.HIEROGLYPH_CODE, EMPTY);
		if(existingCode.hieroglyphs().contains(hieroglyph))
			return false;
		
		ImmutableList.Builder<Block> listBuilder = ImmutableList.builder();
		listBuilder.add(hieroglyph);
		listBuilder.addAll(existingCode.hieroglyphs());
		stack.set(MSItemComponents.HIEROGLYPH_CODE, new HieroglyphCode(listBuilder.build()));
		return true;
	}
}
