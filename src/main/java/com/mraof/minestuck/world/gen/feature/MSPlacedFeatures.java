package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public final class MSPlacedFeatures
{
	public static final DeferredRegister<PlacedFeature> REGISTER = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<PlacedFeature> RETURN_NODE = REGISTER.register("return_node", () -> placed(MSCFeatures.RETURN_NODE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> COG = REGISTER.register("cog", () -> placed(MSCFeatures.COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2))));
	public static final RegistryObject<PlacedFeature> UNCOMMON_COG = REGISTER.register("uncommon_cog", () -> placed(MSCFeatures.COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(10))));
	public static final RegistryObject<PlacedFeature> FLOOR_COG = REGISTER.register("floor_cog", () -> placed(MSCFeatures.FLOOR_COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(3))));
	public static final RegistryObject<PlacedFeature> UNCOMMON_FLOOR_COG = REGISTER.register("uncommon_floor_cog", () -> placed(MSCFeatures.FLOOR_COG,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20))));
	
	public static final RegistryObject<PlacedFeature> SURFACE_FOSSIL = REGISTER.register("surface_fossil", () -> placed(MSCFeatures.SURFACE_FOSSIL,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(5))));
	
	public static final RegistryObject<PlacedFeature> BROKEN_SWORD = REGISTER.register("broken_sword", () -> placed(MSCFeatures.BROKEN_SWORD,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(10))));
	public static final RegistryObject<PlacedFeature> UNCOMMON_BROKEN_SWORD = REGISTER.register("uncommon_broken_sword", () -> placed(MSCFeatures.BROKEN_SWORD,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(50))));
	public static final RegistryObject<PlacedFeature> BUCKET = REGISTER.register("bucket", () -> placed(MSCFeatures.BUCKET,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(16), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final RegistryObject<PlacedFeature> CAKE_PEDESTAL = REGISTER.register("cake_pedestal", () -> placed(MSCFeatures.CAKE_PEDESTAL,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(100))));
	public static final RegistryObject<PlacedFeature> SMALL_LIBRARY = REGISTER.register("small_library", () -> placed(MSCFeatures.SMALL_LIBRARY,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(64))));
	public static final RegistryObject<PlacedFeature> TOWER = REGISTER.register("tower", () -> placed(MSCFeatures.TOWER,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final RegistryObject<PlacedFeature> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> placed(MSCFeatures.PARCEL_PYXIS,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> BLOOD_POOL = REGISTER.register("blood_pool", () -> placed(MSCFeatures.BLOOD_POOL,
			worldGenModifiers(CountPlacement.of(5), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> OASIS = REGISTER.register("oasis", () -> placed(MSCFeatures.OASIS,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> OCEAN_RUNDOWN = REGISTER.register("ocean_rundown", () -> placed(MSCFeatures.OCEAN_RUNDOWN,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> FIRE_FIELD = REGISTER.register("fire_field", () -> placed(MSCFeatures.FIRE_FIELD,
			worldGenModifiers(CountPlacement.of(7), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)))));
	public static final RegistryObject<PlacedFeature> EXTRA_FIRE_FIELD = REGISTER.register("extra_fire_field", () -> placed(MSCFeatures.FIRE_FIELD,
			worldGenModifiers(CountPlacement.of(10), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)))));
	public static final RegistryObject<PlacedFeature> COARSE_DIRT_DISK = REGISTER.register("coarse_dirt_disk", () -> placed(MSCFeatures.COARSE_DIRT_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> SNOW_BLOCK_DISK = REGISTER.register("snow_block_disk", () -> placed(MSCFeatures.SNOW_BLOCK_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> SMALL_SNOW_BLOCK_DISK = REGISTER.register("small_snow_block_disk", () -> placed(MSCFeatures.SMALL_SNOW_BLOCK_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> ICE_DISK = REGISTER.register("ice_disk", () -> placed(MSCFeatures.ICE_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> SAND_DISK = REGISTER.register("sand_disk", () -> placed(MSCFeatures.SAND_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> RED_SAND_DISK = REGISTER.register("red_sand_disk", () -> placed(MSCFeatures.RED_SAND_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> SLIME_DISK = REGISTER.register("slime_disk", () -> placed(MSCFeatures.SLIME_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> EXTRA_SLIME_DISK = REGISTER.register("extra_slime_disk", () -> placed(MSCFeatures.SLIME_DISK,
			worldGenModifiers(CountPlacement.of(2), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> NETHERRACK_DISK = REGISTER.register("netherrack_disk", () -> placed(MSCFeatures.NETHERRACK_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> OAK_LEAVES_DISK = REGISTER.register("oak_leaves_disk", () -> placed(MSCFeatures.OAK_LEAVES_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> COAGULATED_BLOOD_DISK = REGISTER.register("coagulated_blood_disk", () -> placed(MSCFeatures.COAGULATED_BLOOD_DISK,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> COBBLESTONE_SURFACE_DISK = REGISTER.register("cobblestone_surface_disk", () -> placed(MSCFeatures.COBBLESTONE_SURFACE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> STONE_SURFACE_DISK = REGISTER.register("stone_surface_disk", () -> placed(MSCFeatures.STONE_SURFACE_DISK,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> END_GRASS_SURFACE_DISK = REGISTER.register("end_grass_surface_disk", () -> placed(MSCFeatures.END_GRASS_SURFACE_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> END_STONE_SURFACE_DISK = REGISTER.register("end_stone_surface_disk", () -> placed(MSCFeatures.END_STONE_SURFACE_DISK,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	
	public static final RegistryObject<PlacedFeature> MESA = REGISTER.register("mesa", () -> placed(MSCFeatures.MESA,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(25), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> STONE_MOUND = REGISTER.register("stone_mound", () -> placed(MSCFeatures.STONE_MOUND,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> COBBLESTONE_BLOCK_BLOB = REGISTER.register("cobblestone_block_blob", () -> placed(MSCFeatures.COBBLESTONE_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> SANDSTONE_BLOCK_BLOB = REGISTER.register("sandstone_block_blob", () -> placed(MSCFeatures.SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 3)), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> EXTRA_SANDSTONE_BLOCK_BLOB = REGISTER.register("extra_sandstone_block_blob", () -> placed(MSCFeatures.SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 5)), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> RED_SANDSTONE_BLOCK_BLOB = REGISTER.register("red_sandstone_block_blob", () -> placed(MSCFeatures.RED_SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 3)), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> EXTRA_RED_SANDSTONE_BLOCK_BLOB = REGISTER.register("extra_red_sandstone_block_blob", () -> placed(MSCFeatures.RED_SANDSTONE_BLOCK_BLOB,
			worldGenModifiers(CountPlacement.of(UniformInt.of(0, 5)), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("random_rock_block_blob", () -> placed(MSCFeatures.RANDOM_ROCK_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> LARGE_RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("large_random_rock_block_blob", () -> placed(MSCFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> PILLAR = REGISTER.register("pillar", () -> placed(MSCFeatures.PILLAR,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> LARGE_PILLAR = REGISTER.register("large_pillar", () -> placed(MSCFeatures.LARGE_PILLAR,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	public static final RegistryObject<PlacedFeature> LARGE_PILLAR_EXTRA = REGISTER.register("large_pillar_extra", () -> placed(MSCFeatures.LARGE_PILLAR,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	
	public static final RegistryObject<PlacedFeature> DARK_OAK = REGISTER.register("dark_oak", () -> placed(TreeFeatures.DARK_OAK,
			worldGenModifiers(CountPlacement.of(10), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING))));
	public static final RegistryObject<PlacedFeature> RAINBOW_TREE = REGISTER.register("rainbow_tree", () -> placed(MSCFeatures.RAINBOW_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()))));
	public static final RegistryObject<PlacedFeature> EXTRA_RAINBOW_TREE = REGISTER.register("extra_rainbow_tree", () -> placed(MSCFeatures.RAINBOW_TREE,
			worldGenModifiers(PlacementUtils.countExtra(4, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.RAINBOW_SAPLING.get()))));
	public static final RegistryObject<PlacedFeature> END_TREE = REGISTER.register("end_tree", () -> placed(MSCFeatures.END_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.END_SAPLING.get()))));
	public static final RegistryObject<PlacedFeature> GLOWING_TREE = REGISTER.register("glowing_tree", () -> placed(MSCFeatures.GLOWING_TREE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlock(MSBlocks.BLUE_DIRT.get(), Vec3i.ZERO.below())))));
	public static final RegistryObject<PlacedFeature> PETRIFIED_TREE = REGISTER.register("petrified_tree", () -> placed(MSCFeatures.PETRIFIED_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.5F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.PETRIFIED_GRASS.get()))));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_TREE = REGISTER.register("sparse_petrified_tree", () -> placed(MSCFeatures.PETRIFIED_TREE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(20), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.PETRIFIED_GRASS.get()))));
	public static final RegistryObject<PlacedFeature> DEAD_TREE = REGISTER.register("dead_tree", () -> placed(MSCFeatures.DEAD_TREE,
			worldGenModifiers(PlacementUtils.countExtra(1, 0.1F, 1), SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlock(MSBlocks.CHALK.get(), Vec3i.ZERO.below())))));
	public static final RegistryObject<PlacedFeature> EXTRA_DEAD_TREE = REGISTER.register("extra_dead_tree", () -> placed(MSCFeatures.DEAD_TREE,
			worldGenModifiers(PlacementUtils.countExtra(2, 0.1F, 1), SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlock(MSBlocks.CHALK.get(), Vec3i.ZERO.below())))));
	
	public static final RegistryObject<PlacedFeature> FOREST_LAND_TREES = REGISTER.register("forest_land_trees", () -> placed(MSCFeatures.FOREST_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(5, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> DENSE_FOREST_LAND_TREES = REGISTER.register("dense_forest_land_trees", () -> placed(MSCFeatures.FOREST_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(10, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> TAIGA_LAND_TREES = REGISTER.register("taiga_land_trees", () -> placed(MSCFeatures.TAIGA_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(5, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> DENSE_TAIGA_LAND_TREES = REGISTER.register("dense_taiga_land_trees", () -> placed(MSCFeatures.TAIGA_LAND_TREES,
			worldGenModifiers(PlacementUtils.countExtra(10, 0.1F, 1), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> HUGE_MUSHROOMS = REGISTER.register("huge_mushrooms", () -> placed(VegetationFeatures.MUSHROOM_ISLAND_VEGETATION,
			worldGenModifiers(CountPlacement.of(3), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> SPARSE_JUNGLE_GRASS_PATCH = REGISTER.register("sparse_jungle_grass_patch", () -> placed(VegetationFeatures.PATCH_GRASS_JUNGLE,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> STRAWBERRY_PATCH = REGISTER.register("strawberry_patch", () -> placed(MSCFeatures.STRAWBERRY_PATCH,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final RegistryObject<PlacedFeature> RARE_STRAWBERRY_PATCH = REGISTER.register("rare_strawberry_patch", () -> placed(MSCFeatures.STRAWBERRY_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
	public static final RegistryObject<PlacedFeature> GLOWING_MUSHROOM_PATCH = REGISTER.register("glowing_mushroom_patch", () -> placed(MSCFeatures.GLOWING_MUSHROOM_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> SPARSE_GLOWING_MUSHROOM_PATCH = REGISTER.register("sparse_glowing_mushroom_patch", () -> placed(MSCFeatures.GLOWING_MUSHROOM_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> BROWN_MUSHROOM_PATCH = REGISTER.register("brown_mushroom_patch", () -> placed(VegetationFeatures.PATCH_BROWN_MUSHROOM,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> RED_MUSHROOM_PATCH = REGISTER.register("red_mushroom_patch", () -> placed(VegetationFeatures.PATCH_RED_MUSHROOM,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> TALL_END_GRASS_PATCH = REGISTER.register("tall_end_grass_patch", () -> placed(MSCFeatures.TALL_END_GRASS_PATCH,
			worldGenModifiers(PlacementUtils.countExtra(4, 0.1F, 1), PlacementUtils.HEIGHTMAP, PlacementUtils.filteredByBlockSurvival(MSBlocks.TALL_END_GRASS.get()))));
	public static final RegistryObject<PlacedFeature> PETRIFIED_GRASS_PATCH = REGISTER.register("petrified_grass_patch", () -> placed(MSCFeatures.PETRIFIED_GRASS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_GRASS_PATCH = REGISTER.register("sparse_petrified_grass_patch", () -> placed(MSCFeatures.PETRIFIED_GRASS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> PETRIFIED_POPPY_PATCH = REGISTER.register("petrified_poppy_patch", () -> placed(MSCFeatures.PETRIFIED_POPPY_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> SPARSE_PETRIFIED_POPPY_PATCH = REGISTER.register("sparse_petrified_poppy_patch", () -> placed(MSCFeatures.PETRIFIED_POPPY_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> DESERT_BUSH_PATCH = REGISTER.register("desert_bush_patch", () -> placed(MSCFeatures.DESERT_BUSH_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> SPARSE_DESERT_BUSH_PATCH = REGISTER.register("sparse_desert_bush_patch", () -> placed(MSCFeatures.DESERT_BUSH_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(120), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> BLOOMING_CACTUS_PATCH = REGISTER.register("blooming_cactus_patch", () -> placed(MSCFeatures.BLOOMING_CACTUS_PATCH,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> WATERLILY_PATCH = REGISTER.register("waterlily_patch", () -> placed(VegetationFeatures.PATCH_WATERLILY,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(60), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> PUMPKIN = REGISTER.register("pumpkin", () -> placed(MSCFeatures.PUMPKIN,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(128), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> RABBIT_PLACEMENT = REGISTER.register("rabbit_placement", () -> placed(MSCFeatures.RABBIT_PLACEMENT,
			singlePlacementModifiers(PlacementUtils.HEIGHTMAP)));
	public static final RegistryObject<PlacedFeature> SMALL_RABBIT_PLACEMENT = REGISTER.register("small_rabbit_placement", () -> placed(MSCFeatures.RABBIT_PLACEMENT,
			worldGenModifiers(RarityFilter.onAverageOnceEvery(2), PlacementUtils.HEIGHTMAP)));
	
	public static final RegistryObject<PlacedFeature> CRUXITE_ORE = REGISTER.register("cruxite_ore", () -> placed(MSCFeatures.CRUXITE_ORE,
			worldGenModifiers(CountPlacement.of(OreGeneration.cruxiteVeinsPerChunk), HeightRangePlacement.triangle(VerticalAnchor.absolute(OreGeneration.cruxiteStratumMin), VerticalAnchor.absolute(OreGeneration.cruxiteStratumMax)))));
	public static final RegistryObject<PlacedFeature> URANIUM_ORE = REGISTER.register("uranium_ore", () -> placed(MSCFeatures.URANIUM_ORE,
			worldGenModifiers(CountPlacement.of(OreGeneration.uraniumVeinsPerChunk), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreGeneration.uraniumStratumMinAboveBottom), VerticalAnchor.aboveBottom(OreGeneration.uraniumStratumMaxAboveBottom)))));
	
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
}