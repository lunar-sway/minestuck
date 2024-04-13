package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public final class MSPlacedFeatures
{
	public static final ResourceKey<PlacedFeature> RETURN_NODE = key("return_node");
	
	public static final ResourceKey<PlacedFeature> COG = key("cog/regular");
	public static final ResourceKey<PlacedFeature> UNCOMMON_COG = key("cog/sparse");
	public static final ResourceKey<PlacedFeature> FLOOR_COG = key("cog/floor_regular");
	public static final ResourceKey<PlacedFeature> UNCOMMON_FLOOR_COG = key("cog/floor_sparse");
	
	public static final ResourceKey<PlacedFeature> SURFACE_FOSSIL = key("surface_fossil");
	
	public static final ResourceKey<PlacedFeature> BROKEN_SWORD = key("broken_sword/regular");
	public static final ResourceKey<PlacedFeature> UNCOMMON_BROKEN_SWORD = key("broken_sword/uncommon");
	public static final ResourceKey<PlacedFeature> BUCKET = key("bucket");
	public static final ResourceKey<PlacedFeature> CAKE_PEDESTAL = key("cake_pedestal");
	public static final ResourceKey<PlacedFeature> SMALL_LIBRARY = key("small_library");
	public static final ResourceKey<PlacedFeature> TOWER = key("tower");
	public static final ResourceKey<PlacedFeature> PARCEL_PYXIS = key("parcel_pyxis");
	public static final ResourceKey<PlacedFeature> FROG_RUINS = key("frog_ruins");
	
	public static final ResourceKey<PlacedFeature> CARVED_CHERRY_TREE = key("carved_cherry_tree");
	public static final ResourceKey<PlacedFeature> CARVED_LOG = key("carved_log");
	public static final ResourceKey<PlacedFeature> CARVED_HOUSE = key("carved_house");
	public static final ResourceKey<PlacedFeature> LARGE_CARVED_LOG = key("large_carved_log");
	public static final ResourceKey<PlacedFeature> SPARSE_TREATED_CHAIR = key("sparse_treated_chair");
	public static final ResourceKey<PlacedFeature> TREATED_CHAIR = key("treated_chair");
	public static final ResourceKey<PlacedFeature> SPARSE_TREATED_TABLE = key("sparse_treated_table");
	public static final ResourceKey<PlacedFeature> TREATED_TABLE = key("treated_table");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_BOOKSHELF = key("unfinished_carved_bookshelf");
	public static final ResourceKey<PlacedFeature> SPARSE_UNFINISHED_CARVED_CHAIR = key("sparse_unfinished_carved_chair");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_CHAIR = key("unfinished_carved_chair");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_CREEPER = key("unfinished_carved_creeper");
	public static final ResourceKey<PlacedFeature> SPARSE_UNFINISHED_CARVED_DRAWER = key("sparse_unfinished_carved_drawer");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_DRAWER = key("unfinished_carved_drawer");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_SALAMANDER = key("unfinished_carved_salamander");
	public static final ResourceKey<PlacedFeature> SPARSE_UNFINISHED_CARVED_TABLE = key("sparse_unfinished_carved_table");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_TABLE = key("unfinished_carved_table");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_TREE = key("unfinished_carved_tree");
	public static final ResourceKey<PlacedFeature> UNFINISHED_CARVED_TUNNEL = key("unfinished_carved_tunnel");
	public static final ResourceKey<PlacedFeature> WOOD_SHAVINGS_PILE = key("wood_shavings_pile");
	public static final ResourceKey<PlacedFeature> WOODEN_CACTUS_PAIR = key("wooden_cactus_pair");
	public static final ResourceKey<PlacedFeature> MASSIVE_CHAIR = key("massive_chair");
	public static final ResourceKey<PlacedFeature> MASSIVE_STOOL = key("massive_stool");
	
	public static final ResourceKey<PlacedFeature> SULFUR_POOL = key("sulfur_pool");
	public static final ResourceKey<PlacedFeature> CAST_IRON_BUILDING = key("cast_iron_building");
	public static final ResourceKey<PlacedFeature> CAST_IRON_PLATFORM = key("cast_iron_platform");
	
	public static final ResourceKey<PlacedFeature> LARGE_CAKE = key("large_cake");
	
	public static final ResourceKey<PlacedFeature> BLOOD_POOL = key("blood_pool");
	public static final ResourceKey<PlacedFeature> OIL_POOL = key("oil_pool");
	public static final ResourceKey<PlacedFeature> OASIS = key("oasis");
	public static final ResourceKey<PlacedFeature> OCEAN_RUNDOWN = key("ocean_rundown");
	
	public static final ResourceKey<PlacedFeature> FIRE_FIELD = key("fire_field");
	public static final ResourceKey<PlacedFeature> EXTRA_FIRE_FIELD = key("extra_fire_field");
	public static final ResourceKey<PlacedFeature> COARSE_DIRT_DISK = key("coarse_dirt_disk");
	public static final ResourceKey<PlacedFeature> SNOW_BLOCK_DISK = key("snow_block_disk");
	public static final ResourceKey<PlacedFeature> SMALL_SNOW_BLOCK_DISK = key("small_snow_block_disk");
	public static final ResourceKey<PlacedFeature> ICE_DISK = key("ice_disk");
	public static final ResourceKey<PlacedFeature> SAND_DISK = key("sand_disk");
	public static final ResourceKey<PlacedFeature> RED_SAND_DISK = key("red_sand_disk");
	public static final ResourceKey<PlacedFeature> SLIME_DISK = key("slime_disk");
	public static final ResourceKey<PlacedFeature> EXTRA_SLIME_DISK = key("extra_slime_disk");
	public static final ResourceKey<PlacedFeature> NETHERRACK_DISK = key("netherrack_disk");
	public static final ResourceKey<PlacedFeature> MAGMATIC_IGNEOUS_DISK = key("magmatic_igneous_disk");
	public static final ResourceKey<PlacedFeature> BLACK_SAND_DISK = key("black_sand_disk");
	public static final ResourceKey<PlacedFeature> PUMICE_STONE_DISK = key("pumice_stone_disk");
	public static final ResourceKey<PlacedFeature> TREATED_PLANKS_DISK = key("treated_planks_disk");
	public static final ResourceKey<PlacedFeature> LACQUERED_PLANKS_DISK = key("lacquered_planks_disk");
	public static final ResourceKey<PlacedFeature> TREATED_UNCARVED_WOOD_DISK = key("treated_uncarved_wood_disk");
	public static final ResourceKey<PlacedFeature> TREATED_CHIPBOARD_DISK = key("treated_chipboard_disk");
	public static final ResourceKey<PlacedFeature> CHIPBOARD_DISK = key("chipboard_disk");
	public static final ResourceKey<PlacedFeature> CARVED_PLANKS_DISK = key("carved_planks_disk");
	public static final ResourceKey<PlacedFeature> COAGULATED_BLOOD_DISK = key("coagulated_blood_disk");
	public static final ResourceKey<PlacedFeature> COBBLESTONE_SURFACE_DISK = key("cobblestone_surface_disk");
	public static final ResourceKey<PlacedFeature> STONE_SURFACE_DISK = key("stone_surface_disk");
	public static final ResourceKey<PlacedFeature> END_GRASS_SURFACE_DISK = key("end_grass_surface_disk");
	public static final ResourceKey<PlacedFeature> END_STONE_SURFACE_DISK = key("end_stone_surface_disk");
	
	//these are similar to the ones in CavePlacements, but with edits to the height at which they generate and count
	public static final ResourceKey<PlacedFeature> DRIPSTONE_CLUSTER = key("dripstone_cluster");
	public static final ResourceKey<PlacedFeature> OCEANIC_DRIPSTONE_CLUSTER = key("oceanic_dripstone_cluster");
	public static final ResourceKey<PlacedFeature> LARGE_DRIPSTONE = key("large_dripstone");
	public static final ResourceKey<PlacedFeature> POINTED_DRIPSTONE = key("pointed_dripstone");
	public static final ResourceKey<PlacedFeature> OCEANIC_POINTED_DRIPSTONE = key("oceanic_pointed_dripstone");
	public static final ResourceKey<PlacedFeature> LUSH_CAVES_VEGETATION = key("lush_caves_vegetation");
	public static final ResourceKey<PlacedFeature> LUSH_CAVES_CEILING_VEGETATION = key("lush_caves_ceiling_vegetation");
	public static final ResourceKey<PlacedFeature> SPARSE_LUSH_CAVES_CEILING_VEGETATION = key("sparse_lush_caves_ceiling_vegetation");
	
	public static final ResourceKey<PlacedFeature> CEILING_ROOTS = key("ceiling_roots");
	
	public static final ResourceKey<PlacedFeature> MESA = key("mesa");
	public static final ResourceKey<PlacedFeature> STONE_MOUND = key("stone_mound");
	public static final ResourceKey<PlacedFeature> COBBLESTONE_BLOCK_BLOB = key("cobblestone_block_blob");
	public static final ResourceKey<PlacedFeature> SANDSTONE_BLOCK_BLOB = key("sandstone_block_blob");
	public static final ResourceKey<PlacedFeature> EXTRA_SANDSTONE_BLOCK_BLOB = key("extra_sandstone_block_blob");
	public static final ResourceKey<PlacedFeature> RED_SANDSTONE_BLOCK_BLOB = key("red_sandstone_block_blob");
	public static final ResourceKey<PlacedFeature> EXTRA_RED_SANDSTONE_BLOCK_BLOB = key("extra_red_sandstone_block_blob");
	public static final ResourceKey<PlacedFeature> RANDOM_ROCK_BLOCK_BLOB = key("random_rock_block_blob");
	public static final ResourceKey<PlacedFeature> LARGE_RANDOM_ROCK_BLOCK_BLOB = key("large_random_rock_block_blob");
	public static final ResourceKey<PlacedFeature> SHADE_STONE_BLOCK_BLOB = key("shade_stone_block_blob");
	public static final ResourceKey<PlacedFeature> FOREST_ROCK = key("forest_rock");
	public static final ResourceKey<PlacedFeature> SMALL_PILLAR = key("pillar/small");
	public static final ResourceKey<PlacedFeature> MIXED_PILLARS = key("pillar/mixed");
	public static final ResourceKey<PlacedFeature> MIXED_PILLARS_EXTRA = key("pillar/mixed_extra");
	public static final ResourceKey<PlacedFeature> ICE_SPIKE = key("ice_spike");
	
	public static final ResourceKey<PlacedFeature> DARK_OAK = key("dark_oak");
	public static final ResourceKey<PlacedFeature> RAINBOW_TREE = key("tree/rainbow");
	public static final ResourceKey<PlacedFeature> EXTRA_RAINBOW_TREE = key("tree/rainbow_extra");
	public static final ResourceKey<PlacedFeature> END_TREE = key("tree/end");
	public static final ResourceKey<PlacedFeature> GLOWING_TREE = key("tree/glowing");
	public static final ResourceKey<PlacedFeature> SHADEWOOD_TREE = key("tree/shadewood");
	public static final ResourceKey<PlacedFeature> SCARRED_SHADEWOOD_TREE = key("tree/scarred_shadewood");
	public static final ResourceKey<PlacedFeature> ORNATE_SHADEWOOD_TREE = key("tree/ornate_shadewood");
	public static final ResourceKey<PlacedFeature> TREE_STUMP = key("tree/tree_stump");
	public static final ResourceKey<PlacedFeature> PETRIFIED_TREE = key("tree/petrified");
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_TREE = key("tree/petrified_sparse");
	public static final ResourceKey<PlacedFeature> DEAD_TREE = key("tree/dead");
	public static final ResourceKey<PlacedFeature> EXTRA_DEAD_TREE = key("tree/dead_extra");
	public static final ResourceKey<PlacedFeature> CINDERED_TREE = key("tree/cindered");
	
	public static final ResourceKey<PlacedFeature> FROST_TREE = key("tree/frost");
	
	public static final ResourceKey<PlacedFeature> BLOOD_TREE = key("tree/blood");
	public static final ResourceKey<PlacedFeature> BREATH_TREE = key("tree/breath");
	public static final ResourceKey<PlacedFeature> DOOM_TREE = key("tree/doom");
	public static final ResourceKey<PlacedFeature> HEART_TREE = key("tree/heart");
	public static final ResourceKey<PlacedFeature> HOPE_TREE = key("tree/hope");
	public static final ResourceKey<PlacedFeature> LIFE_TREE = key("tree/life");
	public static final ResourceKey<PlacedFeature> LIGHT_TREE = key("tree/light");
	public static final ResourceKey<PlacedFeature> MIND_TREE = key("tree/mind");
	public static final ResourceKey<PlacedFeature> RAGE_TREE = key("tree/rage");
	public static final ResourceKey<PlacedFeature> SPACE_TREE = key("tree/space");
	public static final ResourceKey<PlacedFeature> TIME_TREE = key("tree/time");
	public static final ResourceKey<PlacedFeature> VOID_TREE = key("tree/void");
	
	public static final ResourceKey<PlacedFeature> FOREST_LAND_TREES = key("tree/forest_land");
	public static final ResourceKey<PlacedFeature> DENSE_FOREST_LAND_TREES = key("tree/forest_land_dense");
	public static final ResourceKey<PlacedFeature> TAIGA_LAND_TREES = key("tree/taiga_land");
	public static final ResourceKey<PlacedFeature> DENSE_TAIGA_LAND_TREES = key("tree/taiga_land_dense");
	public static final ResourceKey<PlacedFeature> HUGE_MUSHROOMS = key("huge_mushrooms");
	
	public static final ResourceKey<PlacedFeature> SPARSE_JUNGLE_GRASS_PATCH = key("sparse_jungle_grass_patch");
	public static final ResourceKey<PlacedFeature> STRAWBERRY_PATCH = key("strawberry_patch");
	public static final ResourceKey<PlacedFeature> RARE_STRAWBERRY_PATCH = key("rare_strawberry_patch");
	public static final ResourceKey<PlacedFeature> GLOWING_MUSHROOM_PATCH = key("glowing_mushroom_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_GLOWING_MUSHROOM_PATCH = key("sparse_glowing_mushroom_patch");
	public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_PATCH = key("brown_mushroom_patch");
	public static final ResourceKey<PlacedFeature> RED_MUSHROOM_PATCH = key("red_mushroom_patch");
	public static final ResourceKey<PlacedFeature> WOODEN_GRASS_PATCH = key("wooden_grass_patch");
	public static final ResourceKey<PlacedFeature> TREATED_WOODEN_GRASS_PATCH = key("treated_wooden_grass_patch");
	public static final ResourceKey<PlacedFeature> LACQUERED_WOODEN_MUSHROOM_PATCH = key("lacquered_wooden_mushroom");
	public static final ResourceKey<PlacedFeature> CARVED_BUSH_PATCH = key("carved_bush_patch");
	public static final ResourceKey<PlacedFeature> TALL_END_GRASS_PATCH = key("tall_end_grass_patch");
	public static final ResourceKey<PlacedFeature> PETRIFIED_GRASS_PATCH = key("petrified_grass_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_GRASS_PATCH = key("sparse_petrified_grass_patch");
	public static final ResourceKey<PlacedFeature> PETRIFIED_POPPY_PATCH = key("petrified_poppy_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_POPPY_PATCH = key("sparse_petrified_poppy_patch");
	public static final ResourceKey<PlacedFeature> SINGED_GRASS_PATCH = key("singed_grass_patch");
	public static final ResourceKey<PlacedFeature> SINGED_FOLIAGE_PATCH = key("singed_foliage_patch");
	public static final ResourceKey<PlacedFeature> IGNEOUS_SPIKE_PATCH = key("igneous_spike_patch");
	public static final ResourceKey<PlacedFeature> SULFUR_BUBBLE_PATCH = key("sulfur_bubble_patch");
	public static final ResourceKey<PlacedFeature> DESERT_BUSH_PATCH = key("desert_bush_patch");
	public static final ResourceKey<PlacedFeature> SANDY_GRASS_PATCH = key("sandy_grass_patch");
	public static final ResourceKey<PlacedFeature> TALL_SANDY_GRASS_PATCH = key("tall_sandy_grass_patch");
	public static final ResourceKey<PlacedFeature> TALL_DEAD_BUSH_PATCH = key("tall_dead_bush_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_DESERT_BUSH_PATCH = key("sparse_desert_bush_patch");
	public static final ResourceKey<PlacedFeature> MOSS_CARPET_PATCH = key("moss_carpet_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_MOSS_CARPET_PATCH = key("sparse_moss_carpet_patch");
	public static final ResourceKey<PlacedFeature> AZALEA_PATCH = key("azalea_patch");
	public static final ResourceKey<PlacedFeature> BLOOMING_CACTUS_PATCH = key("blooming_cactus_patch");
	public static final ResourceKey<PlacedFeature> WATERLILY_PATCH = key("waterlily_patch");
	public static final ResourceKey<PlacedFeature> CRIMSON_FUNGUS_PATCH = key("crimson_fungus");
	public static final ResourceKey<PlacedFeature> WARPED_FUNGUS_PATCH = key("warped_fungus");
	
	public static final ResourceKey<PlacedFeature> PUMPKIN = key("pumpkin");
	
	public static final ResourceKey<PlacedFeature> RABBIT_PLACEMENT = key("rabbit_placement");
	public static final ResourceKey<PlacedFeature> SMALL_RABBIT_PLACEMENT = key("small_rabbit_placement");
	
	public static final ResourceKey<PlacedFeature> CRUXITE_ORE = key("cruxite_ore");
	public static final ResourceKey<PlacedFeature> URANIUM_ORE = key("uranium_ore");
	
	private static ResourceKey<PlacedFeature> key(String name)
	{
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
	
	public static <T extends FeatureConfiguration> PlacedFeature inline(Feature<T> feature, T config, PlacementModifier... placements)
	{
		return new PlacedFeature(Holder.direct(new ConfiguredFeature<>(feature, config)), List.of(placements));
	}
}