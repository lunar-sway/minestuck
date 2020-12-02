package com.mraof.minestuck.world.biome;

import com.google.common.collect.ImmutableSet;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;

public class MinestuckBiomeFeatures
{
	public static final BlockClusterFeatureConfig BLOOMING_CACTUS_CONFIG = new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.BLOOMING_CACTUS.getDefaultState()), new SimpleBlockPlacer()).tries(32).build();
	public static final BlockClusterFeatureConfig DESERT_BUSH_CONFIG = new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.DESERT_BUSH.getDefaultState()), new SimpleBlockPlacer()).tries(64).build();
	public static final BlockClusterFeatureConfig STRAWBERRY_CONFIG = new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.STRAWBERRY.getDefaultState()), new SimpleBlockPlacer()).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig GLOWING_MUSHROOM_CONFIG = new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.GLOWING_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(32).build();
	public static final BlockClusterFeatureConfig PETRIFIED_GRASS_CONFIG = new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_GRASS.getDefaultState()), new SimpleBlockPlacer()).tries(32).build();
	public static final BlockClusterFeatureConfig PETRIFIED_POPPY_CONFIG = new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_POPPY.getDefaultState()), new SimpleBlockPlacer()).tries(32).build();
}
