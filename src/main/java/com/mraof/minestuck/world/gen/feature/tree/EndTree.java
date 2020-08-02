package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class EndTree extends Tree
{
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean p_225546_2_) {
		return MSFeatures.END_TREE.withConfiguration((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.END_LOG.getDefaultState()), new SimpleBlockStateProvider(MSBlocks.END_LEAVES.getDefaultState()),
				new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).ignoreVines().setSapling((net.minecraftforge.common.IPlantable) MSBlocks.END_SAPLING.getDefaultState()).build());
	}
}