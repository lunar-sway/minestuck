package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.OreGeneration;
import com.mraof.minestuck.world.gen.feature.RandomRockBlockBlobConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.RegistryOps;
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
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.List;
import java.util.function.BiConsumer;

import static com.mraof.minestuck.world.gen.feature.MSCFeatures.*;

public final class MSConfiguredFeatureProvider
{
	public static DataProvider create(RegistryAccess.Writable registryAccess, DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		var registry = registryAccess.ownedWritableRegistryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
		DataEntriesBuilder<ConfiguredFeature<?, ?>> builder = new DataEntriesBuilder<>(registry);
		generate(registry, registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY), builder::add);
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID, RegistryOps.create(JsonOps.INSTANCE, registryAccess), Registry.CONFIGURED_FEATURE_REGISTRY, builder.getMap());
	}
	
	private static void generate(Registry<ConfiguredFeature<?, ?>> configured, Registry<PlacedFeature> placed, BiConsumer<ResourceKey<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> consumer)
	{
		consumer.accept(RETURN_NODE, new ConfiguredFeature<>(MSFeatures.RETURN_NODE.get(), FeatureConfiguration.NONE));
		
		consumer.accept(SMALL_COG, new ConfiguredFeature<>(MSFeatures.SMALL_COG.get(), FeatureConfiguration.NONE));
		consumer.accept(LARGE_COG, new ConfiguredFeature<>(MSFeatures.LARGE_COG.get(), FeatureConfiguration.NONE));
		consumer.accept(COG, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(configured.getHolderOrThrow(LARGE_COG)), 0.1F)), PlacementUtils.inlinePlaced(configured.getHolderOrThrow(SMALL_COG)))));
		consumer.accept(FLOOR_COG, new ConfiguredFeature<>(MSFeatures.FLOOR_COG.get(), FeatureConfiguration.NONE));
		
		consumer.accept(SURFACE_FOSSIL, new ConfiguredFeature<>(MSFeatures.SURFACE_FOSSIL.get(), FeatureConfiguration.NONE));
		consumer.accept(BROKEN_SWORD, new ConfiguredFeature<>(MSFeatures.BROKEN_SWORD.get(), FeatureConfiguration.NONE));
		consumer.accept(BUCKET, new ConfiguredFeature<>(MSFeatures.BUCKET.get(), FeatureConfiguration.NONE));
		consumer.accept(CAKE_PEDESTAL, new ConfiguredFeature<>(MSFeatures.CAKE_PEDESTAL.get(), FeatureConfiguration.NONE));
		consumer.accept(SMALL_LIBRARY, new ConfiguredFeature<>(MSFeatures.SMALL_LIBRARY.get(), FeatureConfiguration.NONE));
		consumer.accept(TOWER, new ConfiguredFeature<>(MSFeatures.TOWER.get(), FeatureConfiguration.NONE));
		consumer.accept(PARCEL_PYXIS, new ConfiguredFeature<>(MSFeatures.PARCEL_PYXIS.get(), FeatureConfiguration.NONE));
		
		consumer.accept(BLOOD_POOL, new ConfiguredFeature<>(Feature.LAKE,
				new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.BLOOD.get()), BlockStateProvider.simple(Blocks.AIR))));
		consumer.accept(OIL_POOL, new ConfiguredFeature<>(Feature.LAKE,
				new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.OIL.get()), BlockStateProvider.simple(Blocks.AIR))));
		consumer.accept(OASIS, new ConfiguredFeature<>(MSFeatures.OASIS.get(), FeatureConfiguration.NONE));
		consumer.accept(OCEAN_RUNDOWN, new ConfiguredFeature<>(MSFeatures.OCEAN_RUNDOWN.get(), FeatureConfiguration.NONE));
		
		consumer.accept(FIRE_FIELD, new ConfiguredFeature<>(MSFeatures.FIRE_FIELD.get(), FeatureConfiguration.NONE));
		consumer.accept(COARSE_DIRT_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.COARSE_DIRT), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 4), 2)));
		consumer.accept(SNOW_BLOCK_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 4), 2)));
		consumer.accept(SMALL_SNOW_BLOCK_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 3), 2)));
		consumer.accept(ICE_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.ICE), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 6), 1)));
		consumer.accept(SAND_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SAND), BlockPredicate.matchesBlocks(Blocks.SANDSTONE, Blocks.SAND), UniformInt.of(2, 5), 2)));
		consumer.accept(RED_SAND_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.RED_SAND), BlockPredicate.matchesBlocks(Blocks.RED_SANDSTONE, Blocks.RED_SAND), UniformInt.of(2, 5), 2)));
		consumer.accept(SLIME_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SLIME_BLOCK), BlockPredicate.matchesBlocks(Blocks.MYCELIUM, Blocks.DIRT), UniformInt.of(2, 6), 2)));
		consumer.accept(NETHERRACK_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.NETHERRACK), BlockPredicate.alwaysTrue(), UniformInt.of(2, 3), 1)));
		consumer.accept(COAGULATED_BLOOD_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.COAGULATED_BLOOD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 5), 2)));
		consumer.accept(COBBLESTONE_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.COBBLESTONE), BlockPredicate.matchesBlocks(Blocks.GRAVEL, Blocks.COBBLESTONE), UniformInt.of(2, 5), 1)));
		consumer.accept(STONE_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.STONE), BlockPredicate.matchesBlocks(Blocks.GRAVEL, Blocks.STONE), UniformInt.of(2, 4), 2)));
		consumer.accept(END_GRASS_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.END_GRASS.get()), BlockPredicate.matchesBlocks(Blocks.END_STONE, MSBlocks.END_GRASS.get()), UniformInt.of(2, 4), 1)));
		consumer.accept(END_STONE_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.END_STONE), BlockPredicate.matchesBlocks(MSBlocks.END_GRASS.get(), Blocks.END_STONE), UniformInt.of(2, 3), 1)));
		
		consumer.accept(MESA, new ConfiguredFeature<>(MSFeatures.MESA.get(), FeatureConfiguration.NONE));
		consumer.accept(STONE_MOUND, new ConfiguredFeature<>(MSFeatures.STONE_MOUND.get(), new BlockStateConfiguration(Blocks.STONE.defaultBlockState())));
		consumer.accept(COBBLESTONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.COBBLESTONE.defaultBlockState())));
		consumer.accept(SANDSTONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.SANDSTONE.defaultBlockState())));
		consumer.accept(RED_SANDSTONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.RED_SANDSTONE.defaultBlockState())));
		consumer.accept(RANDOM_ROCK_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(3)));
		consumer.accept(LARGE_RANDOM_ROCK_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(5)));
		consumer.accept(SHADE_STONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(MSBlocks.SHADE_STONE.get().defaultBlockState())));
		consumer.accept(SMALL_PILLAR, new ConfiguredFeature<>(MSFeatures.SMALL_PILLAR.get(), new BlockStateConfiguration(Blocks.STONE_BRICKS.defaultBlockState())));
		consumer.accept(PILLAR, new ConfiguredFeature<>(MSFeatures.PILLAR.get(), new BlockStateConfiguration(Blocks.STONE_BRICKS.defaultBlockState())));
		consumer.accept(MIXED_PILLARS, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(configured.getHolderOrThrow(PILLAR)), 0.4F)), PlacementUtils.inlinePlaced(configured.getHolderOrThrow(SMALL_PILLAR)))));
		
		consumer.accept(LARGE_CAKE, new ConfiguredFeature<>(MSFeatures.LARGE_CAKE.get(), FeatureConfiguration.NONE));
		
		consumer.accept(RAINBOW_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(MSBlocks.RAINBOW_LOG.get().defaultBlockState()), new StraightTrunkPlacer(4, 2, 0),
				BlockStateProvider.simple(MSBlocks.RAINBOW_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
				new TwoLayersFeatureSize(1, 0, 1)).dirt(BlockStateProvider.simple(Blocks.WHITE_WOOL)).ignoreVines().build()));
		consumer.accept(END_TREE, new ConfiguredFeature<>(MSFeatures.END_TREE.get(), FeatureConfiguration.NONE));
		consumer.accept(GLOWING_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.GLOWING_LOG.get().defaultBlockState())));
		consumer.accept(SHADEWOOD_TREE, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LOG.get().defaultBlockState(), 10).add(MSBlocks.ROTTED_SHADEWOOD_LOG.get().defaultBlockState(), 1)),
				new StraightTrunkPlacer(4, 1, 1),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)),
				new AcaciaFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 1)),
				new TwoLayersFeatureSize(1, 0, 2))).ignoreVines()
				.decorators(List.of(new AttachedToLeavesDecorator(0.20F, 1, 0, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)), 2, List.of(Direction.DOWN)),
						new AttachedToLeavesDecorator(0.1F, 1, 0, BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState()), 1, List.of(Direction.DOWN)))).build()));
		//scarred variant tends to be a little more scraggly
		consumer.accept(SCARRED_SHADEWOOD_TREE, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(MSBlocks.SCARRED_SHADEWOOD_LOG.get().defaultBlockState()), new StraightTrunkPlacer(3, 1, 1),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)),
				new AcaciaFoliagePlacer(UniformInt.of(1, 3), UniformInt.of(0, 1)),
				new TwoLayersFeatureSize(1, 0, 2))).ignoreVines()
				.decorators(List.of(new AttachedToLeavesDecorator(0.1F, 1, 0, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)), 2, List.of(Direction.DOWN)),
						new AttachedToLeavesDecorator(0.4F, 1, 0, BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState()), 1, List.of(Direction.DOWN)))).build()));
		consumer.accept(ORNATE_SHADEWOOD_TREE, new ConfiguredFeature<>(MSFeatures.ORNATE_SHADEWOOD_TREE.get(), FeatureConfiguration.NONE));
		consumer.accept(PETRIFIED_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.PETRIFIED_LOG.get().defaultBlockState())));
		consumer.accept(DEAD_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.DEAD_LOG.get().defaultBlockState())));
		
		consumer.accept(FOREST_LAND_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(wrap(placed, TreePlacements.BIRCH_CHECKED), 0.2F), new WeightedPlacedFeature(wrap(placed, TreePlacements.FANCY_OAK_CHECKED), 0.1F)), wrap(placed, TreePlacements.OAK_CHECKED))));
		consumer.accept(TAIGA_LAND_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(wrap(placed, TreePlacements.PINE_CHECKED), 1 / 3F)), wrap(placed, TreePlacements.SPRUCE_CHECKED))));
		
		consumer.accept(STRAWBERRY_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.STRAWBERRY.get())), List.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.MUD), 4)));
		consumer.accept(GLOWING_MUSHROOM_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState()))))));
		consumer.accept(TALL_END_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_END_GRASS.get().defaultBlockState())))));
		consumer.accept(PETRIFIED_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_GRASS.get().defaultBlockState()))))));
		consumer.accept(PETRIFIED_POPPY_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_POPPY.get().defaultBlockState()))))));
		consumer.accept(BLOOMING_CACTUS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.BLOOMING_CACTUS.get()))))));
		consumer.accept(DESERT_BUSH_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.DESERT_BUSH.get()))))));
		consumer.accept(MOSS_CARPET_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MOSS_CARPET.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
		consumer.accept(AZALEA_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.AZALEA.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
		consumer.accept(CRIMSON_FUNGUS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.CRIMSON_FUNGUS.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.MYCELIUM)))));
		consumer.accept(WARPED_FUNGUS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.WARPED_FUNGUS.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.MYCELIUM)))));
		
		consumer.accept(PUMPKIN, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN))));
		
		consumer.accept(CEILING_ROOTS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.HANGING_ROOTS))));
		
		consumer.accept(RABBIT_PLACEMENT, new ConfiguredFeature<>(MSFeatures.RABBIT_PLACEMENT.get(), FeatureConfiguration.NONE));
		
		consumer.accept(CRUXITE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
				OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, MSBlocks.STONE_CRUXITE_ORE.get().defaultBlockState())),
				OreGeneration.baseCruxiteVeinSize)));
		consumer.accept(URANIUM_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
				OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, MSBlocks.STONE_URANIUM_ORE.get().defaultBlockState()),
				OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, MSBlocks.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())),
				OreGeneration.baseUraniumVeinSize)));
	}
	
	/**
	 * Converts the given reference holder to its corresponding holder in the given registry.
	 * When serializing a holder, it is important that the holder belongs to the same registry that is used in the {@link RegistryOps} used to serialize it.
	 * So if the registry op is not using {@link net.minecraft.data.BuiltinRegistries#ACCESS} any holders from the builtin registry (such as those in {@link TreePlacements}) must be converted with this function.
	 * This will not be needed in 1.19.3 where it'll not be as easy to get the holder for the wrong registry.
	 * @param registry The registry from the {@link RegistryAccess} that we used in the {@link RegistryOps}.
	 * @param holder A reference holder (not a direct holder!) that comes from some other registry set.
	 * @return A reference holder from the given registry matching the given holder.
	 */
	private static Holder<PlacedFeature> wrap(Registry<PlacedFeature> registry, Holder<PlacedFeature> holder)
	{
		return registry.getHolderOrThrow(holder.unwrapKey().orElseThrow());
	}
}
