package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ShadewoodTree extends AbstractTreeGrower
{
	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean hasFlowers)
	{
		//TODO theres an issue with structure nbt defined variants that prevent them from generating correctly when grown from saplings
		return rand.nextInt(20) == 0 ? MSCFeatures.SCARRED_SHADEWOOD_TREE : MSCFeatures.SHADEWOOD_TREE;
	}
}