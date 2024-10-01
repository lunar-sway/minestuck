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
public final class ExtraForgeTags
{
	public static final class Blocks
	{
		public static final TagKey<Block> URANIUM_ORES = tag("ores/uranium");
		public static final TagKey<Block> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		
		private static TagKey<Block> tag(String name)
		{
			return BlockTags.create(new ResourceLocation("forge", name));
		}
	}
	
	public static final class Items
	{
		public static final TagKey<Item> URANIUM_ORES = tag("ores/uranium");
		public static final TagKey<Item> TIN_ORES = tag("ores/tin");
		public static final TagKey<Item> SILVER_ORES = tag("ores/silver");
		public static final TagKey<Item> LEAD_ORES = tag("ores/lead");
		public static final TagKey<Item> GALENA_ORES = tag("ores/galena"); //lead ore, some mods may also have it drop silver
		public static final TagKey<Item> NICKEL_ORES = tag("ores/nickel");
		public static final TagKey<Item> ZINC_ORES = tag("ores/zinc");
		public static final TagKey<Item> ALUMINIUM_ORES = tag("ores/aluminium"); //most mods do not seem to use this spelling
		public static final TagKey<Item> ALUMINUM_ORES = tag("ores/aluminum");
		public static final TagKey<Item> COBALT_ORES = tag("ores/cobalt");
		public static final TagKey<Item> ARDITE_ORES = tag("ores/ardite");
		
		public static final TagKey<Item> TIN_RAW_MATERIALS = tag("raw_materials/tin");
		public static final TagKey<Item> SILVER_RAW_MATERIALS = tag("raw_materials/silver");
		public static final TagKey<Item> LEAD_RAW_MATERIALS = tag("raw_materials/lead");
		public static final TagKey<Item> GALENA_RAW_MATERIALS = tag("raw_materials/galena");
		public static final TagKey<Item> NICKEL_RAW_MATERIALS = tag("raw_materials/nickel");
		public static final TagKey<Item> ZINC_RAW_MATERIALS = tag("raw_materials/zinc");
		public static final TagKey<Item> ALUMINIUM_RAW_MATERIALS = tag("raw_materials/aluminium");
		public static final TagKey<Item> ALUMINUM_RAW_MATERIALS = tag("raw_materials/aluminum");
		public static final TagKey<Item> COBALT_RAW_MATERIALS = tag("raw_materials/cobalt");
		public static final TagKey<Item> ARDITE_RAW_MATERIALS = tag("raw_materials/ardite");
		
		public static final TagKey<Item> URANIUM_STORAGE_BLOCKS = tag("storage_blocks/uranium");
		public static final TagKey<Item> URANIUM_CHUNKS = tag("chunks/uranium");
		
		public static final TagKey<Item> URANIUM_INGOTS = tag("ingots/uranium");
		public static final TagKey<Item> TIN_INGOTS = tag("ingots/tin");
		public static final TagKey<Item> BRASS_INGOTS = tag("ingots/brass");
		public static final TagKey<Item> SILVER_INGOTS = tag("ingots/silver");
		public static final TagKey<Item> ELECTRUM_INGOTS = tag("ingots/electrum");
		public static final TagKey<Item> LEAD_INGOTS = tag("ingots/lead");
		public static final TagKey<Item> NICKEL_INGOTS = tag("ingots/nickel");
		public static final TagKey<Item> ZINC_INGOTS = tag("ingots/zinc");
		public static final TagKey<Item> INVAR_INGOTS = tag("ingots/invar");
		public static final TagKey<Item> ALUMINIUM_INGOTS = tag("ingots/aluminium");
		public static final TagKey<Item> ALUMINUM_INGOTS = tag("ingots/aluminum");
		public static final TagKey<Item> COBALT_INGOTS = tag("ingots/cobalt");
		public static final TagKey<Item> ARDITE_INGOTS = tag("ingots/ardite");
		public static final TagKey<Item> RED_ALLOY_INGOTS = tag("ingots/red_alloy");
		
		
		private static TagKey<Item> tag(String name)
		{
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}
}
