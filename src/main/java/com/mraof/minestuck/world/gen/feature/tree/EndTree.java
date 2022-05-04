package com.mraof.minestuck.world.gen.feature.tree;

import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class EndTree extends Tree
{
	@Override
	public boolean growTree(ServerWorld world, ChunkGenerator generator, BlockPos pos, BlockState sapling, Random rand)
	{
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.NO_RERENDER);
		if(MSCFeatures.get().END_TREE.place(world, generator, rand, pos))
			return true;
		else
		{
			world.setBlock(pos, sapling, Constants.BlockFlags.NO_RERENDER);
			return false;
		}
	}
	
	@Nullable
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random rand, boolean hasFlowers)
	{
		return null;
	}
}