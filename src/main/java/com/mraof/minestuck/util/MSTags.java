package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class MSTags
{
	public static class Blocks
	{
		public static final Tags.IOptionalNamedTag<Block> GLOWING_LOGS = tag("logs/glowing");
		public static final Tags.IOptionalNamedTag<Block> FROST_LOGS = tag("logs/frost");
		public static final Tags.IOptionalNamedTag<Block> RAINBOW_LOGS = tag("logs/rainbow");
		public static final Tags.IOptionalNamedTag<Block> END_LOGS = tag("logs/end");
		public static final Tags.IOptionalNamedTag<Block> VINE_LOGS = tag("logs/vine");
		public static final Tags.IOptionalNamedTag<Block> FLOWERY_VINE_LOGS = tag("logs/flowery_vine");
		public static final Tags.IOptionalNamedTag<Block> DEAD_LOGS = tag("logs/dead");
		public static final Tags.IOptionalNamedTag<Block> PETRIFIED_LOGS = tag("logs/petrified");
		public static final Tags.IOptionalNamedTag<Block> ASPECT_LOGS = tag("logs/aspect");
		public static final Tags.IOptionalNamedTag<Block> ASPECT_PLANKS = tag("planks/aspect");
		public static final Tags.IOptionalNamedTag<Block> ASPECT_LEAVES = tag("leaves/aspect");
		public static final Tags.IOptionalNamedTag<Block> ASPECT_SAPLINGS = tag("saplings/aspect");
		public static final Tags.IOptionalNamedTag<Block> CRUXITE_ORES = tag("ores/cruxite");
		public static final Tags.IOptionalNamedTag<Block> URANIUM_ORES = tag("ores/uranium");
		public static final Tags.IOptionalNamedTag<Block> COAL_ORES = tag("ores/coal");
		public static final Tags.IOptionalNamedTag<Block> IRON_ORES = tag("ores/iron");
		public static final Tags.IOptionalNamedTag<Block> GOLD_ORES = tag("ores/gold");
		public static final Tags.IOptionalNamedTag<Block> REDSTONE_ORES = tag("ores/redstone");
		public static final Tags.IOptionalNamedTag<Block> QUARTZ_ORES = tag("ores/quartz");
		public static final Tags.IOptionalNamedTag<Block> LAPIS_ORES = tag("ores/lapis");
		public static final Tags.IOptionalNamedTag<Block> DIAMOND_ORES = tag("ores/diamond");
		public static final Tags.IOptionalNamedTag<Block> CRUXITE_STORAGE_BLOCKS = tag("storage_blocks/cruxite");
		public static final Tags.IOptionalNamedTag<Block> END_SAPLING_DIRT = tag("end_sapling_dirt");
		public static final Tags.IOptionalNamedTag<Block> RULE_EXEMPT_ROTATABLE = tag("rule_exempt_rotatable");
		
		private static Tags.IOptionalNamedTag<Block> tag(String name)
		{
			return BlockTags.createOptional(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class Items
	{
		public static final Tags.IOptionalNamedTag<Item> GLOWING_LOGS = tag("logs/glowing");
		public static final Tags.IOptionalNamedTag<Item> FROST_LOGS = tag("logs/frost");
		public static final Tags.IOptionalNamedTag<Item> RAINBOW_LOGS = tag("logs/rainbow");
		public static final Tags.IOptionalNamedTag<Item> END_LOGS = tag("logs/end");
		public static final Tags.IOptionalNamedTag<Item> VINE_LOGS = tag("logs/vine");
		public static final Tags.IOptionalNamedTag<Item> FLOWERY_VINE_LOGS = tag("logs/flowery_vine");
		public static final Tags.IOptionalNamedTag<Item> DEAD_LOGS = tag("logs/dead");
		public static final Tags.IOptionalNamedTag<Item> PETRIFIED_LOGS = tag("logs/petrified");
		public static final Tags.IOptionalNamedTag<Item> ASPECT_LOGS = tag("logs/aspect");
		public static final Tags.IOptionalNamedTag<Item> ASPECT_PLANKS = tag("planks/aspect");
		public static final Tags.IOptionalNamedTag<Item> ASPECT_LEAVES = tag("leaves/aspect");
		public static final Tags.IOptionalNamedTag<Item> ASPECT_SAPLINGS = tag("saplings/aspect");
		public static final Tags.IOptionalNamedTag<Item> CRUXITE_ORES = tag("ores/cruxite");
		public static final Tags.IOptionalNamedTag<Item> URANIUM_ORES = tag("ores/uranium");
		public static final Tags.IOptionalNamedTag<Item> COAL_ORES = tag("ores/coal");
		public static final Tags.IOptionalNamedTag<Item> IRON_ORES = tag("ores/iron");
		public static final Tags.IOptionalNamedTag<Item> GOLD_ORES = tag("ores/gold");
		public static final Tags.IOptionalNamedTag<Item> REDSTONE_ORES = tag("ores/redstone");
		public static final Tags.IOptionalNamedTag<Item> QUARTZ_ORES = tag("ores/quartz");
		public static final Tags.IOptionalNamedTag<Item> LAPIS_ORES = tag("ores/lapis");
		public static final Tags.IOptionalNamedTag<Item> DIAMOND_ORES = tag("ores/diamond");
		public static final Tags.IOptionalNamedTag<Item> CRUXITE_STORAGE_BLOCKS = tag("storage_blocks/cruxite");
		public static final Tags.IOptionalNamedTag<Item> GRIST_CANDY = tag("grist_candy");
		public static final Tags.IOptionalNamedTag<Item> FAYGO = tag("faygo");
		public static final Tags.IOptionalNamedTag<Item> MODUS_CARD = tag("modus_card");
		public static final Tags.IOptionalNamedTag<Item> CASSETTES = tag("cassettes");
		public static final Tags.IOptionalNamedTag<Item> BUGS = tag("bugs");
		public static final Tags.IOptionalNamedTag<Item> CREATIVE_SHOCK_RIGHT_CLICK_LIMIT = tag("creative_shock_right_click_limit");

		private static Tags.IOptionalNamedTag<Item> tag(String name)
		{
			return ItemTags.createOptional(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class Fluids
	{
		public static final Tags.IOptionalNamedTag<Fluid> OIL = tag("oil");
		public static final Tags.IOptionalNamedTag<Fluid> BLOOD = tag("blood");
		public static final Tags.IOptionalNamedTag<Fluid> BRAIN_JUICE = tag("brain_juice");
		public static final Tags.IOptionalNamedTag<Fluid> WATER_COLORS = tag("water_colors");
		public static final Tags.IOptionalNamedTag<Fluid> ENDER = tag("ender");
		public static final Tags.IOptionalNamedTag<Fluid> LIGHT_WATER = tag("light_water");
		
		private static Tags.IOptionalNamedTag<Fluid> tag(String name)
		{
			return FluidTags.createOptional(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class EntityTypes
	{
		public static final Tags.IOptionalNamedTag<EntityType<?>> UNDERLINGS = tag("underlings");
		public static final Tags.IOptionalNamedTag<EntityType<?>> CONSORTS = tag("consorts");
		public static final Tags.IOptionalNamedTag<EntityType<?>> CARAPACIANS = tag("carapacians");
		public static final Tags.IOptionalNamedTag<EntityType<?>> DERSITE_CARAPACIANS = tag("carapacians/dersite");
		public static final Tags.IOptionalNamedTag<EntityType<?>> PROSPITIAN_CARAPACIANS = tag("carapacians/prospitian");
		public static final Tags.IOptionalNamedTag<EntityType<?>> PAWNS = tag("carapacians/pawn");
		public static final Tags.IOptionalNamedTag<EntityType<?>> BISHOPS = tag("carapacians/bishop");
		public static final Tags.IOptionalNamedTag<EntityType<?>> ROOKS = tag("carapacians/rook");
		public static final Tags.IOptionalNamedTag<EntityType<?>> MAGNET_RECEPTIVE = tag("magnet_receptive");
		
		private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name)
		{
			return EntityTypeTags.createOptional(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
}