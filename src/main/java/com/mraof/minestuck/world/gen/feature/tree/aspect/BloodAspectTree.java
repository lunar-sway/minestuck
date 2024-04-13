package com.mraof.minestuck.world.gen.feature.tree.aspect;

import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class BloodAspectTree extends AbstractTreeGrower
{
	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomIn, boolean hasFlowers)
	{
		return MSCFeatures.BLOOD_TREE;
	}
}