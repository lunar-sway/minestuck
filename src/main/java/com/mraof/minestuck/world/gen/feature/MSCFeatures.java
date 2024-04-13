package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * Holds minestuck configured features. Also creates and registers them when appropriate.
 * See {@link MSFeatures} for minestuck features.
 * (Configured features are world-gen features that has been configured with blocks, sizes or whatever else features may be configured with)
 * Check {@link MSPlacedFeatures} for utilizations of these configured features
 */
public final class MSCFeatures
{
	public static final ResourceKey<ConfiguredFeature<?, ?>> RETURN_NODE = key("return_node");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_COG = key("small_cog");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_COG_1 = key("large_cog_1");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_COG_2 = key("large_cog_2");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_COG = key("large_cog");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COG = key("cog");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOOR_COG = key("floor_cog");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_FOSSIL = key("surface_fossil");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BROKEN_SWORD = key("broken_sword");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BUCKET = key("bucket");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAKE_PEDESTAL = key("cake_pedestal");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_LIBRARY = key("small_library");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TOWER = key("tower");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PARCEL_PYXIS = key("parcel_pyxis");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FROG_RUINS = key("frog_ruins");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_CHERRY_TREE = key("carved_cherry_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_HOUSE = key("carved_house");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_LOG = key("carved_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_CARVED_LOG = key("large_carved_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_CHAIR = key("treated_chair");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_TABLE = key("treated_table");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_BOOKSHELF = key("unfinished_carved_bookshelf");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_CHAIR = key("unfinished_carved_chair");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_CREEPER = key("unfinished_carved_creeper");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_DRAWER = key("unfinished_carved_drawer");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_SALAMANDER = key("unfinished_carved_salamander");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_TABLE = key("unfinished_carved_table");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_TREE = key("unfinished_carved_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNFINISHED_CARVED_TUNNEL = key("unfinished_carved_tunnel");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WOOD_SHAVINGS_PILE = key("wood_shavings_pile");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WOODEN_CACTUS_PAIR = key("wooden_cactus_pair");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MASSIVE_CHAIR = key("massive_chair");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MASSIVE_STOOL = key("massive_stool");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_POOL = key("sulfur_pool");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAST_IRON_BUILDING = key("cast_iron_building");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAST_IRON_PLATFORM = key("cast_iron_platform");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_POOL = key("blood_pool");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OIL_POOL = key("oil_pool");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OASIS = key("oasis");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OCEAN_RUNDOWN = key("ocean_rundown");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_FIELD = key("fire_field");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COARSE_DIRT_DISK = key("coarse_dirt_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_BLOCK_DISK = key("snow_block_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_SNOW_BLOCK_DISK = key("small_snow_block_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_DISK = key("ice_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SAND_DISK = key("sand_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_SAND_DISK = key("red_sand_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SLIME_DISK = key("slime_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> NETHERRACK_DISK = key("netherrack_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MAGMATIC_IGNEOUS_DISK = key("magmatic_igneous_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLACK_SAND_DISK = key("black_sand_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PUMICE_STONE_DISK = key("pumice_stone_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_PLANKS_DISK = key("treated_planks_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LACQUERED_PLANKS_DISK = key("lacquered_planks_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_UNCARVED_WOOD_DISK = key("treated_uncarved_wood_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_CHIPBOARD_DISK = key("treated_chipboard_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CHIPBOARD_DISK = key("chipboard_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_PLANKS_DISK = key("carved_planks_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COAGULATED_BLOOD_DISK = key("coagulated_blood_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COBBLESTONE_SURFACE_DISK = key("cobblestone_surface_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_SURFACE_DISK = key("stone_surface_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_GRASS_SURFACE_DISK = key("end_grass_surface_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_STONE_SURFACE_DISK = key("end_stone_surface_disk");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> MESA = key("mesa");
	public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_MOUND = key("stone_mound");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COBBLESTONE_BLOCK_BLOB = key("cobblestone_block_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SANDSTONE_BLOCK_BLOB = key("sandstone_block_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_SANDSTONE_BLOCK_BLOB = key("red_sandstone_block_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RANDOM_ROCK_BLOCK_BLOB = key("random_rock_block_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_RANDOM_ROCK_BLOCK_BLOB = key("large_random_rock_block_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SHADE_STONE_BLOCK_BLOB = key("shade_stone_block_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_PILLAR = key("small_pillar");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PILLAR = key("pillar");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MIXED_PILLARS = key("pillars");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_CAKE = key("large_cake");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_TREE = key("rainbow_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_TREE = key("end_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWING_TREE = key("glowing_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SHADEWOOD_TREE = key("shadewood_tree");
	//scarred variant tends to be a little more scraggly
	public static final ResourceKey<ConfiguredFeature<?, ?>> SCARRED_SHADEWOOD_TREE = key("scarred_shadewood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORNATE_SHADEWOOD_TREE = key("ornate_shadewood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREE_STUMP = key("tree_stump");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_TREE = key("petrified_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_TREE = key("dead_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CINDERED_TREE = key("cindered_tree");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> FROST_TREE = key("frost_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_TREE = key("blood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BREATH_TREE = key("breath_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DOOM_TREE = key("doom_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HEART_TREE = key("heart_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOPE_TREE = key("hope_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LIFE_TREE = key("life_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHT_TREE = key("light_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MIND_TREE = key("mind_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RAGE_TREE = key("rage_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SPACE_TREE = key("space_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TIME_TREE = key("time_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_TREE = key("void_tree");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_LAND_TREES = key("forest_land_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TAIGA_LAND_TREES = key("taiga_land_trees");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> STRAWBERRY_PATCH = key("strawberry_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWING_MUSHROOM_PATCH = key("glowing_mushroom_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WOODEN_GRASS_PATCH = key("wooden_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_WOODEN_GRASS_PATCH = key("treated_wooden_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LACQUERED_WOODEN_MUSHROOM_PATCH = key("lacquered_wooden_mushroom_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_BUSH_PATCH = key("carved_bush_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_END_GRASS_PATCH = key("tall_end_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_GRASS_PATCH = key("petrified_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_POPPY_PATCH = key("petrified_poppy_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGED_GRASS_PATCH = key("singed_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGED_FOLIAGE_PATCH = key("singed_foliage_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> IGNEOUS_SPIKE_PATCH = key("igneous_spike_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_BUBBLE_PATCH = key("sulfur_bubble_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOMING_CACTUS_PATCH = key("blooming_cactus_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DESERT_BUSH_PATCH = key("desert_bush_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SANDY_GRASS_PATCH = key("sandy_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_SANDY_GRASS_PATCH = key("tall_sandy_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_DEAD_BUSH_PATCH = key("tall_dead_bush_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_CARPET_PATCH = key("moss_carpet_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_PATCH = key("azalea_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FUNGUS_PATCH = key("crimson_fungus_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FUNGUS_PATCH = key("warped_fungus_patch");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> PUMPKIN = key("pumpkin");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> CEILING_ROOTS = key("ceiling_roots");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> RABBIT_PLACEMENT = key("rabbit_placement");
	
	/**
	 * The OreConfiguration uses an inlined list that matches ores to the stone type they should replace
	 */
	public static final ResourceKey<ConfiguredFeature<?, ?>> CRUXITE_ORE = key("cruxite_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> URANIUM_ORE = key("uranium_ore");
	
	private static ResourceKey<ConfiguredFeature<?, ?>> key(String name)
	{
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
}