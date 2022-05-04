package com.mraof.minestuck.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

/**
 * A place to put tags that forge doesn't add themselves, but which are generally expected to be filled by other mods.
 */
public class ExtraForgeTags
{
	public static class Blocks
	{
		public static final Tags.IOptionalNamedTag<Block> URANIUM_ORES = tag("ores/uranium");
		public static final Tags.IOptionalNamedTag<Block> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final Tags.IOptionalNamedTag<Block> TERRACOTTA = tag("terracotta");
		
		private static Tags.IOptionalNamedTag<Block> tag(String name)
		{
			return BlockTags.createOptional(new ResourceLocation("forge", name));
		}
	}
	
	public static class Items
	{
		public static final Tags.IOptionalNamedTag<Item> URANIUM_ORES = tag("ores/uranium");
		public static final Tags.IOptionalNamedTag<Item> COPPER_ORES = tag("ores/copper");
		public static final Tags.IOptionalNamedTag<Item> TIN_ORES = tag("ores/tin");
		public static final Tags.IOptionalNamedTag<Item> SILVER_ORES = tag("ores/silver");
		public static final Tags.IOptionalNamedTag<Item> LEAD_ORES = tag("ores/lead");
		public static final Tags.IOptionalNamedTag<Item> NICKEL_ORES = tag("ores/nickel");
		public static final Tags.IOptionalNamedTag<Item> ALUMINIUM_ORES = tag("ores/aluminium");
		public static final Tags.IOptionalNamedTag<Item> COBALT_ORES = tag("ores/cobalt");
		public static final Tags.IOptionalNamedTag<Item> ARDITE_ORES = tag("ores/ardite");
		public static final Tags.IOptionalNamedTag<Item> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final Tags.IOptionalNamedTag<Item> URANIUM_CHUNKS = tag("chunks/uranium");
		public static final Tags.IOptionalNamedTag<Item> TERRACOTTA = tag("terracotta");
		public static final Tags.IOptionalNamedTag<Item> COPPER_INGOTS = tag("ingots/copper");
		public static final Tags.IOptionalNamedTag<Item> TIN_INGOTS = tag("ingots/tin");
		public static final Tags.IOptionalNamedTag<Item> SILVER_INGOTS = tag("ingots/silver");
		public static final Tags.IOptionalNamedTag<Item> LEAD_INGOTS = tag("ingots/lead");
		public static final Tags.IOptionalNamedTag<Item> NICKEL_INGOTS = tag("ingots/nickel");
		public static final Tags.IOptionalNamedTag<Item> INVAR_INGOTS = tag("ingots/invar");
		public static final Tags.IOptionalNamedTag<Item> ALUMINIUM_INGOTS = tag("ingots/aluminium");
		public static final Tags.IOptionalNamedTag<Item> COBALT_INGOTS = tag("ingots/cobalt");
		public static final Tags.IOptionalNamedTag<Item> ARDITE_INGOTS = tag("ingots/ardite");
		public static final Tags.IOptionalNamedTag<Item> RED_ALLOY_INGOTS = tag("ingots/red_alloy");
		
		
		private static Tags.IOptionalNamedTag<Item> tag(String name)
		{
			return ItemTags.createOptional(new ResourceLocation("forge", name));
		}
	}
}
