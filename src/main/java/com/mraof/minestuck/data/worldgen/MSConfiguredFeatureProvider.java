package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.AspectTreeBlocks;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.OreGeneration;
import com.mraof.minestuck.world.gen.feature.RandomRockBlockBlobConfig;
import com.mraof.minestuck.world.gen.feature.SimpleTemplateFeature;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static com.mraof.minestuck.world.gen.feature.MSCFeatures.*;

public final class MSConfiguredFeatureProvider
{
	public static void register(BootstapContext<ConfiguredFeature<?, ?>> context)
	{
		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
		
		context.register(RETURN_NODE, new ConfiguredFeature<>(MSFeatures.RETURN_NODE.get(), FeatureConfiguration.NONE));
		
		var smallCog = context.register(SMALL_COG, new ConfiguredFeature<>(MSFeatures.SIMPLE_TEMPLATE.get(),
				new SimpleTemplateFeature.Config(Minestuck.id("small_cog"), true,
						SimpleTemplateFeature.HeightQueryType.MIN.with(Heightmap.Types.WORLD_SURFACE_WG, UniformInt.of(-2, 0)))));
		
		var largeCog1 = context.register(LARGE_COG_1, new ConfiguredFeature<>(MSFeatures.SIMPLE_TEMPLATE.get(),
				new SimpleTemplateFeature.Config(Minestuck.id("large_cog_1"), true,
						SimpleTemplateFeature.HeightQueryType.MIN.with(Heightmap.Types.WORLD_SURFACE_WG, UniformInt.of(-3, 0)))));
		var largeCog2 = context.register(LARGE_COG_2, new ConfiguredFeature<>(MSFeatures.SIMPLE_TEMPLATE.get(),
				new SimpleTemplateFeature.Config(Minestuck.id("large_cog_2"), true,
						SimpleTemplateFeature.HeightQueryType.MIN.with(Heightmap.Types.WORLD_SURFACE_WG, UniformInt.of(-3, 0)))));
		var largeCog = context.register(LARGE_COG, new ConfiguredFeature<>(Feature.RANDOM_BOOLEAN_SELECTOR,
				new RandomBooleanFeatureConfiguration(PlacementUtils.inlinePlaced(largeCog1), PlacementUtils.inlinePlaced(largeCog2))));
		
		context.register(COG, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(largeCog), 0.1F)), PlacementUtils.inlinePlaced(smallCog))));
		
		context.register(FLOOR_COG, new ConfiguredFeature<>(MSFeatures.FLOOR_COG.get(), FeatureConfiguration.NONE));
		
		context.register(SURFACE_FOSSIL, new ConfiguredFeature<>(MSFeatures.SURFACE_FOSSIL.get(), FeatureConfiguration.NONE));
		context.register(BROKEN_SWORD, new ConfiguredFeature<>(MSFeatures.BROKEN_SWORD.get(), FeatureConfiguration.NONE));
		context.register(BUCKET, new ConfiguredFeature<>(MSFeatures.BUCKET.get(), FeatureConfiguration.NONE));
		
		context.register(CAKE_PEDESTAL, new ConfiguredFeature<>(MSFeatures.SIMPLE_TEMPLATE.get(),
				new SimpleTemplateFeature.Config(Minestuck.id("cake_pedestal"), true)));
		
		context.register(SMALL_LIBRARY, new ConfiguredFeature<>(MSFeatures.SMALL_LIBRARY.get(), FeatureConfiguration.NONE));
		context.register(TOWER, new ConfiguredFeature<>(MSFeatures.TOWER.get(), FeatureConfiguration.NONE));
		context.register(PARCEL_PYXIS, new ConfiguredFeature<>(MSFeatures.PARCEL_PYXIS.get(), FeatureConfiguration.NONE));
		context.register(FROG_RUINS, new ConfiguredFeature<>(MSFeatures.FROG_RUINS.get(), FeatureConfiguration.NONE));
		
		context.register(CARVED_CHERRY_TREE, new ConfiguredFeature<>(MSFeatures.CARVED_CHERRY_TREE.get(), FeatureConfiguration.NONE));
		context.register(CARVED_HOUSE, new ConfiguredFeature<>(MSFeatures.CARVED_HOUSE.get(), FeatureConfiguration.NONE));
		context.register(CARVED_LOG, new ConfiguredFeature<>(MSFeatures.CARVED_LOG.get(), FeatureConfiguration.NONE));
		context.register(LARGE_CARVED_LOG, new ConfiguredFeature<>(MSFeatures.LARGE_CARVED_LOG.get(), FeatureConfiguration.NONE));
		context.register(TREATED_CHAIR, new ConfiguredFeature<>(MSFeatures.TREATED_CHAIR.get(), FeatureConfiguration.NONE));
		context.register(TREATED_TABLE, new ConfiguredFeature<>(MSFeatures.TREATED_TABLE.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_BOOKSHELF, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_BOOKSHELF.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_CHAIR, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_CHAIR.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_CREEPER, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_CREEPER.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_DRAWER, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_DRAWER.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_SALAMANDER, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_SALAMANDER.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_TABLE, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_TABLE.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_TREE, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_TREE.get(), FeatureConfiguration.NONE));
		context.register(UNFINISHED_CARVED_TUNNEL, new ConfiguredFeature<>(MSFeatures.UNFINISHED_CARVED_TUNNEL.get(), FeatureConfiguration.NONE));
		context.register(WOOD_SHAVINGS_PILE, new ConfiguredFeature<>(MSFeatures.WOOD_SHAVINGS_PILE.get(), FeatureConfiguration.NONE));
		context.register(WOODEN_CACTUS_PAIR, new ConfiguredFeature<>(MSFeatures.WOODEN_CACTUS_PAIR.get(), FeatureConfiguration.NONE));
		context.register(MASSIVE_CHAIR, new ConfiguredFeature<>(MSFeatures.MASSIVE_CHAIR.get(), FeatureConfiguration.NONE));
		context.register(MASSIVE_STOOL, new ConfiguredFeature<>(MSFeatures.MASSIVE_STOOL.get(), FeatureConfiguration.NONE));
		
		context.register(SULFUR_POOL, new ConfiguredFeature<>(MSFeatures.SULFUR_POOL.get(), FeatureConfiguration.NONE));
		context.register(CAST_IRON_BUILDING, new ConfiguredFeature<>(MSFeatures.CAST_IRON_BUILDING.get(), FeatureConfiguration.NONE));
		context.register(CAST_IRON_PLATFORM, new ConfiguredFeature<>(MSFeatures.CAST_IRON_PLATFORM.get(), FeatureConfiguration.NONE));
		
		context.register(BLOOD_POOL, new ConfiguredFeature<>(Feature.LAKE,
				new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.BLOOD.get()), BlockStateProvider.simple(Blocks.AIR))));
		context.register(OIL_POOL, new ConfiguredFeature<>(Feature.LAKE,
				new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.OIL.get()), BlockStateProvider.simple(Blocks.AIR))));
		context.register(OASIS, new ConfiguredFeature<>(MSFeatures.OASIS.get(), FeatureConfiguration.NONE));
		context.register(OCEAN_RUNDOWN, new ConfiguredFeature<>(MSFeatures.OCEAN_RUNDOWN.get(), FeatureConfiguration.NONE));
		
		context.register(FIRE_FIELD, new ConfiguredFeature<>(MSFeatures.FIRE_FIELD.get(), FeatureConfiguration.NONE));
		context.register(COARSE_DIRT_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.COARSE_DIRT), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 4), 2)));
		context.register(SNOW_BLOCK_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 4), 2)));
		context.register(SMALL_SNOW_BLOCK_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SNOW_BLOCK), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 3), 2)));
		context.register(ICE_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.ICE), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT), UniformInt.of(2, 6), 1)));
		context.register(SAND_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SAND), BlockPredicate.matchesBlocks(Blocks.SANDSTONE, Blocks.SAND), UniformInt.of(2, 5), 2)));
		context.register(RED_SAND_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.RED_SAND), BlockPredicate.matchesBlocks(Blocks.RED_SANDSTONE, Blocks.RED_SAND), UniformInt.of(2, 5), 2)));
		context.register(SLIME_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.SLIME_BLOCK), BlockPredicate.matchesBlocks(Blocks.MYCELIUM, Blocks.DIRT), UniformInt.of(2, 6), 2)));
		context.register(NETHERRACK_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.NETHERRACK), BlockPredicate.alwaysTrue(), UniformInt.of(2, 3), 1)));
		context.register(MAGMATIC_IGNEOUS_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.MAGMATIC_IGNEOUS_STONE.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 3), 1)));
		context.register(BLACK_SAND_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.BLACK_SAND.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 4)));
		context.register(PUMICE_STONE_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.PUMICE_STONE.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 4)));
		context.register(TREATED_PLANKS_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.TREATED_PLANKS.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 2)));
		context.register(LACQUERED_PLANKS_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.LACQUERED_PLANKS.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 2)));
		context.register(TREATED_UNCARVED_WOOD_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.TREATED_UNCARVED_WOOD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 4)));
		context.register(TREATED_CHIPBOARD_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.TREATED_CHIPBOARD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 4)));
		context.register(CHIPBOARD_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.CHIPBOARD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 4)));
		context.register(CARVED_PLANKS_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.UNCARVED_WOOD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 8), 4)));
		context.register(COAGULATED_BLOOD_DISK, new ConfiguredFeature<>(MSFeatures.DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.COAGULATED_BLOOD.get()), BlockPredicate.alwaysTrue(), UniformInt.of(2, 5), 2)));
		context.register(COBBLESTONE_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.COBBLESTONE), BlockPredicate.matchesBlocks(Blocks.GRAVEL, Blocks.COBBLESTONE), UniformInt.of(2, 5), 1)));
		context.register(STONE_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.STONE), BlockPredicate.matchesBlocks(Blocks.GRAVEL, Blocks.STONE), UniformInt.of(2, 4), 2)));
		context.register(END_GRASS_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(MSBlocks.END_GRASS.get()), BlockPredicate.matchesBlocks(Blocks.END_STONE, MSBlocks.END_GRASS.get()), UniformInt.of(2, 4), 1)));
		context.register(END_STONE_SURFACE_DISK, new ConfiguredFeature<>(MSFeatures.GRASSY_SURFACE_DISK.get(),
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.END_STONE), BlockPredicate.matchesBlocks(MSBlocks.END_GRASS.get(), Blocks.END_STONE), UniformInt.of(2, 3), 1)));
		
		context.register(MESA, new ConfiguredFeature<>(MSFeatures.MESA.get(), FeatureConfiguration.NONE));
		context.register(STONE_MOUND, new ConfiguredFeature<>(MSFeatures.STONE_MOUND.get(), new BlockStateConfiguration(Blocks.STONE.defaultBlockState())));
		context.register(COBBLESTONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.COBBLESTONE.defaultBlockState())));
		context.register(SANDSTONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.SANDSTONE.defaultBlockState())));
		context.register(RED_SANDSTONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(Blocks.RED_SANDSTONE.defaultBlockState())));
		context.register(RANDOM_ROCK_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(3)));
		context.register(LARGE_RANDOM_ROCK_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.RANDOM_ROCK_BLOCK_BLOB.get(), new RandomRockBlockBlobConfig(5)));
		context.register(SHADE_STONE_BLOCK_BLOB, new ConfiguredFeature<>(MSFeatures.BLOCK_BLOB.get(), new BlockStateConfiguration(MSBlocks.SHADE_STONE.get().defaultBlockState())));
		var smallPillar = context.register(SMALL_PILLAR, new ConfiguredFeature<>(MSFeatures.SMALL_PILLAR.get(), new BlockStateConfiguration(Blocks.STONE_BRICKS.defaultBlockState())));
		var pillar = context.register(PILLAR, new ConfiguredFeature<>(MSFeatures.PILLAR.get(), new BlockStateConfiguration(Blocks.STONE_BRICKS.defaultBlockState())));
		context.register(MIXED_PILLARS, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(pillar), 0.4F)), PlacementUtils.inlinePlaced(smallPillar))));
		
		context.register(LARGE_CAKE, new ConfiguredFeature<>(MSFeatures.LARGE_CAKE.get(), FeatureConfiguration.NONE));
		
		context.register(RAINBOW_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(MSBlocks.RAINBOW_LOG.get().defaultBlockState()), new StraightTrunkPlacer(4, 2, 0),
				BlockStateProvider.simple(MSBlocks.RAINBOW_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
				new TwoLayersFeatureSize(1, 0, 1)).dirt(BlockStateProvider.simple(Blocks.WHITE_WOOL)).ignoreVines().build()));
		context.register(END_TREE, new ConfiguredFeature<>(MSFeatures.END_TREE.get(), FeatureConfiguration.NONE));
		context.register(GLOWING_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.GLOWING_LOG.get().defaultBlockState())));
		context.register(SHADEWOOD_TREE, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LOG.get().defaultBlockState(), 10).add(MSBlocks.ROTTED_SHADEWOOD_LOG.get().defaultBlockState(), 1)),
				new StraightTrunkPlacer(4, 1, 1),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)),
				new AcaciaFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 1)),
				new TwoLayersFeatureSize(1, 0, 2))).ignoreVines()
				.decorators(List.of(new AttachedToLeavesDecorator(0.20F, 1, 0, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)), 2, List.of(Direction.DOWN)),
						new AttachedToLeavesDecorator(0.1F, 1, 0, BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState()), 1, List.of(Direction.DOWN)))).build()));
		//scarred variant tends to be a little more scraggly
		context.register(SCARRED_SHADEWOOD_TREE, new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(MSBlocks.SCARRED_SHADEWOOD_LOG.get().defaultBlockState()), new StraightTrunkPlacer(3, 1, 1),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)),
				new AcaciaFoliagePlacer(UniformInt.of(1, 3), UniformInt.of(0, 1)),
				new TwoLayersFeatureSize(1, 0, 2))).ignoreVines()
				.decorators(List.of(new AttachedToLeavesDecorator(0.1F, 1, 0, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.SHADEWOOD_LEAVES.get().defaultBlockState(), 8).add(MSBlocks.SHROOMY_SHADEWOOD_LEAVES.get().defaultBlockState(), 1)), 2, List.of(Direction.DOWN)),
						new AttachedToLeavesDecorator(0.4F, 1, 0, BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState()), 1, List.of(Direction.DOWN)))).build()));
		context.register(ORNATE_SHADEWOOD_TREE, new ConfiguredFeature<>(MSFeatures.ORNATE_SHADEWOOD_TREE.get(), FeatureConfiguration.NONE));
		context.register(TREE_STUMP, new ConfiguredFeature<>(MSFeatures.TREE_STUMP.get(), FeatureConfiguration.NONE));
		context.register(PETRIFIED_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.PETRIFIED_LOG.get().defaultBlockState())));
		context.register(DEAD_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.DEAD_LOG.get().defaultBlockState())));
		context.register(CINDERED_TREE, new ConfiguredFeature<>(MSFeatures.LEAFLESS_TREE.get(),
				new BlockStateConfiguration(MSBlocks.CINDERED_LOG.get().defaultBlockState())));
	
		context.register(FROST_TREE, new ConfiguredFeature<>(Feature.TREE,
				(new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(MSBlocks.FROST_LOG.get()),
						new FancyTrunkPlacer(20, 3, 0),
						new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(MSBlocks.FROST_LEAVES.get().defaultBlockState(), 3)
								.add(MSBlocks.FROST_LEAVES_FLOWERING.get().defaultBlockState(), 1)),
						new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
						new TwoLayersFeatureSize(1, 0, 1))).dirt(BlockStateProvider.simple(Blocks.SNOW_BLOCK)).forceDirt().build()));
		
		
		context.register(BLOOD_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.BLOOD_ASPECT_LOG.get().defaultBlockState()),
						new DarkOakTrunkPlacer(8, 4, 0), BlockStateProvider.simple(AspectTreeBlocks.BLOOD_ASPECT_LEAVES.get().defaultBlockState()),
						new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(BREATH_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.BREATH_ASPECT_LOG.get().defaultBlockState()),
						new CherryTrunkPlacer(7, 1, 0, new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()), UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)),
						BlockStateProvider.simple(AspectTreeBlocks.BREATH_ASPECT_LEAVES.get().defaultBlockState()),
						new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(DOOM_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.DOOM_ASPECT_LOG.get().defaultBlockState()),
						new StraightTrunkPlacer(8, 4, 0), BlockStateProvider.simple(AspectTreeBlocks.DOOM_ASPECT_LEAVES.get().defaultBlockState()),
						new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(HEART_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.HEART_ASPECT_LOG.get().defaultBlockState()),
						new ForkingTrunkPlacer(5, 2, 2), BlockStateProvider.simple(AspectTreeBlocks.HEART_ASPECT_LEAVES.get().defaultBlockState()),
						new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 4),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
				
		context.register(HOPE_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.HOPE_ASPECT_LOG.get().defaultBlockState()),
						new FancyTrunkPlacer(14, 2, 0),
						BlockStateProvider.simple(AspectTreeBlocks.HOPE_ASPECT_LEAVES.get().defaultBlockState()),
						new CherryFoliagePlacer(ConstantInt.of(3), ConstantInt.of(3), ConstantInt.of(4), 0.25F, 0.5F, 0.5F, 0.33333334F),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(LIFE_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.LIFE_ASPECT_LOG.get().defaultBlockState()),
						new CherryTrunkPlacer(7, 1, 0, new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()), UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)),
						BlockStateProvider.simple(AspectTreeBlocks.LIFE_ASPECT_LEAVES.get().defaultBlockState()),
						new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 3),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(LIGHT_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.LIGHT_ASPECT_LOG.get().defaultBlockState()),
						new StraightTrunkPlacer(5, 2, 0),
						BlockStateProvider.simple(AspectTreeBlocks.LIGHT_ASPECT_LEAVES.get().defaultBlockState()),
						new SpruceFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), ConstantInt.of(3)),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));

		context.register(MIND_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.MIND_ASPECT_LOG.get().defaultBlockState()),
						new ForkingTrunkPlacer(6, 2, 0),
						BlockStateProvider.simple(AspectTreeBlocks.MIND_ASPECT_LEAVES.get().defaultBlockState()),
						new CherryFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(4), 0.25F, 0.5F, 0.5F, 0.33333334F),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(RAGE_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.RAGE_ASPECT_LOG.get().defaultBlockState()),
						new ForkingTrunkPlacer(5, 4, 0),
						BlockStateProvider.simple(AspectTreeBlocks.RAGE_ASPECT_LEAVES.get().defaultBlockState()),
						new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(SPACE_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.SPACE_ASPECT_LOG.get().defaultBlockState()),
						new FancyTrunkPlacer(10, 4, 0),
						BlockStateProvider.simple(AspectTreeBlocks.SPACE_ASPECT_LEAVES.get().defaultBlockState()),
						new CherryFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(4), 0.25F, 0.5F, 0.5F, 0.33333334F),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(TIME_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.TIME_ASPECT_LOG.get().defaultBlockState()),
						new StraightTrunkPlacer(6, 4, 0),
						BlockStateProvider.simple(AspectTreeBlocks.TIME_ASPECT_LEAVES.get().defaultBlockState()),
						new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(VOID_TREE, new ConfiguredFeature<>(Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AspectTreeBlocks.VOID_ASPECT_LOG.get().defaultBlockState()),
						new StraightTrunkPlacer(5, 2, 0),
						BlockStateProvider.simple(AspectTreeBlocks.VOID_ASPECT_LEAVES.get().defaultBlockState()),
						new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(1), ConstantInt.of(4), 0.1F, 0.1F, 0.75F, 0.5F),
						new TwoLayersFeatureSize(1, 0, 1))
						.dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().ignoreVines().build()));
		
		context.register(FOREST_LAND_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.BIRCH_CHECKED), 0.2F), new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_CHECKED), 0.1F)), placedFeatures.getOrThrow(TreePlacements.OAK_CHECKED))));
		context.register(TAIGA_LAND_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
				new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.PINE_CHECKED), 1 / 3F)), placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED))));
		
		context.register(STRAWBERRY_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.STRAWBERRY.get())), List.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.MUD), 4)));
		context.register(GLOWING_MUSHROOM_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState()))))));
		context.register(WOODEN_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.WOODEN_GRASS.get().defaultBlockState()))))));
		context.register(TREATED_WOODEN_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TREATED_WOODEN_GRASS.get().defaultBlockState()))))));
		context.register(LACQUERED_WOODEN_MUSHROOM_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.LACQUERED_WOODEN_MUSHROOM.get().defaultBlockState()))))));
		context.register(CARVED_BUSH_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.CARVED_BUSH.get().defaultBlockState()))))));
		context.register(TALL_END_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_END_GRASS.get().defaultBlockState())))));
		context.register(PETRIFIED_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_GRASS.get().defaultBlockState()))))));
		context.register(PETRIFIED_POPPY_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_POPPY.get().defaultBlockState()))))));
		context.register(SINGED_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.SINGED_GRASS.get().defaultBlockState()))))));
		context.register(SINGED_FOLIAGE_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.SINGED_FOLIAGE.get().defaultBlockState()))))));
		context.register(IGNEOUS_SPIKE_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.IGNEOUS_SPIKE.get().defaultBlockState()))))));
		context.register(SULFUR_BUBBLE_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(16, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.SULFUR_BUBBLE.get().defaultBlockState()))))));
		context.register(BLOOMING_CACTUS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.BLOOMING_CACTUS.get()))))));
		context.register(DESERT_BUSH_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.DESERT_BUSH.get()))))));
		context.register(SANDY_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.SANDY_GRASS.get()))))));
		context.register(TALL_SANDY_GRASS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_SANDY_GRASS.get().defaultBlockState())))));
		context.register(TALL_DEAD_BUSH_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_DEAD_BUSH.get().defaultBlockState())))));
		context.register(MOSS_CARPET_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MOSS_CARPET.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
		context.register(AZALEA_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.AZALEA.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
		context.register(CRIMSON_FUNGUS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.CRIMSON_FUNGUS.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.MYCELIUM)))));
		context.register(WARPED_FUNGUS_PATCH, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.WARPED_FUNGUS.defaultBlockState())), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.MYCELIUM)))));
		
		context.register(PUMPKIN, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN))));
		
		context.register(CEILING_ROOTS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.HANGING_ROOTS))));
		
		context.register(RABBIT_PLACEMENT, new ConfiguredFeature<>(MSFeatures.RABBIT_PLACEMENT.get(), FeatureConfiguration.NONE));
		
		RuleTest replacableStoneTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		
		context.register(CRUXITE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
				OreConfiguration.target(replacableStoneTest, MSBlocks.STONE_CRUXITE_ORE.get().defaultBlockState())),
				OreGeneration.baseCruxiteVeinSize)));
		context.register(URANIUM_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(
				OreConfiguration.target(replacableStoneTest, MSBlocks.STONE_URANIUM_ORE.get().defaultBlockState()),
				OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), MSBlocks.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())),
				OreGeneration.baseUraniumVeinSize)));
	}
}
