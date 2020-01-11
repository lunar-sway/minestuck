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
		public static final Tag<Item> COPPER_ORES = tag("ores/copper");
		public static final Tag<Item> TIN_ORES = tag("ores/tin");
		public static final Tag<Item> SILVER_ORES = tag("ores/silver");
		public static final Tag<Item> LEAD_ORES = tag("ores/lead");
		public static final Tag<Item> NICKEL_ORES = tag("ores/nickel");
		public static final Tag<Item> ALUMINIUM_ORES = tag("ores/aluminium");
		public static final Tag<Item> COBALT_ORES = tag("ores/cobalt");
		public static final Tag<Item> ARDITE_ORES = tag("ores/ardite");
		public static final Tag<Item> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final Tag<Item> URANIUM_CHUNKS = tag("chunks/uranium");
		public static final Tag<Item> TERRACOTTA = tag("terracotta");
		public static final Tag<Item> COPPER_INGOTS = tag("ingots/copper");
		public static final Tag<Item> TIN_INGOTS = tag("ingots/tin");
		public static final Tag<Item> SILVER_INGOTS = tag("ingots/silver");
		public static final Tag<Item> LEAD_INGOTS = tag("ingots/lead");
		public static final Tag<Item> NICKEL_INGOTS = tag("ingots/nickel");
		public static final Tag<Item> INVAR_INGOTS = tag("ingots/invar");
		public static final Tag<Item> ALUMINIUM_INGOTS = tag("ingots/aluminium");
		public static final Tag<Item> COBALT_INGOTS = tag("ingots/cobalt");
		public static final Tag<Item> ARDITE_INGOTS = tag("ingots/ardite");
		public static final Tag<Item> RED_ALLOY_INGOTS = tag("ingots/red_alloy");
		
		
		private static Tag<Item> tag(String name)
		{
			return new ItemTags.Wrapper(new ResourceLocation("forge", name));
		}
	}
}
