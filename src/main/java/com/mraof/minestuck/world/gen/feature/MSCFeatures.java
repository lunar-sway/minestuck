package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Holds minestuck configured features. Also creates and registers them when appropriate.
 * See {@link MSFeatures} for minestuck features.
 * (Configured features are world-gen features that has been configured with blocks, sizes or whatever else features may be configured with)
 * Check {@link MSPlacedFeatures} for utilizations of these configured features
 */
public final class MSCFeatures
{
	public static final DeferredRegister<ConfiguredFeature<?, ?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> RETURN_NODE = register("return_node", () -> new ConfiguredFeature<>(MSFeatures.RETURN_NODE.get(), FeatureConfiguration.NONE));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_COG = register("small_cog", () -> new ConfiguredFeature<>(MSFeatures.SMALL_COG.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_COG = register("large_cog", () -> new ConfiguredFeature<>(MSFeatures.LARGE_COG.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> COG = register("cog", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(LARGE_COG)), 0.1F)), PlacementUtils.inlinePlaced(BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(SMALL_COG)))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOOR_COG = register("floor_cog", () -> new ConfiguredFeature<>(MSFeatures.FLOOR_COG.get(), FeatureConfiguration.NONE));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_FOSSIL = register("surface_fossil", () -> new ConfiguredFeature<>(MSFeatures.SURFACE_FOSSIL.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> BROKEN_SWORD = register("broken_sword", () -> new ConfiguredFeature<>(MSFeatures.BROKEN_SWORD.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> BUCKET = register("bucket", () -> new ConfiguredFeature<>(MSFeatures.BUCKET.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAKE_PEDESTAL = register("cake_pedestal", () -> new ConfiguredFeature<>(MSFeatures.CAKE_PEDESTAL.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_LIBRARY = register("small_library", () -> new ConfiguredFeature<>(MSFeatures.SMALL_LIBRARY.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> TOWER = register("tower", () -> new ConfiguredFeature<>(MSFeatures.TOWER.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> PARCEL_PYXIS = register("parcel_pyxis", () -> new ConfiguredFeature<>(MSFeatures.PARCEL_PYXIS.get(), FeatureConfiguration.NONE));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_POOL = register("blood_pool", () -> new ConfiguredFeature<>(Feature.LAKE,
			new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.BLOOD.get()), BlockStateProvider.simple(Blocks.AIR))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> OIL_POOL = register("oil_pool", () -> new ConfiguredFeature<>(Feature.LAKE,
			new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.OIL.get()), BlockStateProvider.simple(Blocks.AIR))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> OASIS = register("oasis", () -> new ConfiguredFeature<>(MSFeatures.OASIS.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> OCEAN_RUNDOWN = register("ocean_rundown", () -> new ConfiguredFeature<>(MSFeatures.OCEAN_RUNDOWN.get(), FeatureConfiguration.NONE));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_FIELD = register("fire_field", () -> new ConfiguredFeature<>(MSFeatures.FIRE_FIELD.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> COARSE_DIRT_DISK = register("coarse_dirt_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.COARSE_DIRT), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 4), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_BLOCK_DISK = register("snow_block_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 4), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_SNOW_BLOCK_DISK = register("small_snow_block_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 3), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_DISK = register("ice_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.ICE), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 6), 1)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SAND_DISK = register("sand_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SAND), BlockPredicate.matchesBlocks(Blocks.SANDSTONE, Blocks.SAND), UniformInt.of(2, 5), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_SAND_DISK = register("red_sand_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.RED_SAND), BlockPredicate.matchesBlocks(Blocks.RED_SANDSTONE, Blocks.RED_SAND), UniformInt.of(2, 5), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SLIME_DISK = register("slime_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SLIME_BLOCK), BlockPredicate.matchesBlocks(Blocks.MYCELIUM, Blocks.DIRT), UniformInt.of(2, 6), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> NETHERRACK_DISK = register("netherrack_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.NETHERRACK), BlockPredicate.alwaysTrue(), UniformInt.of(2, 3), 1)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> COAGULATED_BLOOD_DISK = register("coagulated_blood_disk", () -> new ConfiguredFeature<>(MSFeatures.DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.COAGULATED_BLOOD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 5), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> COBBLESTONE_SURFACE_DISK = register("cobblestone_surface_disk", () -> new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.COBBLESTONE), BlockPredicate.matchesBlocks(Blocks.GRAVEL, Blocks.COBBLESTONE), UniformInt.of(2, 5), 1)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_SURFACE_DISK = register("stone_surface_disk", () -> new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.STONE), BlockPredicate.matchesBlocks(Blocks.GRAVEL, Blocks.STONE), UniformInt.of(2, 4), 2)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_GRASS_SURFACE_DISK = register("end_grass_surface_disk", () -> new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.END_GRASS.get()), BlockPredicate.matchesBlocks(Blocks.END_STONE, MSBlocks.END_GRASS.get()), UniformInt.of(2, 4), 1)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_STONE_SURFACE_DISK = register("end_stone_surface_disk", () -> new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
			new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.END_STONE), BlockPredicate.matchesBlocks(MSBlocks.END_GRASS.get(), Blocks.END_STONE), UniformInt.of(2, 3), 1)));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> MESA = register("mesa", () -> new ConfiguredFeature<>(MSFeatures.MESA.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_MOUND = register("stone_mound", () -> new ConfiguredFeature<>(MSFeatures.STONE_MOUND.get(), new BlockStateConfiguration(Blocks.STONE.defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> COBBLESTONE_BLOCK_BLOB = register("cobblestone_block_blob", () -> new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.COBBLESTONE.defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SANDSTONE_BLOCK_BLOB = register("sandstone_block_blob", () -> new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.SANDSTONE.defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_SANDSTONE_BLOCK_BLOB = register("red_sandstone_block_blob", () -> new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.RED_SANDSTONE.defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> RANDOM_ROCK_BLOCK_BLOB = register("random_rock_block_blob", () -> new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(3)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_RANDOM_ROCK_BLOCK_BLOB = register("large_random_rock_block_blob", () -> new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(5)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SHADE_STONE_BLOCK_BLOB = register("shade_stone_block_blob", () -> new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(MSBlocks.SHADE_STONE.get().defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_PILLAR = register("small_pillar", () -> new ConfiguredFeature<>(MSFeatures.SMALL_PILLAR.get(), new BlockStateConfiguration(Blocks.STONE_BRICKS.defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> PILLAR = register("pillar", () -> new ConfiguredFeature<>(MSFeatures.PILLAR.get(), new BlockStateConfiguration(Blocks.STONE_BRICKS.defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> MIXED_PILLARS = register("pillars", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(PILLAR)), 0.4F)), PlacementUtils.inlinePlaced(BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(SMALL_PILLAR)))));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_CAKE = register("large_cake", () -> new ConfiguredFeature<>(MSFeatures.LARGE_CAKE.get(), FeatureConfiguration.NONE));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_TREE = register("rainbow_tree", () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(MSBlocks.RAINBOW_LOG.get().defaultBlockState()), new StraightTrunkPlacer(4, 2, 0),
			BlockStateProvider.simple(MSBlocks.RAINBOW_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
			new TwoLayersFeatureSize(1, 0, 1)).dirt(BlockStateProvider.simple(Blocks.WHITE_WOOL)).ignoreVines().build()));
	public static final ResourceKey<ConfiguredFeature<?, ?>> END_TREE = register("end_tree", () -> new ConfiguredFeature<>(MSFeatures.END_TREE.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWING_TREE = register("glowing_tree", () -> new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
			new BlockStateConfiguration(MSBlocks.GLOWING_LOG.get().defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> SHADEWOOD_TREE = register("shadewood_tree", () -> new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
			new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LOG.get().defaultBlockState(), 10).add(MSBlocks.ROTTED_SHADEWOOD_LOG.get().defaultBlockState(), 1)),
			new StraightTrunkPlacer(4, 1, 1),
			new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)),
			new AcaciaFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 1)),
			new TwoLayersFeatureSize(1, 0, 2))).ignoreVines()
			.decorators(List.of(new AttachedToLeavesDecorator(0.20F, 1, 0, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)), 2, List.of(Direction.DOWN)),
					new AttachedToLeavesDecorator(0.1F, 1, 0, BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState()), 1, List.of(Direction.DOWN)))).build()));
	//scarred variant tends to be a little more scraggly
	public static final ResourceKey<ConfiguredFeature<?, ?>> SCARRED_SHADEWOOD_TREE = register("scarred_shadewood_tree", () -> new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(MSBlocks.SCARRED_SHADEWOOD_LOG.get().defaultBlockState()), new StraightTrunkPlacer(3, 1, 1),
			new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)),
			new AcaciaFoliagePlacer(UniformInt.of(1, 3), UniformInt.of(0, 1)),
			new TwoLayersFeatureSize(1, 0, 2))).ignoreVines()
			.decorators(List.of(new AttachedToLeavesDecorator(0.1F, 1, 0, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)), 2, List.of(Direction.DOWN)),
					new AttachedToLeavesDecorator(0.4F, 1, 0, BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState()), 1, List.of(Direction.DOWN)))).build()));
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORNATE_SHADEWOOD_TREE = register("ornate_shadewood_tree", () -> new ConfiguredFeature<>(MSFeatures.ORNATE_SHADEWOOD_TREE.get(), FeatureConfiguration.NONE));
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_TREE = register("petrified_tree", () -> new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
			new BlockStateConfiguration(MSBlocks.PETRIFIED_LOG.get().defaultBlockState())));
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_TREE = register("dead_tree", () -> new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
			new BlockStateConfiguration(MSBlocks.DEAD_LOG.get().defaultBlockState())));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_LAND_TREES = register("forest_land_trees", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.BIRCH_CHECKED, 0.2F), new WeightedPlacedFeature(TreePlacements.FANCY_OAK_CHECKED, 0.1F)), TreePlacements.OAK_CHECKED)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> TAIGA_LAND_TREES = register("taiga_land_trees", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.PINE_CHECKED, 1 / 3F)), TreePlacements.SPRUCE_CHECKED)));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> STRAWBERRY_PATCH = register("strawberry_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.STRAWBERRY.get())), List.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.MUD), 4)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWING_MUSHROOM_PATCH = register("glowing_mushroom_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState()))))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_END_GRASS_PATCH = register("tall_end_grass_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_END_GRASS.get().defaultBlockState())))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_GRASS_PATCH = register("petrified_grass_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_GRASS.get().defaultBlockState()))))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_POPPY_PATCH = register("petrified_poppy_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_POPPY.get().defaultBlockState()))))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOMING_CACTUS_PATCH = register("blooming_cactus_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.BLOOMING_CACTUS.get()))))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> DESERT_BUSH_PATCH = register("desert_bush_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.DESERT_BUSH.get()))))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_CARPET_PATCH = register("moss_carpet_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MOSS_CARPET.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_PATCH = register("azalea_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.AZALEA.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FUNGUS_PATCH = register("crimson_fungus_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.CRIMSON_FUNGUS.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.MYCELIUM)))));
	public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FUNGUS_PATCH = register("warped_fungus_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.WARPED_FUNGUS.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.MYCELIUM)))));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> PUMPKIN = register("pumpkin", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN))));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> CEILING_ROOTS = register("ceiling_roots", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.HANGING_ROOTS))));
	
	public static final ResourceKey<ConfiguredFeature<?, ?>> RABBIT_PLACEMENT = register("rabbit_placement", () -> new ConfiguredFeature<>(MSFeatures.RABBIT_PLACEMENT.get(), FeatureConfiguration.NONE));
	
	/**
	 * The OreConfiguration uses an inlined list that matches ores to the stone type they should replace
	 */
	public static final ResourceKey<ConfiguredFeature<?, ?>> CRUXITE_ORE = register("cruxite_ore", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
					OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, MSBlocks.STONE_CRUXITE_ORE.get().defaultBlockState())),
					OreGeneration.baseCruxiteVeinSize)));
	public static final ResourceKey<ConfiguredFeature<?, ?>> URANIUM_ORE = register("uranium_ore", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
					OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, MSBlocks.STONE_URANIUM_ORE.get().defaultBlockState()),
					OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, MSBlocks.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())),
					OreGeneration.baseUraniumVeinSize)));
	
	private static ResourceKey<ConfiguredFeature<?, ?>> register(String name, Supplier<ConfiguredFeature<?, ?>> supplier)
	{
		return Objects.requireNonNull(REGISTER.register(name, supplier).getKey());
	}
}