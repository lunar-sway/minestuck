package com.mraof.minestuck.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * A place to put tags that forge doesn't add themselves, but which are generally expected to be filled by other mods.
 */
public class ExtraForgeTags
{
	public static class Blocks
	{
		public static final Tag<Block> URANIUM_ORES = tag("ores/uranium");
		public static final Tag<Block> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final Tag<Block> TERRACOTTA = tag("terracotta");
		
		private static Tag<Block> tag(String name)
		{
			return new BlockTags.Wrapper(new ResourceLocation("forge", name));
		}
	}
	
	public static class Items
	{
		public static final Tag<Item> URANIUM_ORES = tag("ores/uranium");
		public static final Tag<Item> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final Tag<Item> URANIUM_CHUNKS = tag("chunks/uranium");
		public static final Tag<Item> TERRACOTTA = tag("terracotta");
		
		private static Tag<Item> tag(String name)
		{
			return new ItemTags.Wrapper(new ResourceLocation("forge", name));
		}
	}
}
