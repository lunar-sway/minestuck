package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.plant.DoubleLogBlock;
import com.mraof.minestuck.block.plant.EndLeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;

import java.util.Random;


public class EndTreeFeature extends Feature<NoFeatureConfig>
{
	
	public EndTreeFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos position, NoFeatureConfig config)
	{
		BlockPos soilPos = position.below();
		if(world.getBlockState(soilPos).canSustainPlant(world, soilPos, Direction.UP, MSBlocks.END_SAPLING))
		{
			if(subGenerate(world, rand, position, position, EndLeavesBlock.LEAF_SUSTAIN_DISTANCE, 0, 4))
			{
				setLog(world, position);
				return true;
			}
		}
		return false;
	}
	
	//The point of using this algorithm, pretty much copy-pasted from the code on chorus plants, was to make these trees more reminiscent of chorus plants.
	//As it stands, however, they don't branch horizontally, the way a chorus plant would.
	//It's not necessary to fix this for end trees to exist and be enjoyed, but fixing it would be a good idea.
	private boolean subGenerate(ISeedReader world, Random rand, BlockPos curr, BlockPos origin, int range, int step, int maxSteps)
	{
		int height = rand.nextInt(Math.max(1, 4 - step)) + 1;
		
		if(step == 0)
			height++;
		
		for(int y = 1; y < height; y++)
			if(!areAllNeighborsEmpty(world, curr.above(y), null))
				return false;
		
		for(int y = 1; y < height; y++)
			setLog(world, curr.above(y));
		
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
						&& TreeFeature.isAirOrLeaves(world, nextPos)
						&& TreeFeature.isAirOrLeaves(world, nextPos.below())
						&& areAllNeighborsEmpty(world, nextPos, direction.getOpposite()))
				{
					flag = true;
					Direction.Axis axis = direction.getAxis();
					setLog(world, nextPos, axis);
					subGenerate(world, rand, nextPos, origin, range, step + 1, maxSteps);
					generateLeaves(world, nextPos, Direction.Axis.Y, axis);
					nextPos = curr.above(height);
					setLog(world, nextPos, axis);
				}
			}
		}
		
		if (!flag)
		{
			generateLeaves(world, curr.above(height), Direction.Axis.Y, Direction.Axis.Y);
			setLog(world, curr.above(height));
		}
		return true;
	}
	
	private static boolean areAllNeighborsEmpty(IWorldGenerationReader worldIn, BlockPos pos, Direction excludingSide)
	{
		for (Direction direction : Direction.Plane.HORIZONTAL)
		{
			if (direction != excludingSide && !TreeFeature.isAirOrLeaves(worldIn, pos.relative(direction)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void generateLeaves(ISeedReader world, BlockPos pos, Direction.Axis primary, Direction.Axis secondary)
	{
		if(primary == Direction.Axis.X || secondary == Direction.Axis.X)
		{
			leaves(world, pos.east(), 2);
			leaves(world, pos.west(), 2);
		}
		if(primary == Direction.Axis.Y || secondary == Direction.Axis.Y)
		{
			leaves(world, pos.above(), 1);
			leaves(world, pos.below(), 1);
		}
		if(primary == Direction.Axis.Z || secondary == Direction.Axis.Z)
		{
			leaves(world, pos.south(), 1);
			leaves(world, pos.north(), 1);
		}
	}
	
	private void leaves(ISeedReader world, BlockPos curr, int distance)
	{
		if(TreeFeature.isAirOrLeaves(world, curr))
		{
			if(distance <= EndLeavesBlock.LEAF_SUSTAIN_DISTANCE)
			{
				setLeaf(world, curr);
				leaves(world, curr.south(), distance + 1);
				leaves(world, curr.north(), distance + 1);
				leaves(world, curr.above(), distance + 1);
				leaves(world, curr.below(), distance + 1);
				leaves(world, curr.east(), distance + 2);
				leaves(world, curr.west(), distance + 2);
			}
		}
	}
	
	private void setLog(ISeedReader world, BlockPos pos)
	{
		setLog(world, pos, Direction.Axis.Y);
	}
	
	private void setLog(ISeedReader world, BlockPos pos, Direction.Axis axis)
	{
		if(TreeFeature.validTreePos(world, pos))
		{
			BlockState log = MSBlocks.END_LOG.defaultBlockState().setValue(DoubleLogBlock.AXIS_2, axis);
			TreeFeature.setBlockKnownShape(world, pos, log);
		}
	}
	
	private void setLeaf(ISeedReader world, BlockPos pos)
	{
		if(TreeFeature.validTreePos(world, pos))
			TreeFeature.setBlockKnownShape(world, pos, MSBlocks.END_LEAVES.defaultBlockState());
	}
}