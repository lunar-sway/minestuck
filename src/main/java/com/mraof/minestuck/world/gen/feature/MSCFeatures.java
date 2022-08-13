package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
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
 */
public final class MSCFeatures
{
	public static final DeferredRegister<ConfiguredFeature<?, ?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> RETURN_NODE = REGISTER.register("return_node", () -> new ConfiguredFeature<>(MSFeatures.RETURN_NODE, FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> SURFACE_FOSSIL = REGISTER.register("surface_fossil", () -> new ConfiguredFeature<>(MSFeatures.SURFACE_FOSSIL, FeatureConfiguration.NONE));
	public static final RegistryObject<ConfiguredFeature<?, ?>> BROKEN_SWORD = REGISTER.register("broken_sword", () -> new ConfiguredFeature<>(MSFeatures.BROKEN_SWORD, FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> BLOOD_POOL = REGISTER.register("blood_pool", () -> new ConfiguredFeature<>(Feature.LAKE,
			new LakeFeature.Configuration(BlockStateProvider.simple(MSBlocks.BLOOD.get()), BlockStateProvider.simple(Blocks.AIR))));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> RAINBOW_TREE = REGISTER.register("rainbow_tree", () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(MSBlocks.RAINBOW_LOG.get().defaultBlockState()), new StraightTrunkPlacer(4, 2, 0),
			BlockStateProvider.simple(MSBlocks.RAINBOW_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
			new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));
	public static final RegistryObject<ConfiguredFeature<?, ?>> END_TREE = REGISTER.register("end_tree", () -> new ConfiguredFeature<>(MSFeatures.END_TREE, FeatureConfiguration.NONE));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> STRAWBERRY_PATCH = REGISTER.register("strawberry_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.STRAWBERRY.get())), List.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT), 64)));
	
}