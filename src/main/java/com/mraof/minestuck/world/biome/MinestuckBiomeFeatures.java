package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class MinestuckBiomeFeatures
{
	public static final RandomPatchConfiguration BLOOMING_CACTUS_CONFIG = FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.BLOOMING_CACTUS.get()))));
	public static final RandomPatchConfiguration DESERT_BUSH_CONFIG = FeatureUtils.simpleRandomPatchConfiguration(64, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.DESERT_BUSH.get()))));
	public static final RandomPatchConfiguration GLOWING_MUSHROOM_CONFIG = FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState()))));
	public static final RandomPatchConfiguration PETRIFIED_GRASS_CONFIG = FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_GRASS.get().defaultBlockState()))));
	public static final RandomPatchConfiguration PETRIFIED_POPPY_CONFIG = FeatureUtils.simpleRandomPatchConfiguration(32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.PETRIFIED_POPPY.get().defaultBlockState()))));
	public static final RandomPatchConfiguration TALL_END_GRASS_CONFIG = FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MSBlocks.TALL_END_GRASS.get().defaultBlockState())));
}
