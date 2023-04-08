package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class ShadewoodTree extends AbstractTreeGrower
{
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean hasFlowers)
	{
		if(rand.nextInt(8) == 0)
			return MSCFeatures.ORNATE_SHADEWOOD_TREE.getHolder().orElseThrow(); //structure nbt defined variant
		else
			return rand.nextInt(20) == 0 ? MSCFeatures.SCARRED_SHADEWOOD_TREE.getHolder().orElseThrow() : MSCFeatures.SHADEWOOD_TREE.getHolder().orElseThrow();
	}
}