package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class MSTags
{
	public static class Blocks
	{
		public static final Tag<Block> GLOWING_LOGS = tag("logs/glowing");
		public static final Tag<Block> FROST_LOGS = tag("logs/frost");
		public static final Tag<Block> RAINBOW_LOGS = tag("logs/rainbow");
		public static final Tag<Block> END_LOGS = tag("logs/end");
		public static final Tag<Block> VINE_LOGS = tag("logs/vine");
		public static final Tag<Block> FLOWERY_VINE_LOGS = tag("logs/flowery_vine");
		public static final Tag<Block> DEAD_LOGS = tag("logs/dead");
		public static final Tag<Block> PETRIFIED_LOGS = tag("logs/petrified");
		public static final Tag<Block> ASPECT_LOGS = tag("logs/aspect");
		public static final Tag<Block> ASPECT_PLANKS = tag("planks/aspect");
		public static final Tag<Block> ASPECT_LEAVES = tag("leaves/aspect");
		public static final Tag<Block> ASPECT_SAPLINGS = tag("saplings/aspect");
		public static final Tag<Block> CRUXITE_ORES = tag("ores/cruxite");
		public static final Tag<Block> URANIUM_ORES = tag("ores/uranium");
		public static final Tag<Block> COAL_ORES = tag("ores/coal");
		public static final Tag<Block> IRON_ORES = tag("ores/iron");
		public static final Tag<Block> GOLD_ORES = tag("ores/gold");
		public static final Tag<Block> REDSTONE_ORES = tag("ores/redstone");
		public static final Tag<Block> QUARTZ_ORES = tag("ores/quartz");
		public static final Tag<Block> LAPIS_ORES = tag("ores/lapis");
		public static final Tag<Block> DIAMOND_ORES = tag("ores/diamond");
		public static final Tag<Block> CRUXITE_STORAGE_BLOCKS = tag("storage_blocks/cruxite");
		public static final Tag<Block> END_SAPLING_DIRT = tag("end_sapling_dirt");
		
		private static Tag<Block> tag(String name)
		{
			return new BlockTags.Wrapper(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class Items
	{
		public static final Tag<Item> GLOWING_LOGS = tag("logs/glowing");
		public static final Tag<Item> FROST_LOGS = tag("logs/frost");
		public static final Tag<Item> RAINBOW_LOGS = tag("logs/rainbow");
		public static final Tag<Item> END_LOGS = tag("logs/end");
		public static final Tag<Item> VINE_LOGS = tag("logs/vine");
		public static final Tag<Item> FLOWERY_VINE_LOGS = tag("logs/flowery_vine");
		public static final Tag<Item> DEAD_LOGS = tag("logs/dead");
		public static final Tag<Item> PETRIFIED_LOGS = tag("logs/petrified");
		public static final Tag<Item> ASPECT_LOGS = tag("logs/aspect");
		public static final Tag<Item> ASPECT_PLANKS = tag("planks/aspect");
		public static final Tag<Item> ASPECT_LEAVES = tag("leaves/aspect");
		public static final Tag<Item> ASPECT_SAPLINGS = tag("saplings/aspect");
		public static final Tag<Item> CRUXITE_ORES = tag("ores/cruxite");
		public static final Tag<Item> URANIUM_ORES = tag("ores/uranium");
		public static final Tag<Item> COAL_ORES = tag("ores/coal");
		public static final Tag<Item> IRON_ORES = tag("ores/iron");
		public static final Tag<Item> GOLD_ORES = tag("ores/gold");
		public static final Tag<Item> REDSTONE_ORES = tag("ores/redstone");
		public static final Tag<Item> QUARTZ_ORES = tag("ores/quartz");
		public static final Tag<Item> LAPIS_ORES = tag("ores/lapis");
		public static final Tag<Item> DIAMOND_ORES = tag("ores/diamond");
		public static final Tag<Item> CRUXITE_STORAGE_BLOCKS = tag("storage_blocks/cruxite");
		public static final Tag<Item> GRIST_CANDY = tag("grist_candy");
		public static final Tag<Item> FAYGO = tag("faygo");
		public static final Tag<Item> MODUS_CARD = tag("modus_card");
		public static final Tag<Item> CASSETTES = tag("cassettes");
		public static final Tag<Item> BUGS = tag("bugs");
		public static final Tag<Item> GUNS = tag("guns");
		public static final Tag<Item> BULLETS = tag("bullets");

		private static Tag<Item> tag(String name)
		{
			return new ItemTags.Wrapper(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class Fluids
	{
		public static final Tag<Fluid> OIL = tag("oil");
		public static final Tag<Fluid> BLOOD = tag("blood");
		public static final Tag<Fluid> BRAIN_JUICE = tag("brain_juice");
		public static final Tag<Fluid> WATER_COLORS = tag("water_colors");
		public static final Tag<Fluid> ENDER = tag("ender");
		public static final Tag<Fluid> LIGHT_WATER = tag("light_water");
		
		private static Tag<Fluid> tag(String name)
		{
			return new FluidTags.Wrapper(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class EntityTypes
	{
		public static final Tag<EntityType<?>> UNDERLINGS = tag("underlings");
		public static final Tag<EntityType<?>> CONSORTS = tag("consorts");
		public static final Tag<EntityType<?>> CARAPACIANS = tag("carapacians");
		public static final Tag<EntityType<?>> DERSITE_CARAPACIANS = tag("carapacians/dersite");
		public static final Tag<EntityType<?>> PROSPITIAN_CARAPACIANS = tag("carapacians/prospitian");
		public static final Tag<EntityType<?>> PAWNS = tag("carapacians/pawn");
		public static final Tag<EntityType<?>> BISHOPS = tag("carapacians/bishop");
		public static final Tag<EntityType<?>> ROOKS = tag("carapacians/rook");
		
		private static Tag<EntityType<?>> tag(String name)
		{
			return new EntityTypeTags.Wrapper(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static Tag<Item> dyeItemTag(DyeColor color)
	{
		switch(color)
		{
			case WHITE: return Tags.Items.DYES_WHITE;
			case ORANGE: return Tags.Items.DYES_ORANGE;
			case MAGENTA: return Tags.Items.DYES_MAGENTA;
			case LIGHT_BLUE: return Tags.Items.DYES_LIGHT_BLUE;
			case YELLOW: return Tags.Items.DYES_YELLOW;
			case LIME: return Tags.Items.DYES_LIME;
			case PINK: return Tags.Items.DYES_PINK;
			case GRAY: return Tags.Items.DYES_GRAY;
			case LIGHT_GRAY: return Tags.Items.DYES_LIGHT_GRAY;
			case CYAN: return Tags.Items.DYES_CYAN;
			case PURPLE: return Tags.Items.DYES_PURPLE;
			case BLUE: return Tags.Items.DYES_BLUE;
			case BROWN: return Tags.Items.DYES_BROWN;
			case GREEN: return Tags.Items.DYES_GREEN;
			case RED: return Tags.Items.DYES_RED;
			case BLACK: return Tags.Items.DYES_BLACK;
			default: throw new IllegalArgumentException("Got unexpected color: "+color);
		}
	}
}