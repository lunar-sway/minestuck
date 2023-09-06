package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

/**
 * A feature which places a set of branches in a similar manner to fancy oak trees,
 * but without any leaves.
 */
public class LeaflessTreeFeature extends Feature<BlockStateConfiguration>
{
	public LeaflessTreeFeature(Codec<BlockStateConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		BlockState state = context.config().state;
		
		int size = rand.nextInt(3);
		int height = 4 + size;
		
		int branchMinHeight = size < 2 ? 1 : 2;
		
		int branchCount = size + (rand.nextBoolean() ? 2 : 3);
		
		for(int i = 0; i < branchCount; i++)
		{
			int branchHeightStart = branchMinHeight + rand.nextInt(height - branchMinHeight);
			float modifier = (branchHeightStart+3)*0.2F;
			int xOffset = Math.round((rand.nextFloat() - rand.nextFloat())*4*modifier);
			int yOffset = branchHeightStart + Math.round(rand.nextFloat()*2*modifier);
			int zOffset = Math.round((rand.nextFloat() - rand.nextFloat())*4*modifier);
			
			genBranch(pos.above(branchHeightStart), pos.offset(xOffset, yOffset, zOffset), level, state);
		}
		
		genBranch(pos, pos.above(height), level, state);
		
		return true;
	}
	
	/**
	 * Generates blocks in a straight line from pos0 to pos1,
	 * and sets the blockstate axis to the axis that the line travels the furthest along.
	 */
	protected void genBranch(BlockPos pos0, BlockPos pos1, LevelAccessor level, BlockState logState)
	{
		final int xDiff = pos1.getX() - pos0.getX();
		final int yDiff = pos1.getY() - pos0.getY();
		final int zDiff = pos1.getZ() - pos0.getZ();
		final int xLength = Math.abs(xDiff);
		final int yLength = Math.abs(yDiff);
		final int zLength = Math.abs(zDiff);
		
		int length;
		Direction.Axis axis;
		if(xLength >= yLength && xLength >= zLength)
		{
			length = xLength;
			axis = Direction.Axis.X;
		} else if(yLength >= xLength && yLength >= zLength)
		{
			length = yLength;
			axis = Direction.Axis.Y;
		} else
		{
			length = zLength;
			axis = Direction.Axis.Z;
		}
		
		BlockState state = logState.setValue(RotatedPillarBlock.AXIS, axis);
		
		for(int i = 0; i < length; i++)
		{
			float f = i/(float) (length);
			BlockPos pos = pos0.offset(Mth.floor(xDiff*f), Mth.floor(yDiff*f), Mth.floor(zDiff*f));
			if(TreeFeature.validTreePos(level, pos) || level.isStateAtPosition(pos, oldState -> oldState.is(BlockTags.LOGS)))
				setBlock(level, pos, state);
			else return;
		}
	}
}