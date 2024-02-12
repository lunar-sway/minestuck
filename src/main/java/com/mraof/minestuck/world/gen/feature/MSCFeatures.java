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
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_CHERRY_TREE = key("carved_cherry_tree");
	
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
	public static final ResourceKey<ConfiguredFeature<?, ?>> CARVED_PLANKS_DISK = key("carved_planks_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LACQUERED_PLANKS_DISK = key("lacquered_planks_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_UNCARVED_WOOD_DISK = key("treated_uncarved_wood_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_CHIPBOARD_DISK = key("treated_chipboard_disk");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UNCARVED_WOOD_DISK = key("uncarved_wood_disk");
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
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_TREE = key("petrified_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_TREE = key("dead_tree");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_LAND_TREES = key("forest_land_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TAIGA_LAND_TREES = key("taiga_land_trees");
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> STRAWBERRY_PATCH = key("strawberry_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWING_MUSHROOM_PATCH = key("glowing_mushroom_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WOODEN_GRASS_PATCH = key("wooden_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREATED_WOODEN_GRASS_PATCH = key("treated_wooden_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LACQUERED_WOODEN_MUSHROOM_PATCH = key("lacquered_wooden_mushroom_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_END_GRASS_PATCH = key("tall_end_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_GRASS_PATCH = key("petrified_grass_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_POPPY_PATCH = key("petrified_poppy_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOMING_CACTUS_PATCH = key("blooming_cactus_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DESERT_BUSH_PATCH = key("desert_bush_patch");
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