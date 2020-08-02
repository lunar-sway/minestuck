package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class RainbowTree extends Tree
{
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		return MSFeatures.RAINBOW_TREE.withConfiguration((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.RAINBOW_LOG.getDefaultState()), new SimpleBlockStateProvider(MSBlocks.RAINBOW_LEAVES.getDefaultState()),
				new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).ignoreVines().setSapling(MSBlocks.RAINBOW_SAPLING).build());
	}
}