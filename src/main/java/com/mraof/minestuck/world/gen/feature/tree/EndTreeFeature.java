package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.plant.DoubleLogBlock;
import com.mraof.minestuck.block.plant.EndLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class EndTreeFeature extends Feature<NoneFeatureConfiguration>
{
	
	public EndTreeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos position = context.origin();
		BlockPos soilPos = position.below();
		if(level.getBlockState(soilPos).canSustainPlant(level, soilPos, Direction.UP, MSBlocks.END_SAPLING.get()))
		{
			if(subGenerate(level, context.random(), position, position, EndLeavesBlock.LEAF_SUSTAIN_DISTANCE, 0, 4))
			{
				setLog(level, position);
				return true;
			}
		}
		return false;
	}
	
	//The point of using this algorithm, pretty much copy-pasted from the code on chorus plants, was to make these trees more reminiscent of chorus plants.
	//As it stands, however, they don't branch horizontally, the way a chorus plant would.
	//It's not necessary to fix this for end trees to exist and be enjoyed, but fixing it would be a good idea.
	private boolean subGenerate(WorldGenLevel level, RandomSource rand, BlockPos curr, BlockPos origin, int range, int step, int maxSteps)
	{
		int height = rand.nextInt(Math.max(1, 4 - step)) + 1;
		
		if(step == 0)
			height++;
		
		for(int y = 1; y < height; y++)
			if(!areAllNeighborsEmpty(level, curr.above(y), null))
				return false;
		
		for(int y = 1; y < height; y++)
			setLog(level, curr.above(y));
		
		boolean flag = false;
		
		if (step < maxSteps)
		{
			int buds = rand.nextInt(4);
			
			if (step == 0)
			{
				++buds;
			}
			
			for (int k = 0; k < buds; ++k)		//TODO: Change this to prioritize north/south growth over east/west growth, and to lock growth into one axis
			{
				Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
				BlockPos nextPos = curr.above(height).relative(direction);
				
				if (Math.abs(nextPos.getX() - origin.getX()) < range
						&& Math.abs(nextPos.getZ() - origin.getZ()) < range
						&& TreeFeature.isAirOrLeaves(level, nextPos)
						&& TreeFeature.isAirOrLeaves(level, nextPos.below())
						&& areAllNeighborsEmpty(level, nextPos, direction.getOpposite()))
				{
					flag = true;
					Direction.Axis axis = direction.getAxis();
					setLog(level, nextPos, axis);
					subGenerate(level, rand, nextPos, origin, range, step + 1, maxSteps);
					generateLeaves(level, nextPos, Direction.Axis.Y, axis);
					nextPos = curr.above(height);
					setLog(level, nextPos, axis);
				}
			}
		}
		
		if (!flag)
		{
			generateLeaves(level, curr.above(height), Direction.Axis.Y, Direction.Axis.Y);
			setLog(level, curr.above(height));
		}
		return true;
	}
	
	private static boolean areAllNeighborsEmpty(LevelSimulatedReader level, BlockPos pos, Direction excludingSide)
	{
		for (Direction direction : Direction.Plane.HORIZONTAL)
		{
			if (direction != excludingSide && !TreeFeature.isAirOrLeaves(level, pos.relative(direction)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void generateLeaves(WorldGenLevel level, BlockPos pos, Direction.Axis primary, Direction.Axis secondary)
	{
		if(primary == Direction.Axis.X || secondary == Direction.Axis.X)
		{
			leaves(level, pos.east(), 2);
			leaves(level, pos.west(), 2);
		}
		if(primary == Direction.Axis.Y || secondary == Direction.Axis.Y)
		{
			leaves(level, pos.above(), 1);
			leaves(level, pos.below(), 1);
		}
		if(primary == Direction.Axis.Z || secondary == Direction.Axis.Z)
		{
			leaves(level, pos.south(), 1);
			leaves(level, pos.north(), 1);
		}
	}
	
	private void leaves(WorldGenLevel level, BlockPos curr, int distance)
	{
		if(TreeFeature.isAirOrLeaves(level, curr))
		{
			if(distance <= EndLeavesBlock.LEAF_SUSTAIN_DISTANCE)
			{
				setLeaf(level, curr);
				leaves(level, curr.south(), distance + 1);
				leaves(level, curr.north(), distance + 1);
				leaves(level, curr.above(), distance + 1);
				leaves(level, curr.below(), distance + 1);
				leaves(level, curr.east(), distance + 2);
				leaves(level, curr.west(), distance + 2);
			}
		}
	}
	
	private void setLog(WorldGenLevel level, BlockPos pos)
	{
		setLog(level, pos, Direction.Axis.Y);
	}
	
	private void setLog(WorldGenLevel level, BlockPos pos, Direction.Axis axis)
	{
		if(TreeFeature.validTreePos(level, pos))
		{
			BlockState log = MSBlocks.END_LOG.get().defaultBlockState().setValue(DoubleLogBlock.AXIS_2, axis);
			level.setBlock(pos, log, Block.UPDATE_KNOWN_SHAPE + Block.UPDATE_CLIENTS + Block.UPDATE_NEIGHBORS);
		}
	}
	
	private void setLeaf(WorldGenLevel level, BlockPos pos)
	{
		if(TreeFeature.validTreePos(level, pos))
			level.setBlock(pos, MSBlocks.END_LEAVES.get().defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
	}
}