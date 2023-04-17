package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
	
	public static final ResourceKey<PlacedFeature> COG = key("cog");
	public static final ResourceKey<PlacedFeature> UNCOMMON_COG = key("uncommon_cog");
	public static final ResourceKey<PlacedFeature> FLOOR_COG = key("floor_cog");
	public static final ResourceKey<PlacedFeature> UNCOMMON_FLOOR_COG = key("uncommon_floor_cog");
	
	public static final ResourceKey<PlacedFeature> SURFACE_FOSSIL = key("surface_fossil");
	
	public static final ResourceKey<PlacedFeature> BROKEN_SWORD = key("broken_sword");
	public static final ResourceKey<PlacedFeature> UNCOMMON_BROKEN_SWORD = key("uncommon_broken_sword");
	public static final ResourceKey<PlacedFeature> BUCKET = key("bucket");
	public static final ResourceKey<PlacedFeature> CAKE_PEDESTAL = key("cake_pedestal");
	public static final ResourceKey<PlacedFeature> SMALL_LIBRARY = key("small_library");
	public static final ResourceKey<PlacedFeature> TOWER = key("tower");
	public static final ResourceKey<PlacedFeature> PARCEL_PYXIS = key("parcel_pyxis");
	
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
	public static final ResourceKey<PlacedFeature> SMALL_PILLAR = key("small_pillar");
	public static final ResourceKey<PlacedFeature> MIXED_PILLARS = key("mixed_pillars");
	public static final ResourceKey<PlacedFeature> MIXED_PILLARS_EXTRA = key("mixed_pillars_extra");
	public static final ResourceKey<PlacedFeature> ICE_SPIKE = key("ice_spike");
	
	public static final ResourceKey<PlacedFeature> DARK_OAK = key("dark_oak");
	public static final ResourceKey<PlacedFeature> RAINBOW_TREE = key("rainbow_tree");
	public static final ResourceKey<PlacedFeature> EXTRA_RAINBOW_TREE = key("extra_rainbow_tree");
	public static final ResourceKey<PlacedFeature> END_TREE = key("end_tree");
	public static final ResourceKey<PlacedFeature> GLOWING_TREE = key("glowing_tree");
	public static final ResourceKey<PlacedFeature> SHADEWOOD_TREE = key("shadewood_tree");
	public static final ResourceKey<PlacedFeature> SCARRED_SHADEWOOD_TREE = key("scarred_shadewood_tree");
	public static final ResourceKey<PlacedFeature> ORNATE_SHADEWOOD_TREE = key("ornate_shadewood_tree");
	public static final ResourceKey<PlacedFeature> PETRIFIED_TREE = key("petrified_tree");
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_TREE = key("sparse_petrified_tree");
	public static final ResourceKey<PlacedFeature> DEAD_TREE = key("dead_tree");
	public static final ResourceKey<PlacedFeature> EXTRA_DEAD_TREE = key("extra_dead_tree");
	
	public static final ResourceKey<PlacedFeature> FOREST_LAND_TREES = key("forest_land_trees");
	public static final ResourceKey<PlacedFeature> DENSE_FOREST_LAND_TREES = key("dense_forest_land_trees");
	public static final ResourceKey<PlacedFeature> TAIGA_LAND_TREES = key("taiga_land_trees");
	public static final ResourceKey<PlacedFeature> DENSE_TAIGA_LAND_TREES = key("dense_taiga_land_trees");
	public static final ResourceKey<PlacedFeature> HUGE_MUSHROOMS = key("huge_mushrooms");
	
	public static final ResourceKey<PlacedFeature> SPARSE_JUNGLE_GRASS_PATCH = key("sparse_jungle_grass_patch");
	public static final ResourceKey<PlacedFeature> STRAWBERRY_PATCH = key("strawberry_patch");
	public static final ResourceKey<PlacedFeature> RARE_STRAWBERRY_PATCH = key("rare_strawberry_patch");
	public static final ResourceKey<PlacedFeature> GLOWING_MUSHROOM_PATCH = key("glowing_mushroom_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_GLOWING_MUSHROOM_PATCH = key("sparse_glowing_mushroom_patch");
	public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_PATCH = key("brown_mushroom_patch");
	public static final ResourceKey<PlacedFeature> RED_MUSHROOM_PATCH = key("red_mushroom_patch");
	public static final ResourceKey<PlacedFeature> TALL_END_GRASS_PATCH = key("tall_end_grass_patch");
	public static final ResourceKey<PlacedFeature> PETRIFIED_GRASS_PATCH = key("petrified_grass_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_GRASS_PATCH = key("sparse_petrified_grass_patch");
	public static final ResourceKey<PlacedFeature> PETRIFIED_POPPY_PATCH = key("petrified_poppy_patch");
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_POPPY_PATCH = key("sparse_petrified_poppy_patch");
	public static final ResourceKey<PlacedFeature> DESERT_BUSH_PATCH = key("desert_bush_patch");
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
		return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, name));
	}
	
	public static <T extends FeatureConfiguration> PlacedFeature inline(Feature<T> feature, T config, PlacementModifier... placements)
	{
		return new PlacedFeature(Holder.direct(new ConfiguredFeature<>(feature, config)), List.of(placements));
	}
}