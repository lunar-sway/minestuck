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
public class ShadewoodTree extends AbstractTreeGrower
{
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random, boolean hasFlowers)
	{
		//TODO theres an issue with structure nbt defined variants that prevent them from generating correctly when grown from saplings
		return level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY)
				.getHolderOrThrow(random.nextInt(20) == 0 ? MSCFeatures.SCARRED_SHADEWOOD_TREE : MSCFeatures.SHADEWOOD_TREE);
	}
	
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean hasFlowers)
	{
		throw new UnsupportedOperationException();
	}
}