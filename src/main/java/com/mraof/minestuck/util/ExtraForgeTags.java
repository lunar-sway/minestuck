package com.mraof.minestuck.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * A place to put tags that forge doesn't add themselves, but which are generally expected to be filled by other mods.
 */
public class ExtraForgeTags
{
	public static class Blocks
	{
		public static final TagKey<Block> URANIUM_ORES = tag("ores/uranium");
		public static final TagKey<Block> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final TagKey<Block> TERRACOTTA = tag("terracotta");
		
		private static TagKey<Block> tag(String name)
		{
			return BlockTags.create(new ResourceLocation("forge", name));
		}
	}
	
	public static class Items
	{
		public static final TagKey<Item> URANIUM_ORES = tag("ores/uranium");
		public static final TagKey<Item> TIN_ORES = tag("ores/tin");
		public static final TagKey<Item> SILVER_ORES = tag("ores/silver");
		public static final TagKey<Item> LEAD_ORES = tag("ores/lead");
		public static final TagKey<Item> NICKEL_ORES = tag("ores/nickel");
		public static final TagKey<Item> ALUMINIUM_ORES = tag("ores/aluminium");
		public static final TagKey<Item> COBALT_ORES = tag("ores/cobalt");
		public static final TagKey<Item> ARDITE_ORES = tag("ores/ardite");
		public static final TagKey<Item> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final TagKey<Item> URANIUM_CHUNKS = tag("chunks/uranium");
		public static final TagKey<Item> TERRACOTTA = tag("terracotta");
		public static final TagKey<Item> TIN_INGOTS = tag("ingots/tin");
		public static final TagKey<Item> SILVER_INGOTS = tag("ingots/silver");
		public static final TagKey<Item> LEAD_INGOTS = tag("ingots/lead");
		public static final TagKey<Item> NICKEL_INGOTS = tag("ingots/nickel");
		public static final TagKey<Item> INVAR_INGOTS = tag("ingots/invar");
		public static final TagKey<Item> ALUMINIUM_INGOTS = tag("ingots/aluminium");
		public static final TagKey<Item> COBALT_INGOTS = tag("ingots/cobalt");
		public static final TagKey<Item> ARDITE_INGOTS = tag("ingots/ardite");
		public static final TagKey<Item> RED_ALLOY_INGOTS = tag("ingots/red_alloy");
		
		
		private static TagKey<Item> tag(String name)
		{
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}
}
