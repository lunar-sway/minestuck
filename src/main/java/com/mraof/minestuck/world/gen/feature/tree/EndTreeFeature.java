package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.DoubleLogBlock;
import com.mraof.minestuck.block.EndLeavesBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

//public class EndTreeFeature extends AbstractTreeFeature<TreeFeatureConfig>
//{
//	private static final BlockState LOG = MSBlocks.END_LOG.getDefaultState();
//	private static final BlockState LEAF = MSBlocks.END_LEAVES.getDefaultState();
//
//	private final int minMax = 5, maxMax = 5;
//
//	public EndTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> configFactory, boolean doBlockNotify)
//	{
//		super(configFactory);
//	}
//
//    @Override
//    protected boolean place(IWorldGenerationReader generationReader, Random rand, BlockPos positionIn, Set<BlockPos> p_225557_4_, Set<BlockPos> p_225557_5_, MutableBoundingBox boundingBoxIn, TreeFeatureConfig configIn) {
//        return super.place(generationReader, rand, positionIn, p_225557_4_, p_225557_5_, boundingBoxIn, configIn);
//    }
//}