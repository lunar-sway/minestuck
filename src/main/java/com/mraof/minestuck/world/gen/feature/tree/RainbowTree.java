package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RainbowTree extends AbstractTreeGrower
{
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random, boolean hasFlowers)
	{
		return level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).getHolderOrThrow(MSCFeatures.RAINBOW_TREE);
	}
	
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomIn, boolean hasFlowers)
	{
		throw new UnsupportedOperationException();
	}
}