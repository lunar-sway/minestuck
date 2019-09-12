package com.mraof.minestuck.world.gen.feature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.DoubleLogBlock;
import com.mraof.minestuck.block.EndLeavesBlock;
import com.mraof.minestuck.block.MinestuckBlocks;

import com.mraof.minestuck.util.MinestuckTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class EndTreeFeature extends AbstractTreeFeature<NoFeatureConfig>
{
	private static final BlockState LOG = MinestuckBlocks.END_LOG.getDefaultState();
	private static final BlockState LEAF = MinestuckBlocks.END_LEAVES.getDefaultState();
	
	private final int minMax = 5, maxMax = 5;
	
	public EndTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean doBlockNotify)
	{
		super(configFactory, doBlockNotify);
	}
	
	@Override
	protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundsIn)
	{
		boolean flag = true;
		
		if (position.getY() >= 1 && position.getY() < 256)
		{
			if(!worldIn.hasBlockState(position.down(), blockState -> MinestuckTags.Blocks.END_SAPLING_DIRT.contains(blockState.getBlock())))
			{
				return false;
			}
			
			if(subGenerate(changedBlocks, worldIn, rand, position, position, boundsIn, EndLeavesBlock.LEAF_SUSTAIN_DISTANCE, 0, rand.nextInt(maxMax - minMax + 1) + minMax - 1))
			{
				setLogState(changedBlocks, worldIn, position, LOG, boundsIn);
				return true;
			}
		}
		return false;
	}
	
	//The point of using this algorithm, pretty much copy-pasted from the code on chorus plants, was to make these trees more reminiscent of chorus plants.
	//As it stands, however, they don't branch horizontally, the way a chorus plant would.
	//It's not necessary to fix this for end trees to exist and be enjoyed, but fixing it would be a good idea.
	private boolean subGenerate(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos curr, BlockPos origin, MutableBoundingBox boundsIn, int range, int age, int maxAge)
	{
		int i = rand.nextInt(Math.max(1, 4 - age)) + 1;
		
		if (age == 0)
		{
			++i;
		}
		
		for (int j = 1; j < i; ++j)
		{
			BlockPos logPos = curr.up(j);
			
			if (!areAllNeighborsEmpty(worldIn, logPos, null))
			{
				return false;
			}
			
			setLogState(changedBlocks, worldIn, logPos, LOG, boundsIn);
		}
		
		boolean flag = false;
		
		if (age < maxAge)
		{
			int buds = rand.nextInt(4);
			
			if (age == 0)
			{
				++buds;
			}
			
			for (int k = 0; k < buds; ++k)		//TODO: Change this to prioritize north/south growth over east/west growth, and to lock growth into one axis
			{
				Direction direction = Direction.Plane.HORIZONTAL.random(rand);
				BlockPos nextPos = curr.up(i).offset(direction);
				
				if (Math.abs(nextPos.getX() - origin.getX()) < range
						&& Math.abs(nextPos.getZ() - origin.getZ()) < range
						&& isAirOrLeaves(worldIn, nextPos)
						&& isAirOrLeaves(worldIn, nextPos.down())
						&& areAllNeighborsEmpty(worldIn, nextPos, direction.getOpposite()))
				{
					flag = true;
					BlockState logState = LOG.with(DoubleLogBlock.AXIS_2, direction.getAxis());
					setLogState(changedBlocks, worldIn, nextPos, logState, boundsIn);
					subGenerate(changedBlocks, worldIn, rand, nextPos, origin, boundsIn, range, age + 1, maxAge);
					generateLeaves(changedBlocks, worldIn, nextPos, logState, boundsIn);
					nextPos = curr.up(i);
					setLogState(changedBlocks, worldIn, nextPos, LOG.with(DoubleLogBlock.AXIS_2, direction.getAxis()), boundsIn);
				}
			}
		}
		
		if (!flag)
		{
			setLogState(changedBlocks, worldIn, curr.up(i), LOG, boundsIn);
			generateLeaves(changedBlocks, worldIn, curr.up(i), LOG, boundsIn);
		}
		return true;
	}
	
	private static boolean areAllNeighborsEmpty(IWorldGenerationReader worldIn, BlockPos pos, Direction excludingSide)
	{
		for (Direction direction : Direction.Plane.HORIZONTAL)
		{
			if (direction != excludingSide && !isAirOrLeaves(worldIn, pos.offset(direction)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void generateLeaves(Set<BlockPos> changedBlocks, IWorldGenerationReader world, BlockPos pos, BlockState logState, MutableBoundingBox boundsIn)
	{
		Direction.Axis primary = logState.get(DoubleLogBlock.AXIS);
		Direction.Axis secondary = logState.get(DoubleLogBlock.AXIS_2);
		
		if(primary == Direction.Axis.X || secondary == Direction.Axis.X)
		{
			leaves(changedBlocks, world, pos.east(), boundsIn, 1);
			leaves(changedBlocks, world, pos.west(), boundsIn, 1);
		}
		if(primary == Direction.Axis.Y || secondary == Direction.Axis.Y)
		{
			leaves(changedBlocks, world, pos.up(), boundsIn, 1);
			leaves(changedBlocks, world, pos.down(), boundsIn, 1);
		}
		if(primary == Direction.Axis.Z || secondary == Direction.Axis.Z)
		{
			leaves(changedBlocks, world, pos.south(), boundsIn, 1);
			leaves(changedBlocks, world, pos.north(), boundsIn, 1);
		}
	}
	
	private void leaves(Set<BlockPos> changedBlocks, IWorldGenerationReader world, BlockPos curr, MutableBoundingBox boundsIn, int distance)
	{
		if(isAirOrLeaves(world, curr))
		{
			if(distance <= EndLeavesBlock.LEAF_SUSTAIN_DISTANCE)
			{
				setLogState(changedBlocks, world, curr, MinestuckBlocks.END_LEAVES.getDefaultState().with(EndLeavesBlock.DISTANCE, distance), boundsIn);
				leaves(changedBlocks, world, curr.south(), boundsIn, distance + 1);
				leaves(changedBlocks, world, curr.north(), boundsIn, distance + 1);
				leaves(changedBlocks, world, curr.up(), boundsIn, distance + 1);
				leaves(changedBlocks, world, curr.down(), boundsIn, distance + 1);
				leaves(changedBlocks, world, curr.east(), boundsIn, distance + 2);
				leaves(changedBlocks, world, curr.west(), boundsIn, distance + 2);
			}
		}
	}
}