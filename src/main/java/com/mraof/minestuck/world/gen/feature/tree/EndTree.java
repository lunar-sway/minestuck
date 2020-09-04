package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class EndTree extends Tree
{
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		return MSFeatures.END_TREE.withConfiguration(getDefaultConfig().build());
	}
	
	public static TreeFeatureConfig.Builder getDefaultConfig()
	{
		return new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.END_LOG.getDefaultState()), new SimpleBlockStateProvider(MSBlocks.END_LEAVES.getDefaultState()), null).setSapling(MSBlocks.END_SAPLING);
	}
	
}