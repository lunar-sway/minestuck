package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class MSPlacedFeatures
{
	public static final DeferredRegister<PlacedFeature> REGISTER = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final ResourceKey<PlacedFeature> RETURN_NODE = register("return_node", () -> placed(MSCFeatures.RETURN_NODE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> COG = register("cog", () -> placed(MSCFeatures.COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2))));
	public static final ResourceKey<PlacedFeature> UNCOMMON_COG = register("uncommon_cog", () -> placed(MSCFeatures.COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(10))));
	public static final ResourceKey<PlacedFeature> FLOOR_COG = register("floor_cog", () -> placed(MSCFeatures.FLOOR_COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(3))));
	public static final ResourceKey<PlacedFeature> UNCOMMON_FLOOR_COG = register("uncommon_floor_cog", () -> placed(MSCFeatures.FLOOR_COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20))));
	
	public static final ResourceKey<PlacedFeature> SURFACE_FOSSIL = register("surface_fossil", () -> placed(MSCFeatures.SURFACE_FOSSIL,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(5))));
	
	public static final ResourceKey<PlacedFeature> BROKEN_SWORD = register("broken_sword", () -> placed(MSCFeatures.BROKEN_SWORD,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(10))));
	public static final ResourceKey<PlacedFeature> UNCOMMON_BROKEN_SWORD = register("uncommon_broken_sword", () -> placed(MSCFeatures.BROKEN_SWORD,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(50))));
	public static final ResourceKey<PlacedFeature> BUCKET = register("bucket", () -> placed(MSCFeatures.BUCKET,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final ResourceKey<PlacedFeature> CAKE_PEDESTAL = register("cake_pedestal", () -> placed(MSCFeatures.CAKE_PEDESTAL,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(100))));
	public static final ResourceKey<PlacedFeature> SMALL_LIBRARY = register("small_library", () -> placed(MSCFeatures.SMALL_LIBRARY,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(64))));
	public static final ResourceKey<PlacedFeature> TOWER = register("tower", () -> placed(MSCFeatures.TOWER,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final ResourceKey<PlacedFeature> PARCEL_PYXIS = register("parcel_pyxis", () -> placed(MSCFeatures.PARCEL_PYXIS,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> LARGE_CAKE = register("large_cake", () -> placed(MSCFeatures.LARGE_CAKE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(50))));
	
	public static final ResourceKey<PlacedFeature> BLOOD_POOL = register("blood_pool", () -> placed(MSCFeatures.BLOOD_POOL,
			worldGenModifiers(CountPlacement.of(5), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> OIL_POOL = register("oil_pool", () -> placed(MSCFeatures.OIL_POOL,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> OASIS = register("oasis", () -> placed(MSCFeatures.OASIS,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> OCEAN_RUNDOWN = register("ocean_rundown", () -> placed(MSCFeatures.OCEAN_RUNDOWN,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> FIRE_FIELD = register("fire_field", () -> placed(MSCFeatures.FIRE_FIELD,
			worldGenModifiers(CountPlacement.of(7), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)))));
	public static final ResourceKey<PlacedFeature> EXTRA_FIRE_FIELD = register("extra_fire_field", () -> placed(MSCFeatures.FIRE_FIELD,
			worldGenModifiers(CountPlacement.of(10), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)))));
	public static final ResourceKey<PlacedFeature> COARSE_DIRT_DISK = register("coarse_dirt_disk", () -> placed(MSCFeatures.COARSE_DIRT_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> SNOW_BLOCK_DISK = register("snow_block_disk", () -> placed(MSCFeatures.SNOW_BLOCK_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> SMALL_SNOW_BLOCK_DISK = register("small_snow_block_disk", () -> placed(MSCFeatures.SMALL_SNOW_BLOCK_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> ICE_DISK = register("ice_disk", () -> placed(MSCFeatures.ICE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(32), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> SAND_DISK = register("sand_disk", () -> placed(MSCFeatures.SAND_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> RED_SAND_DISK = register("red_sand_disk", () -> placed(MSCFeatures.RED_SAND_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> SLIME_DISK = register("slime_disk", () -> placed(MSCFeatures.SLIME_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> EXTRA_SLIME_DISK = register("extra_slime_disk", () -> placed(MSCFeatures.SLIME_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(32), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> NETHERRACK_DISK = register("netherrack_disk", () -> placed(MSCFeatures.NETHERRACK_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> COAGULATED_BLOOD_DISK = register("coagulated_blood_disk", () -> placed(MSCFeatures.COAGULATED_BLOOD_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(56), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> COBBLESTONE_SURFACE_DISK = register("cobblestone_surface_disk", () -> placed(MSCFeatures.COBBLESTONE_SURFACE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> STONE_SURFACE_DISK = register("stone_surface_disk", () -> placed(MSCFeatures.STONE_SURFACE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> END_GRASS_SURFACE_DISK = register("end_grass_surface_disk", () -> placed(MSCFeatures.END_GRASS_SURFACE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(5), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> END_STONE_SURFACE_DISK = register("end_stone_surface_disk", () -> placed(MSCFeatures.END_STONE_SURFACE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	
	//these are similar to the ones in CavePlacements, but with edits to the height at which they generate and count
	public static final ResourceKey<PlacedFeature> DRIPSTONE_CLUSTER = register("dripstone_cluster", () -> placed(CaveFeatures.DRIPSTONE_CLUSTER,
			worldGenModifiers(CountPlacement.of(UniformInt.of(22, 74)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)))));
	public static final ResourceKey<PlacedFeature> OCEANIC_DRIPSTONE_CLUSTER = register("oceanic_dripstone_cluster", () -> placed(CaveFeatures.DRIPSTONE_CLUSTER,
			worldGenModifiers(CountPlacement.of(UniformInt.of(42, 96)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)))));
	public static final ResourceKey<PlacedFeature> LARGE_DRIPSTONE = register("large_dripstone", () -> placed(CaveFeatures.LARGE_DRIPSTONE,
			worldGenModifiers(CountPlacement.of(UniformInt.of(10, 48)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));
	public static final ResourceKey<PlacedFeature> POINTED_DRIPSTONE = register("pointed_dripstone", () -> placed(CaveFeatures.POINTED_DRIPSTONE,
			worldGenModifiers(CountPlacement.of(UniformInt.of(100, 200)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)))));
	public static final ResourceKey<PlacedFeature> OCEANIC_POINTED_DRIPSTONE = register("oceanic_pointed_dripstone", () -> placed(CaveFeatures.POINTED_DRIPSTONE,
			worldGenModifiers(CountPlacement.of(UniformInt.of(164, 256)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)))));
	public static final ResourceKey<PlacedFeature> LUSH_CAVES_VEGETATION = register("lush_caves_vegetation", () -> placed(CaveFeatures.MOSS_PATCH,
			worldGenModifiers(CountPlacement.of(125), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)))));
	public static final ResourceKey<PlacedFeature> LUSH_CAVES_CEILING_VEGETATION = register("lush_caves_ceiling_vegetation", () -> placed(CaveFeatures.MOSS_PATCH_CEILING,
			worldGenModifiers(CountPlacement.of(125), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1)))));
	public static final ResourceKey<PlacedFeature> SPARSE_LUSH_CAVES_CEILING_VEGETATION = register("sparse_lush_caves_ceiling_vegetation", () -> placed(CaveFeatures.MOSS_PATCH_CEILING,
			worldGenModifiers(CountPlacement.of(35), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1)))));
	
	public static final ResourceKey<PlacedFeature> CEILING_ROOTS = register("ceiling_roots", () -> placed(MSCFeatures.CEILING_ROOTS,
			worldGenModifiers(CountPlacement.of(150), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE,1))));
	
	public static final ResourceKey<PlacedFeature> MESA = register("mesa", () -> placed(MSCFeatures.MESA,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(25), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> STONE_MOUND = register("stone_mound", () -> placed(MSCFeatures.STONE_MOUND,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> COBBLESTONE_BLOCK_BLOB = register("cobblestone_block_blob", () -> placed(MSCFeatures.COBBLESTONE_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SANDSTONE_BLOCK_BLOB = register("sandstone_block_blob", () -> placed(MSCFeatures.SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 3)), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> EXTRA_SANDSTONE_BLOCK_BLOB = register("extra_sandstone_block_blob", () -> placed(MSCFeatures.SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 5)), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> RED_SANDSTONE_BLOCK_BLOB = register("red_sandstone_block_blob", () -> placed(MSCFeatures.RED_SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 3)), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> EXTRA_RED_SANDSTONE_BLOCK_BLOB = register("extra_red_sandstone_block_blob", () -> placed(MSCFeatures.RED_SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 5)), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> RANDOM_ROCK_BLOCK_BLOB = register("random_rock_block_blob", () -> placed(MSCFeatures.RANDOM_ROCK_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> LARGE_RANDOM_ROCK_BLOCK_BLOB = register("large_random_rock_block_blob", () -> placed(MSCFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SHADE_STONE_BLOCK_BLOB = register("shade_stone_block_blob", () -> placed(MSCFeatures.SHADE_STONE_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)));
	public static final ResourceKey<PlacedFeature> FOREST_ROCK = register("forest_rock", () -> placed(MiscOverworldFeatures.FOREST_ROCK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> SMALL_PILLAR = register("small_pillar", () -> placed(MSCFeatures.SMALL_PILLAR,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> MIXED_PILLARS = register("mixed_pillars", () -> placed(MSCFeatures.MIXED_PILLARS,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> MIXED_PILLARS_EXTRA = register("mixed_pillars_extra", () -> placed(MSCFeatures.MIXED_PILLARS,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final ResourceKey<PlacedFeature> ICE_SPIKE = register("ice_spike", () -> placed(MiscOverworldFeatures.ICE_SPIKE,
			worldGenModifiers(CountPlacement.of(16), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> DARK_OAK = register("dark_oak", () -> placed(TreeFeatures.DARK_OAK,
			worldGenModifiers(CountPlacement.of(10), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING))));
	public static final ResourceKey<PlacedFeature> RAINBOW_TREE = register("rainbow_tree", () -> placed(MSCFeatures.RAINBOW_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()))));
	public static final ResourceKey<PlacedFeature> EXTRA_RAINBOW_TREE = register("extra_rainbow_tree", () -> placed(MSCFeatures.RAINBOW_TREE,
			worldGenModifiers(PlacementUtils.countExtra(4, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()))));
	public static final ResourceKey<PlacedFeature> END_TREE = register("end_tree", () -> placed(MSCFeatures.END_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.END_SAPLING.get()))));
	public static final ResourceKey<PlacedFeature> GLOWING_TREE = register("glowing_tree", () -> placed(MSCFeatures.GLOWING_TREE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
	public static final ResourceKey<PlacedFeature> SHADEWOOD_TREE = register("shadewood_tree", () -> placed(MSCFeatures.SHADEWOOD_TREE,
			worldGenModifiers(PlacementUtils.countExtra(3, 0.1F, 2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
	public static final ResourceKey<PlacedFeature> SCARRED_SHADEWOOD_TREE = register("scarred_shadewood_tree", () -> placed(MSCFeatures.SCARRED_SHADEWOOD_TREE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
	public static final ResourceKey<PlacedFeature> ORNATE_SHADEWOOD_TREE = register("ornate_shadewood_tree", () -> placed(MSCFeatures.ORNATE_SHADEWOOD_TREE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(6), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
	public static final ResourceKey<PlacedFeature> PETRIFIED_TREE = register("petrified_tree", () -> placed(MSCFeatures.PETRIFIED_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.5F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.PETRIFIED_GRASS.get()))));
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_TREE = register("sparse_petrified_tree", () -> placed(MSCFeatures.PETRIFIED_TREE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.PETRIFIED_GRASS.get()))));
	public static final ResourceKey<PlacedFeature> DEAD_TREE = register("dead_tree", () -> placed(MSCFeatures.DEAD_TREE,
			worldGenModifiers(PlacementUtils.countExtra(1, 0.1F, 1), SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.CHALK.get())))));
	public static final ResourceKey<PlacedFeature> EXTRA_DEAD_TREE = register("extra_dead_tree", () -> placed(MSCFeatures.DEAD_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.CHALK.get())))));
	
	public static final ResourceKey<PlacedFeature> FOREST_LAND_TREES = register("forest_land_trees", () -> placed(MSCFeatures.FOREST_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(5, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> DENSE_FOREST_LAND_TREES = register("dense_forest_land_trees", () -> placed(MSCFeatures.FOREST_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(12, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> TAIGA_LAND_TREES = register("taiga_land_trees", () -> placed(MSCFeatures.TAIGA_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(5, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> DENSE_TAIGA_LAND_TREES = register("dense_taiga_land_trees", () -> placed(MSCFeatures.TAIGA_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(12, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> HUGE_MUSHROOMS = register("huge_mushrooms", () -> placed(VegetationFeatures.MUSHROOM_ISLAND_VEGETATION,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> SPARSE_JUNGLE_GRASS_PATCH = register("sparse_jungle_grass_patch", () -> placed(VegetationFeatures.PATCH_GRASS_JUNGLE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> STRAWBERRY_PATCH = register("strawberry_patch", () -> placed(MSCFeatures.STRAWBERRY_PATCH,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final ResourceKey<PlacedFeature> RARE_STRAWBERRY_PATCH = register("rare_strawberry_patch", () -> placed(MSCFeatures.STRAWBERRY_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final ResourceKey<PlacedFeature> GLOWING_MUSHROOM_PATCH = register("glowing_mushroom_patch", () -> placed(MSCFeatures.GLOWING_MUSHROOM_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(1), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SPARSE_GLOWING_MUSHROOM_PATCH = register("sparse_glowing_mushroom_patch", () -> placed(MSCFeatures.GLOWING_MUSHROOM_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_PATCH = register("brown_mushroom_patch", () -> placed(VegetationFeatures.PATCH_BROWN_MUSHROOM,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> RED_MUSHROOM_PATCH = register("red_mushroom_patch", () -> placed(VegetationFeatures.PATCH_RED_MUSHROOM,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> TALL_END_GRASS_PATCH = register("tall_end_grass_patch", () -> placed(MSCFeatures.TALL_END_GRASS_PATCH,
			worldGenModifiers(PlacementUtils.countExtra(4, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.TALL_END_GRASS.get()))));
	public static final ResourceKey<PlacedFeature> PETRIFIED_GRASS_PATCH = register("petrified_grass_patch", () -> placed(MSCFeatures.PETRIFIED_GRASS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_GRASS_PATCH = register("sparse_petrified_grass_patch", () -> placed(MSCFeatures.PETRIFIED_GRASS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> PETRIFIED_POPPY_PATCH = register("petrified_poppy_patch", () -> placed(MSCFeatures.PETRIFIED_POPPY_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SPARSE_PETRIFIED_POPPY_PATCH = register("sparse_petrified_poppy_patch", () -> placed(MSCFeatures.PETRIFIED_POPPY_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> DESERT_BUSH_PATCH = register("desert_bush_patch", () -> placed(MSCFeatures.DESERT_BUSH_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SPARSE_DESERT_BUSH_PATCH = register("sparse_desert_bush_patch", () -> placed(MSCFeatures.DESERT_BUSH_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> MOSS_CARPET_PATCH = register("moss_carpet_patch", () -> placed(MSCFeatures.MOSS_CARPET_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(6), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SPARSE_MOSS_CARPET_PATCH = register("sparse_moss_carpet_patch", () -> placed(MSCFeatures.MOSS_CARPET_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> AZALEA_PATCH = register("azalea_patch", () -> placed(MSCFeatures.AZALEA_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> BLOOMING_CACTUS_PATCH = register("blooming_cactus_patch", () -> placed(MSCFeatures.BLOOMING_CACTUS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> WATERLILY_PATCH = register("waterlily_patch", () -> placed(VegetationFeatures.PATCH_WATERLILY,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> CRIMSON_FUNGUS_PATCH = register("crimson_fungus", () -> placed(MSCFeatures.CRIMSON_FUNGUS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> WARPED_FUNGUS_PATCH = register("warped_fungus", () -> placed(MSCFeatures.WARPED_FUNGUS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> PUMPKIN = register("pumpkin", () -> placed(MSCFeatures.PUMPKIN,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> RABBIT_PLACEMENT = register("rabbit_placement", () -> placed(MSCFeatures.RABBIT_PLACEMENT,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP)));
	public static final ResourceKey<PlacedFeature> SMALL_RABBIT_PLACEMENT = register("small_rabbit_placement", () -> placed(MSCFeatures.RABBIT_PLACEMENT,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP)));
	
	public static final ResourceKey<PlacedFeature> CRUXITE_ORE = register("cruxite_ore", () -> placed(MSCFeatures.CRUXITE_ORE,
			worldGenModifiers(CountPlacement.of(OreGeneration.cruxiteVeinsPerChunk), HeightRangePlacement.triangle(VerticalAnchor.absolute(OreGeneration.cruxiteStratumMin), VerticalAnchor.absolute(OreGeneration.cruxiteStratumMax)))));
	public static final ResourceKey<PlacedFeature> URANIUM_ORE = register("uranium_ore", () -> placed(MSCFeatures.URANIUM_ORE,
			worldGenModifiers(CountPlacement.of(OreGeneration.uraniumVeinsPerChunk), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreGeneration.uraniumStratumMinAboveBottom), VerticalAnchor.aboveBottom(OreGeneration.uraniumStratumMaxAboveBottom)))));
	
	private static ResourceKey<PlacedFeature> register(String name, Supplier<PlacedFeature> supplier)
	{
		return Objects.requireNonNull(REGISTER.register(name, supplier).getKey());
	}
	
	private static PlacedFeature placed(Holder<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers)
	{
		return new PlacedFeature(Holder.hackyErase(feature), modifiers);
	}
	
	private static PlacedFeature placed(RegistryObject<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers)
	{
		return new PlacedFeature(feature.getHolder().orElseThrow(), modifiers);
	}
	
	private static List<PlacementModifier> singlePlacementModifiers(PlacementModifier... afterSquareModifiers)
	{
		return worldGenModifiers(null, afterSquareModifiers);
	}
	
	private static List<PlacementModifier> worldGenModifiers(@Nullable PlacementModifier frequency, PlacementModifier... afterSquareModifiers)
	{
		ImmutableList.Builder<PlacementModifier> builder = new ImmutableList.Builder<>();
		
		if(frequency != null)
			builder.add(frequency);
		
		builder.add(InSquarePlacement.spread());
		
		for(PlacementModifier modifier : afterSquareModifiers)
			builder.add(modifier);
		
		builder.add(BiomeFilter.biome());
		
		return builder.build();
	}
	
	public static <T extends FeatureConfiguration> PlacedFeature inline(Feature<T> feature, T config, PlacementModifier... placements)
	{
		return new PlacedFeature(Holder.direct(new ConfiguredFeature<>(feature, config)), List.of(placements));
	}
}