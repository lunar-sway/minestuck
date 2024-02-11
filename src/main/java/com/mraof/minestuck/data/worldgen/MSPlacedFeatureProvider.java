package com.mraof.minestuck.data.worldgen;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import com.mraof.minestuck.world.gen.feature.OreGeneration;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
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
import net.minecraft.world.level.levelgen.placement.*;

import javax.annotation.Nullable;
import java.util.List;

import static com.mraof.minestuck.world.gen.feature.MSPlacedFeatures.*;

public final class MSPlacedFeatureProvider
{
	public static void register(BootstapContext<PlacedFeature> context)
	{
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
		
		context.register(RETURN_NODE, placed(features, MSCFeatures.RETURN_NODE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
		
		context.register(COG, placed(features, MSCFeatures.COG,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(2))));
		context.register(UNCOMMON_COG, placed(features, MSCFeatures.COG,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(10))));
		context.register(FLOOR_COG, placed(features, MSCFeatures.FLOOR_COG,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(3))));
		context.register(UNCOMMON_FLOOR_COG, placed(features, MSCFeatures.FLOOR_COG,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20))));
		
		context.register(SURFACE_FOSSIL, placed(features, MSCFeatures.SURFACE_FOSSIL,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(5))));
		
		context.register(BROKEN_SWORD, placed(features, MSCFeatures.BROKEN_SWORD,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(10))));
		context.register(UNCOMMON_BROKEN_SWORD, placed(features, MSCFeatures.BROKEN_SWORD,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(50))));
		context.register(BUCKET, placed(features, MSCFeatures.BUCKET,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
		context.register(CAKE_PEDESTAL, placed(features, MSCFeatures.CAKE_PEDESTAL,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(100))));
		context.register(SMALL_LIBRARY, placed(features, MSCFeatures.SMALL_LIBRARY,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(64))));
		context.register(TOWER, placed(features, MSCFeatures.TOWER,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
		context.register(PARCEL_PYXIS, placed(features, MSCFeatures.PARCEL_PYXIS,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
		context.register(FROG_RUINS, placed(features, MSCFeatures.FROG_RUINS,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)));
		
		context.register(LARGE_CAKE, placed(features, MSCFeatures.LARGE_CAKE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(50))));
		
		context.register(BLOOD_POOL, placed(features, MSCFeatures.BLOOD_POOL,
				worldGenModifiers(CountPlacement.of(5), PlacementUtils.HEIGHTMAP)));
		context.register(OIL_POOL, placed(features, MSCFeatures.OIL_POOL,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP)));
		context.register(OASIS, placed(features, MSCFeatures.OASIS,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
		context.register(OCEAN_RUNDOWN, placed(features, MSCFeatures.OCEAN_RUNDOWN,
				worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP)));
		
		context.register(FIRE_FIELD, placed(features, MSCFeatures.FIRE_FIELD,
				worldGenModifiers(CountPlacement.of(7), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)))));
		context.register(EXTRA_FIRE_FIELD, placed(features, MSCFeatures.FIRE_FIELD,
				worldGenModifiers(CountPlacement.of(10), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)))));
		context.register(COARSE_DIRT_DISK, placed(features, MSCFeatures.COARSE_DIRT_DISK,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(SNOW_BLOCK_DISK, placed(features, MSCFeatures.SNOW_BLOCK_DISK,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(SMALL_SNOW_BLOCK_DISK, placed(features, MSCFeatures.SMALL_SNOW_BLOCK_DISK,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(ICE_DISK, placed(features, MSCFeatures.ICE_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(32), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(SAND_DISK, placed(features, MSCFeatures.SAND_DISK,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(RED_SAND_DISK, placed(features, MSCFeatures.RED_SAND_DISK,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(SLIME_DISK, placed(features, MSCFeatures.SLIME_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(EXTRA_SLIME_DISK, placed(features, MSCFeatures.SLIME_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(32), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(NETHERRACK_DISK, placed(features, MSCFeatures.NETHERRACK_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(COAGULATED_BLOOD_DISK, placed(features, MSCFeatures.COAGULATED_BLOOD_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(56), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(COBBLESTONE_SURFACE_DISK, placed(features, MSCFeatures.COBBLESTONE_SURFACE_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(STONE_SURFACE_DISK, placed(features, MSCFeatures.STONE_SURFACE_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(END_GRASS_SURFACE_DISK, placed(features, MSCFeatures.END_GRASS_SURFACE_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(5), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(END_STONE_SURFACE_DISK, placed(features, MSCFeatures.END_STONE_SURFACE_DISK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		
		//these are similar to the ones in CavePlacements, but with edits to the height at which they generate and count
		context.register(DRIPSTONE_CLUSTER, placed(features, CaveFeatures.DRIPSTONE_CLUSTER,
				worldGenModifiers(CountPlacement.of(UniformInt.of(22, 74)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)))));
		context.register(OCEANIC_DRIPSTONE_CLUSTER, placed(features, CaveFeatures.DRIPSTONE_CLUSTER,
				worldGenModifiers(CountPlacement.of(UniformInt.of(42, 96)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)))));
		context.register(LARGE_DRIPSTONE, placed(features, CaveFeatures.LARGE_DRIPSTONE,
				worldGenModifiers(CountPlacement.of(UniformInt.of(10, 48)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))));
		context.register(POINTED_DRIPSTONE, placed(features, CaveFeatures.POINTED_DRIPSTONE,
				worldGenModifiers(CountPlacement.of(UniformInt.of(100, 200)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)))));
		context.register(OCEANIC_POINTED_DRIPSTONE, placed(features, CaveFeatures.POINTED_DRIPSTONE,
				worldGenModifiers(CountPlacement.of(UniformInt.of(164, 256)), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)))));
		context.register(LUSH_CAVES_VEGETATION, placed(features, CaveFeatures.MOSS_PATCH,
				worldGenModifiers(CountPlacement.of(125), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)))));
		context.register(LUSH_CAVES_CEILING_VEGETATION, placed(features, CaveFeatures.MOSS_PATCH_CEILING,
				worldGenModifiers(CountPlacement.of(125), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1)))));
		context.register(SPARSE_LUSH_CAVES_CEILING_VEGETATION, placed(features, CaveFeatures.MOSS_PATCH_CEILING,
				worldGenModifiers(CountPlacement.of(35), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1)))));
		
		context.register(CEILING_ROOTS, placed(features, MSCFeatures.CEILING_ROOTS,
				worldGenModifiers(CountPlacement.of(150), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE,1))));
		
		context.register(MESA, placed(features, MSCFeatures.MESA,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(25), PlacementUtils.HEIGHTMAP)));
		context.register(STONE_MOUND, placed(features, MSCFeatures.STONE_MOUND,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(COBBLESTONE_BLOCK_BLOB, placed(features, MSCFeatures.COBBLESTONE_BLOCK_BLOB,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
		context.register(SANDSTONE_BLOCK_BLOB, placed(features, MSCFeatures.SANDSTONE_BLOCK_BLOB,
				worldGenModifiers(CountPlacement.of(UniformInt.of(0, 3)), PlacementUtils.HEIGHTMAP)));
		context.register(EXTRA_SANDSTONE_BLOCK_BLOB, placed(features, MSCFeatures.SANDSTONE_BLOCK_BLOB,
				worldGenModifiers(CountPlacement.of(UniformInt.of(0, 5)), PlacementUtils.HEIGHTMAP)));
		context.register(RED_SANDSTONE_BLOCK_BLOB, placed(features, MSCFeatures.RED_SANDSTONE_BLOCK_BLOB,
				worldGenModifiers(CountPlacement.of(UniformInt.of(0, 3)), PlacementUtils.HEIGHTMAP)));
		context.register(EXTRA_RED_SANDSTONE_BLOCK_BLOB, placed(features, MSCFeatures.RED_SANDSTONE_BLOCK_BLOB,
				worldGenModifiers(CountPlacement.of(UniformInt.of(0, 5)), PlacementUtils.HEIGHTMAP)));
		context.register(RANDOM_ROCK_BLOCK_BLOB, placed(features, MSCFeatures.RANDOM_ROCK_BLOCK_BLOB,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP)));
		context.register(LARGE_RANDOM_ROCK_BLOCK_BLOB, placed(features, MSCFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
		context.register(SHADE_STONE_BLOCK_BLOB, placed(features, MSCFeatures.SHADE_STONE_BLOCK_BLOB,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)));
		context.register(FOREST_ROCK, placed(features, MiscOverworldFeatures.FOREST_ROCK,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(SMALL_PILLAR, placed(features, MSCFeatures.SMALL_PILLAR,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(MIXED_PILLARS, placed(features, MSCFeatures.MIXED_PILLARS,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(MIXED_PILLARS_EXTRA, placed(features, MSCFeatures.MIXED_PILLARS,
				worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
		context.register(ICE_SPIKE, placed(features, MiscOverworldFeatures.ICE_SPIKE,
				worldGenModifiers(CountPlacement.of(16), PlacementUtils.HEIGHTMAP)));
		
		context.register(DARK_OAK, placed(features, TreeFeatures.DARK_OAK,
				worldGenModifiers(CountPlacement.of(10), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING))));
		context.register(RAINBOW_TREE, placed(features, MSCFeatures.RAINBOW_TREE,
				worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()))));
		context.register(EXTRA_RAINBOW_TREE, placed(features, MSCFeatures.RAINBOW_TREE,
				worldGenModifiers(PlacementUtils.countExtra(4, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()))));
		context.register(END_TREE, placed(features, MSCFeatures.END_TREE,
				worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.END_SAPLING.get()))));
		context.register(GLOWING_TREE, placed(features, MSCFeatures.GLOWING_TREE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
		context.register(SHADEWOOD_TREE, placed(features, MSCFeatures.SHADEWOOD_TREE,
				worldGenModifiers(PlacementUtils.countExtra(3, 0.1F, 2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
		context.register(SCARRED_SHADEWOOD_TREE, placed(features, MSCFeatures.SCARRED_SHADEWOOD_TREE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
		context.register(ORNATE_SHADEWOOD_TREE, placed(features, MSCFeatures.ORNATE_SHADEWOOD_TREE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(6), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.BLUE_DIRT.get())))));
		context.register(TREE_STUMP, placed(features, MSCFeatures.TREE_STUMP,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(40), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.ROOTED_DIRT)))));
		context.register(PETRIFIED_TREE, placed(features, MSCFeatures.PETRIFIED_TREE,
				worldGenModifiers(PlacementUtils.countExtra(2, 0.5F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.PETRIFIED_GRASS.get()))));
		context.register(SPARSE_PETRIFIED_TREE, placed(features, MSCFeatures.PETRIFIED_TREE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.PETRIFIED_GRASS.get()))));
		context.register(DEAD_TREE, placed(features, MSCFeatures.DEAD_TREE,
				worldGenModifiers(PlacementUtils.countExtra(1, 0.1F, 1), SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.CHALK.get())))));
		context.register(EXTRA_DEAD_TREE, placed(features, MSCFeatures.DEAD_TREE,
				worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), MSBlocks.CHALK.get())))));
		
		context.register(FOREST_LAND_TREES, placed(features, MSCFeatures.FOREST_LAND_TREES,
				worldGenModifiers(PlacementUtils.countExtra(5, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
		context.register(DENSE_FOREST_LAND_TREES, placed(features, MSCFeatures.FOREST_LAND_TREES,
				worldGenModifiers(PlacementUtils.countExtra(12, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
		context.register(TAIGA_LAND_TREES, placed(features, MSCFeatures.TAIGA_LAND_TREES,
				worldGenModifiers(PlacementUtils.countExtra(5, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
		context.register(DENSE_TAIGA_LAND_TREES, placed(features, MSCFeatures.TAIGA_LAND_TREES,
				worldGenModifiers(PlacementUtils.countExtra(12, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
		context.register(HUGE_MUSHROOMS, placed(features, VegetationFeatures.MUSHROOM_ISLAND_VEGETATION,
				worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP)));
		
		context.register(SPARSE_JUNGLE_GRASS_PATCH, placed(features, VegetationFeatures.PATCH_GRASS_JUNGLE,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
		context.register(STRAWBERRY_PATCH, placed(features, MSCFeatures.STRAWBERRY_PATCH,
				singlePlacementModifiers(PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
		context.register(RARE_STRAWBERRY_PATCH, placed(features, MSCFeatures.STRAWBERRY_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
		context.register(GLOWING_MUSHROOM_PATCH, placed(features, MSCFeatures.GLOWING_MUSHROOM_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(1), PlacementUtils.HEIGHTMAP)));
		context.register(SPARSE_GLOWING_MUSHROOM_PATCH, placed(features, MSCFeatures.GLOWING_MUSHROOM_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP)));
		context.register(BROWN_MUSHROOM_PATCH, placed(features, VegetationFeatures.PATCH_BROWN_MUSHROOM,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
		context.register(RED_MUSHROOM_PATCH, placed(features, VegetationFeatures.PATCH_RED_MUSHROOM,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
		context.register(TALL_END_GRASS_PATCH, placed(features, MSCFeatures.TALL_END_GRASS_PATCH,
				worldGenModifiers(PlacementUtils.countExtra(4, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.TALL_END_GRASS.get()))));
		context.register(PETRIFIED_GRASS_PATCH, placed(features, MSCFeatures.PETRIFIED_GRASS_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
		context.register(SPARSE_PETRIFIED_GRASS_PATCH, placed(features, MSCFeatures.PETRIFIED_GRASS_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
		context.register(PETRIFIED_POPPY_PATCH, placed(features, MSCFeatures.PETRIFIED_POPPY_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
		context.register(SPARSE_PETRIFIED_POPPY_PATCH, placed(features, MSCFeatures.PETRIFIED_POPPY_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
		context.register(DESERT_BUSH_PATCH, placed(features, MSCFeatures.DESERT_BUSH_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
		context.register(SPARSE_DESERT_BUSH_PATCH, placed(features, MSCFeatures.DESERT_BUSH_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
		context.register(MOSS_CARPET_PATCH, placed(features, MSCFeatures.MOSS_CARPET_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(6), PlacementUtils.HEIGHTMAP)));
		context.register(SPARSE_MOSS_CARPET_PATCH, placed(features, MSCFeatures.MOSS_CARPET_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP)));
		context.register(AZALEA_PATCH, placed(features, MSCFeatures.AZALEA_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP)));
		context.register(BLOOMING_CACTUS_PATCH, placed(features, MSCFeatures.BLOOMING_CACTUS_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
		context.register(WATERLILY_PATCH, placed(features, VegetationFeatures.PATCH_WATERLILY,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
		context.register(CRIMSON_FUNGUS_PATCH, placed(features, MSCFeatures.CRIMSON_FUNGUS_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP)));
		context.register(WARPED_FUNGUS_PATCH, placed(features, MSCFeatures.WARPED_FUNGUS_PATCH,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP)));
		
		context.register(PUMPKIN, placed(features, MSCFeatures.PUMPKIN,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
		
		context.register(RABBIT_PLACEMENT, placed(features, MSCFeatures.RABBIT_PLACEMENT,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP)));
		context.register(SMALL_RABBIT_PLACEMENT, placed(features, MSCFeatures.RABBIT_PLACEMENT,
				worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP)));
		
		context.register(CRUXITE_ORE, placed(features, MSCFeatures.CRUXITE_ORE,
				worldGenModifiers(CountPlacement.of(OreGeneration.cruxiteVeinsPerChunk), HeightRangePlacement.triangle(VerticalAnchor.absolute(OreGeneration.cruxiteStratumMin), VerticalAnchor.absolute(OreGeneration.cruxiteStratumMax)))));
		context.register(URANIUM_ORE, placed(features, MSCFeatures.URANIUM_ORE,
				worldGenModifiers(CountPlacement.of(OreGeneration.uraniumVeinsPerChunk), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreGeneration.uraniumStratumMinAboveBottom), VerticalAnchor.aboveBottom(OreGeneration.uraniumStratumMaxAboveBottom)))));
		
	}
	
	private static PlacedFeature placed(HolderGetter<ConfiguredFeature<?, ?>> registry, ResourceKey<ConfiguredFeature<?, ?>> key, List<PlacementModifier> modifiers)
	{
		return new PlacedFeature(registry.getOrThrow(key), modifiers);
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
}
