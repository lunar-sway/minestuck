package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.plant.DoubleLogBlock;
import com.mraof.minestuck.block.plant.EndLeavesBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class EndTreeFeature extends AbstractTreeFeature<TreeFeatureConfig>
{
	public EndTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected boolean place(IWorldGenerationReader world, Random rand, BlockPos position, Set<BlockPos> logBlocks, Set<BlockPos> foliageBlocks, MutableBoundingBox boundingBox, TreeFeatureConfig config)
	{
		if(isSoil(world, position.down(), config.getSapling()))
		{
			if(subGenerate(world, rand, position, position, logBlocks, foliageBlocks, boundingBox, EndLeavesBlock.LEAF_SUSTAIN_DISTANCE, 0, 4, config))
			{
				setDirtAt(world, position.down(), position);
				setLog(world, rand, position, logBlocks, boundingBox, config);
				return true;
			}
		}
		return false;
	}
	
	//The point of using this algorithm, pretty much copy-pasted from the code on chorus plants, was to make these trees more reminiscent of chorus plants.
	//As it stands, however, they don't branch horizontally, the way a chorus plant would.
	//It's not necessary to fix this for end trees to exist and be enjoyed, but fixing it would be a good idea.
	private boolean subGenerate(IWorldGenerationReader world, Random rand, BlockPos curr, BlockPos origin, Set<BlockPos> logBlocks, Set<BlockPos> foliageBlocks, MutableBoundingBox bounds, int range, int step, int maxSteps, BaseTreeFeatureConfig config)
	{
		int height = rand.nextInt(Math.max(1, 4 - step)) + 1;
		
		if(step == 0)
			height++;
		
		for(int y = 1; y < height; y++)
			if(!areAllNeighborsEmpty(world, curr.up(y), null))
				return false;
		
		for(int y = 1; y < height; y++)
			setLog(world, rand, curr.up(y), logBlocks, bounds, config);
		
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
				Direction direction = Direction.Plane.HORIZONTAL.random(rand);
				BlockPos nextPos = curr.up(height).offset(direction);
				
				if (Math.abs(nextPos.getX() - origin.getX()) < range
						&& Math.abs(nextPos.getZ() - origin.getZ()) < range
						&& isAirOrLeaves(world, nextPos)
						&& isAirOrLeaves(world, nextPos.down())
						&& areAllNeighborsEmpty(world, nextPos, direction.getOpposite()))
				{
					flag = true;
					Direction.Axis axis = direction.getAxis();
					setLogWithAxis2(axis, world, rand, nextPos, logBlocks, bounds, config);
					subGenerate(world, rand, nextPos, origin, logBlocks, foliageBlocks, bounds, range, step + 1, maxSteps, config);
					generateLeaves(world, rand, nextPos, foliageBlocks, bounds, config, Direction.Axis.Y, axis);
					nextPos = curr.up(height);
					setLogWithAxis2(axis, world, rand, nextPos, logBlocks, bounds, config);
				}
			}
		}
		
		if (!flag)
		{
			setLog(world, rand, curr.up(height), logBlocks, bounds, config);
			generateLeaves(world, rand, curr.up(height), foliageBlocks, bounds, config, Direction.Axis.Y, Direction.Axis.Y);
		}
		return true;
	}
	
	protected boolean setLogWithAxis2(Direction.Axis axis, IWorldGenerationReader world, Random rand, BlockPos pos, Set<BlockPos> logBlocks, MutableBoundingBox bounds, BaseTreeFeatureConfig config)
	{
		if(!isAirOrLeaves(world, pos) && !isTallPlants(world, pos) && !isWater(world, pos))
			return false;
		else
		{
			setBlockState(world, pos, config.trunkProvider.getBlockState(rand, pos).with(DoubleLogBlock.AXIS_2, axis), bounds);
			logBlocks.add(pos.toImmutable());
			return true;
		}
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
	
	public void generateLeaves(IWorldGenerationReader world, Random rand, BlockPos pos, Set<BlockPos> changedBlocks, MutableBoundingBox boundsIn, BaseTreeFeatureConfig config, Direction.Axis primary, Direction.Axis secondary)
	{
		if(primary == Direction.Axis.X || secondary == Direction.Axis.X)
		{
			leaves(world, rand, pos.east(), changedBlocks, boundsIn, config, 1);
			leaves(world, rand, pos.west(), changedBlocks, boundsIn, config, 1);
		}
		if(primary == Direction.Axis.Y || secondary == Direction.Axis.Y)
		{
			leaves(world, rand, pos.up(), changedBlocks, boundsIn, config, 1);
			leaves(world, rand, pos.down(), changedBlocks, boundsIn, config, 1);
		}
		if(primary == Direction.Axis.Z || secondary == Direction.Axis.Z)
		{
			leaves(world, rand, pos.south(), changedBlocks, boundsIn, config, 1);
			leaves(world, rand, pos.north(), changedBlocks, boundsIn, config, 1);
		}
	}
	
	private void leaves(IWorldGenerationReader world, Random rand, BlockPos curr, Set<BlockPos> changedBlocks, MutableBoundingBox boundsIn, BaseTreeFeatureConfig config, int distance)
	{
		if(isAirOrLeaves(world, curr))
		{
			if(distance <= EndLeavesBlock.LEAF_SUSTAIN_DISTANCE)
			{
				setLeaf(world, rand, curr, changedBlocks, boundsIn, config);
				leaves(world, rand, curr.south(), changedBlocks, boundsIn, config, distance + 1);
				leaves(world, rand, curr.north(), changedBlocks, boundsIn, config, distance + 1);
				leaves(world, rand, curr.up(), changedBlocks, boundsIn, config, distance + 1);
				leaves(world, rand, curr.down(), changedBlocks, boundsIn, config, distance + 1);
				leaves(world, rand, curr.east(), changedBlocks, boundsIn, config, distance + 2);
				leaves(world, rand, curr.west(), changedBlocks, boundsIn, config, distance + 2);
			}
		}
	}
}