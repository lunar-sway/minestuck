package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class MSTags
{
	public static class Blocks
	{
		public static final TagKey<Block> GLOWING_LOGS = tag("logs/glowing");
		public static final TagKey<Block> FROST_LOGS = tag("logs/frost");
		public static final TagKey<Block> RAINBOW_LOGS = tag("logs/rainbow");
		public static final TagKey<Block> END_LOGS = tag("logs/end");
		public static final TagKey<Block> VINE_LOGS = tag("logs/vine");
		public static final TagKey<Block> FLOWERY_VINE_LOGS = tag("logs/flowery_vine");
		public static final TagKey<Block> DEAD_LOGS = tag("logs/dead");
		public static final TagKey<Block> PETRIFIED_LOGS = tag("logs/petrified");
		public static final TagKey<Block> ASPECT_LOGS = tag("logs/aspect");
		public static final TagKey<Block> ASPECT_PLANKS = tag("planks/aspect");
		public static final TagKey<Block> ASPECT_LEAVES = tag("leaves/aspect");
		public static final TagKey<Block> ASPECT_SAPLINGS = tag("saplings/aspect");
		public static final TagKey<Block> CRUXITE_ORES = tag("ores/cruxite");
		public static final TagKey<Block> URANIUM_ORES = tag("ores/uranium");
		public static final TagKey<Block> COAL_ORES = tag("ores/coal");
		public static final TagKey<Block> IRON_ORES = tag("ores/iron");
		public static final TagKey<Block> GOLD_ORES = tag("ores/gold");
		public static final TagKey<Block> REDSTONE_ORES = tag("ores/redstone");
		public static final TagKey<Block> QUARTZ_ORES = tag("ores/quartz");
		public static final TagKey<Block> LAPIS_ORES = tag("ores/lapis");
		public static final TagKey<Block> DIAMOND_ORES = tag("ores/diamond");
		public static final TagKey<Block> CRUXITE_STORAGE_BLOCKS = tag("storage_blocks/cruxite");
		public static final TagKey<Block> END_SAPLING_DIRT = tag("end_sapling_dirt");
		public static final TagKey<Block> ROTATOR_WHITELISTED = tag("rule_exempt_rotatable");
		public static final TagKey<Block> PLATFORM_ABSORBING = tag("platform_absorbing");
		public static final TagKey<Block> PUSHABLE_BLOCK_REPLACABLE = tag("portable_block_replacable");
		
		private static TagKey<Block> tag(String name)
		{
			return BlockTags.create(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class Items
	{
		public static final TagKey<Item> GLOWING_LOGS = tag("logs/glowing");
		public static final TagKey<Item> FROST_LOGS = tag("logs/frost");
		public static final TagKey<Item> RAINBOW_LOGS = tag("logs/rainbow");
		public static final TagKey<Item> END_LOGS = tag("logs/end");
		public static final TagKey<Item> VINE_LOGS = tag("logs/vine");
		public static final TagKey<Item> FLOWERY_VINE_LOGS = tag("logs/flowery_vine");
		public static final TagKey<Item> DEAD_LOGS = tag("logs/dead");
		public static final TagKey<Item> PETRIFIED_LOGS = tag("logs/petrified");
		public static final TagKey<Item> ASPECT_LOGS = tag("logs/aspect");
		public static final TagKey<Item> ASPECT_PLANKS = tag("planks/aspect");
		public static final TagKey<Item> ASPECT_LEAVES = tag("leaves/aspect");
		public static final TagKey<Item> ASPECT_SAPLINGS = tag("saplings/aspect");
		public static final TagKey<Item> CRUXITE_ORES = tag("ores/cruxite");
		public static final TagKey<Item> URANIUM_ORES = tag("ores/uranium");
		public static final TagKey<Item> COAL_ORES = tag("ores/coal");
		public static final TagKey<Item> IRON_ORES = tag("ores/iron");
		public static final TagKey<Item> GOLD_ORES = tag("ores/gold");
		public static final TagKey<Item> REDSTONE_ORES = tag("ores/redstone");
		public static final TagKey<Item> QUARTZ_ORES = tag("ores/quartz");
		public static final TagKey<Item> LAPIS_ORES = tag("ores/lapis");
		public static final TagKey<Item> DIAMOND_ORES = tag("ores/diamond");
		public static final TagKey<Item> CRUXITE_STORAGE_BLOCKS = tag("storage_blocks/cruxite");
		public static final TagKey<Item> GRIST_CANDY = tag("grist_candy");
		public static final TagKey<Item> FAYGO = tag("faygo");
		public static final TagKey<Item> MODUS_CARD = tag("modus_card");
		public static final TagKey<Item> CASSETTES = tag("cassettes");
		public static final TagKey<Item> BUGS = tag("bugs");
		public static final TagKey<Item> CREATIVE_SHOCK_RIGHT_CLICK_LIMIT = tag("creative_shock_right_click_limit");

		private static TagKey<Item> tag(String name)
		{
			return ItemTags.create(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class Fluids
	{
		public static final TagKey<Fluid> OIL = tag("oil");
		public static final TagKey<Fluid> BLOOD = tag("blood");
		public static final TagKey<Fluid> BRAIN_JUICE = tag("brain_juice");
		public static final TagKey<Fluid> WATER_COLORS = tag("water_colors");
		public static final TagKey<Fluid> ENDER = tag("ender");
		public static final TagKey<Fluid> LIGHT_WATER = tag("light_water");
		
		private static TagKey<Fluid> tag(String name)
		{
			return FluidTags.create(new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
	
	public static class EntityTypes
	{
		public static final TagKey<EntityType<?>> UNDERLINGS = tag("underlings");
		public static final TagKey<EntityType<?>> CONSORTS = tag("consorts");
		public static final TagKey<EntityType<?>> CARAPACIANS = tag("carapacians");
		public static final TagKey<EntityType<?>> DERSITE_CARAPACIANS = tag("carapacians/dersite");
		public static final TagKey<EntityType<?>> PROSPITIAN_CARAPACIANS = tag("carapacians/prospitian");
		public static final TagKey<EntityType<?>> PAWNS = tag("carapacians/pawn");
		public static final TagKey<EntityType<?>> BISHOPS = tag("carapacians/bishop");
		public static final TagKey<EntityType<?>> ROOKS = tag("carapacians/rook");
		public static final TagKey<EntityType<?>> MAGNET_RECEPTIVE = tag("magnet_receptive");
		public static final TagKey<EntityType<?>> REMOTE_OBSERVER_BLACKLIST = tag("remote_observer_blacklist");
		
		private static TagKey<EntityType<?>> tag(String name)
		{
			return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, name));
		}
	}
}