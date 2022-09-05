package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.OreGeneration;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * Holds minestuck configured features. Also creates and registers them when appropriate.
 * See {@link MSFeatures} for minestuck features.
 * (Configured features are world-gen features that has been configured with blocks, sizes or whatever else features may be configured with)
 * Check {@link MSPlacedFeatures} for utilizations of these configured features
 */
public final class MSCFeatures
{
	public static final DeferredRegister<ConfiguredFeature<?, ?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> RETURN_NODE = REGISTER.register("return_node", () -> new ConfiguredFeature<>(MSFeatures.RETURN_NODE.get(), FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> SMALL_COG = REGISTER.register("small_cog", () -> new ConfiguredFeature<>(MSFeatures.SMALL_COG.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> LARGE_COG = REGISTER.register("large_cog", () -> new ConfiguredFeature<>(MSFeatures.LARGE_COG.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> COG = REGISTER.register("cog", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(LARGE_COG.getHolder().orElseThrow()), 0.1F)), PlacementUtils.inlinePlaced(SMALL_COG.getHolder().orElseThrow()))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> FLOOR_COG = REGISTER.register("floor_cog", () -> new ConfiguredFeature<>(MSFeatures.FLOOR_COG.get(), FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> SURFACE_FOSSIL = REGISTER.register("surface_fossil", () -> new ConfiguredFeature<>(MSFeatures.SURFACE_FOSSIL.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> BROKEN_SWORD = REGISTER.register("broken_sword", () -> new ConfiguredFeature<>(MSFeatures.BROKEN_SWORD.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> BUCKET = REGISTER.register("bucket", () -> new ConfiguredFeature<>(MSFeatures.BUCKET.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> CAKE_PEDESTAL = REGISTER.register("cake_pedestal", () -> new ConfiguredFeature<>(MSFeatures.CAKE_PEDESTAL.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> SMALL_LIBRARY = REGISTER.register("small_library", () -> new ConfiguredFeature<>(MSFeatures.SMALL_LIBRARY.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> TOWER = REGISTER.register("tower", () -> new ConfiguredFeature<>(MSFeatures.TOWER.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new ConfiguredFeature<>(MSFeatures.PARCEL_PYXIS.get(), FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> BLOOD_POOL = REGISTER.register("blood_pool", () -> new ConfiguredFeature<>(Feature.LAKE,
			new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.BLOOD.get()), BlockStateProvider.simple(Blocks.AIR))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> OASIS = REGISTER.register("oasis", () -> new ConfiguredFeature<>(MSFeatures.OASIS.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> OCEAN_RUNDOWN = REGISTER.register("ocean_rundown", () -> new ConfiguredFeature<>(MSFeatures.OCEAN_RUNDOWN.get(), FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> FIRE_FIELD = REGISTER.register("fire_field", () -> new ConfiguredFeature<>(MSFeatures.FIRE_FIELD.get(), FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> MESA = REGISTER.register("mesa", () -> new ConfiguredFeature<>(MSFeatures.MESA.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> COBBLESTONE_BLOCK_BLOB = REGISTER.register("cobblestone_block_blob", () -> new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.COBBLESTONE.defaultBlockState())));
	public static final RegistryObject<ConfiguredFeature<?, ?>> RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("random_rock_block_blob", () -> new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(3)));
	public static final RegistryObject<ConfiguredFeature<?, ?>> LARGE_RANDOM_ROCK_BLOCK_BLOB = REGISTER.register("large_random_rock_block_blob", () -> new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(5)));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> RAINBOW_TREE = REGISTER.register("rainbow_tree", () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(MSBlocks.RAINBOW_LOG.get().defaultBlockState()), new StraightTrunkPlacer(4, 2, 0),
			BlockStateProvider.simple(MSBlocks.RAINBOW_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
			new TwoLayersFeatureSize(1, 0, 1)).dirt(BlockStateProvider.simple(Blocks.WHITE_WOOL)).ignoreVines().build()));
	public static final RegistryObject<ConfiguredFeature<?, ?>> END_TREE = REGISTER.register("end_tree", () -> new ConfiguredFeature<>(MSFeatures.END_TREE.get(), FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> GLOWING_TREE = REGISTER.register("glowing_tree", () -> new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
			new BlockStateConfiguration(MSBlocks.GLOWING_LOG.get().defaultBlockState())));
	public static final RegistryObject<ConfiguredFeature<?, ?>> PETRIFIED_TREE = REGISTER.register("petrified_tree", () -> new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
			new BlockStateConfiguration(MSBlocks.PETRIFIED_LOG.get().defaultBlockState())));
	public static final RegistryObject<ConfiguredFeature<?, ?>> DEAD_TREE = REGISTER.register("dead_tree", () -> new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
			new BlockStateConfiguration(MSBlocks.DEAD_LOG.get().defaultBlockState())));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> FOREST_LAND_TREES = REGISTER.register("forest_land_trees", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.BIRCH_CHECKED, 0.2F), new WeightedPlacedFeature(TreePlacements.FANCY_OAK_CHECKED, 0.1F)), TreePlacements.OAK_CHECKED)));
	public static final RegistryObject<ConfiguredFeature<?, ?>> TAIGA_LAND_TREES = REGISTER.register("taiga_land_trees", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
			new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.PINE_CHECKED, 1 / 3F)), TreePlacements.SPRUCE_CHECKED)));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> STRAWBERRY_PATCH = REGISTER.register("strawberry_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.STRAWBERRY.get())), List.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT), 64)));
	public static final RegistryObject<ConfiguredFeature<?, ?>> GLOWING_MUSHROOM_PATCH = REGISTER.register("glowing_mushroom_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState()))))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> TALL_END_GRASS_PATCH = REGISTER.register("tall_end_grass_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_END_GRASS.get().defaultBlockState())))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> PETRIFIED_GRASS_PATCH = REGISTER.register("petrified_grass_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_GRASS.get().defaultBlockState()))))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> PETRIFIED_POPPY_PATCH = REGISTER.register("petrified_poppy_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_POPPY.get().defaultBlockState()))))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> BLOOMING_CACTUS_PATCH = REGISTER.register("blooming_cactus_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.BLOOMING_CACTUS.get()))))));
	public static final RegistryObject<ConfiguredFeature<?, ?>> DESERT_BUSH_PATCH = REGISTER.register("desert_bush_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.DESERT_BUSH.get()))))));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> PUMPKIN = REGISTER.register("pumpkin", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN))));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> RABBIT_PLACEMENT = REGISTER.register("rabbit_placement", () -> new ConfiguredFeature<>(MSFeatures.RABBIT_PLACEMENT.get(), FeatureConfiguration.NONE));
	
	/**
	 * The OreConfiguration uses an inlined list that matches ores to the stone type they should replace
	 */
	public static final RegistryObject<ConfiguredFeature<?, ?>> CRUXITE_ORE = REGISTER.register("cruxite_ore", () -> new ConfiguredFeature<>(Feature.ORE,
			new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, MSBlocks.STONE_CRUXITE_ORE.get().defaultBlockState())), OreGeneration.baseCruxiteVeinSize)));
	public static final RegistryObject<ConfiguredFeature<?, ?>> URANIUM_ORE = REGISTER.register("uranium_ore", () -> new ConfiguredFeature<>(Feature.ORE,
			new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, MSBlocks.STONE_URANIUM_ORE.get().defaultBlockState())), OreGeneration.baseUraniumVeinSize)));
}