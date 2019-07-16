package com.mraof.minestuck.world.gen.feature;

import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;

public class EndTree extends Tree
{
	private static final BlockState LOG = MinestuckBlocks.END_LOG.getDefaultState();
	private static final BlockState LEAF = MinestuckBlocks.END_LEAVES.getDefaultState();
	
	private int minMax;
	private int maxMax;
	
	public EndTree()
	{
		this(5, 5);
	}
	
	public EndTree(int minimumMaxAge, int maximumMaxAge)
	{
	}
	
	@Nullable
	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random)
	{
		return null;
	}
	
	/*@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		boolean flag = true;
		
		if (position.getY() >= 1 && position.getY() < 256)
		{
			IBlockState soil = worldIn.getBlockState(position.down());
			if(soil.getBlock() != Blocks.END_STONE && soil.getBlock() != MinestuckBlocks.endGrass && soil.getBlock() != MinestuckBlocks.coarseEndStone)
			{
				return false;
			}
			
			if(subGenerate(worldIn, rand, position, position, BlockEndLog.LEAF_SUSTAIN_DISTANCE, 0, rand.nextInt(maxMax - minMax + 1) + minMax - 1))
			{
				worldIn.setBlockState(position, LOG);
				return true;
			}
		}
		return false;
	}
	
	//The point of using this algorithm, pretty much copy-pasted from the code on chorus plants, was to make these trees more reminiscent of chorus plants.
	//As it stands, however, they don't branch horizontally, the way a chorus plant would.
	//It's not necessary to fix this for end trees to exist and be enjoyed, but fixing it would be a good idea.
	private boolean subGenerate(World worldIn, Random rand, BlockPos curr, BlockPos origin, int range, int age, int maxAge)
	{
		int i = rand.nextInt(Math.max(1, 4 - age)) + 1;
		
		if (age == 0)
		{
			++i;
		}
		
		for (int j = 1; j < i; ++j)
		{
			BlockPos blockpos = curr.up(j);
			
			if (!areAllNeighborsEmpty(worldIn, blockpos, (EnumFacing)null))
			{
				return false;
			}
			
			worldIn.setBlockState(blockpos, LOG, 2);
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
				EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
				BlockPos blockpos1 = curr.up(i).offset(enumfacing);
				
				if (Math.abs(blockpos1.getX() - origin.getX()) < range
						&& Math.abs(blockpos1.getZ() - origin.getZ()) < range
						&& (worldIn.isAirBlock(blockpos1) || worldIn.getBlockState(blockpos1).getBlock() == MinestuckBlocks.endLeaves)
						&& (worldIn.isAirBlock(blockpos1.down()) || worldIn.getBlockState(blockpos1.down()).getBlock() == MinestuckBlocks.endLeaves)
						&& areAllNeighborsEmpty(worldIn, blockpos1, enumfacing.getOpposite()))
				{
					flag = true;
					worldIn.setBlockState(blockpos1, LOG.withProperty(BlockEndLog.SECOND_AXIS, BlockLog.EnumAxis.fromFacingAxis(enumfacing.getAxis())), 2);
					subGenerate(worldIn, rand, blockpos1, origin, range, age + 1, maxAge);
					generateLeaves(worldIn, blockpos1, worldIn.getBlockState(blockpos1));
					blockpos1 = curr.up(i);
					worldIn.setBlockState(blockpos1, worldIn.getBlockState(blockpos1).withProperty(BlockEndLog.SECOND_AXIS, BlockLog.EnumAxis.fromFacingAxis(enumfacing.getAxis())), 2);
				}
			}
		}
		
		if (!flag)
		{
			worldIn.setBlockState(curr.up(i), LOG, 2);
			generateLeaves(worldIn, curr.up(i), worldIn.getBlockState(curr.up(i)));
		}
		return true;
	}
	
	private static boolean areAllNeighborsEmpty(World worldIn, BlockPos pos, EnumFacing excludingSide)
	{
		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
		{
			if (enumfacing != excludingSide && (!(worldIn.isAirBlock(pos.offset(enumfacing)) || worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == MinestuckBlocks.endLeaves)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void generateLeaves(World world, BlockPos pos, IBlockState state)
	{
		EnumAxis primary = state.getValue(LOG_AXIS);
		EnumAxis secondary = state.getValue(SECOND_AXIS);
		
		if(primary == EnumAxis.X || secondary == EnumAxis.X)
		{
			leaves(world, pos.east(), 0);
			leaves(world, pos.west(), 0);
		}
		if(primary == EnumAxis.Y || secondary == EnumAxis.Y)
		{
			leaves(world, pos.up(), 0);
			leaves(world, pos.down(), 0);
		}
		if(primary == EnumAxis.Z || secondary == EnumAxis.Z)
		{
			leaves(world, pos.south(), 0);
			leaves(world, pos.north(), 0);
		}
	}
	
	private void leaves(World world, BlockPos curr, int distance)
	{
		IBlockState blockState = world.getBlockState(curr);
		if(blockState.getBlock().canBeReplacedByLeaves(blockState, world, curr))
		{
			if(distance <= LEAF_SUSTAIN_DISTANCE)
			{
				world.setBlockState(curr, MinestuckBlocks.endLeaves.getDefaultState().withProperty(BlockEndLeaves.DISTANCE, distance), 2);
				leaves(world, curr.south(),	distance + 1);
				leaves(world, curr.north(),	distance + 1);
				leaves(world, curr.up(),	distance + 1);
				leaves(world, curr.down(),	distance + 1);
				leaves(world, curr.east(),	distance + 2);
				leaves(world, curr.west(),	distance + 2);
			}
		} else if (blockState.getBlock() == MinestuckBlocks.endLeaves)
		{
			if(world.getBlockState(curr).getValue(BlockEndLeaves.DISTANCE) > distance)
			{
				world.setBlockState(curr, MinestuckBlocks.endLeaves.getDefaultState().withProperty(BlockEndLeaves.DISTANCE, distance), 2);
				leaves(world, curr.south(),	distance + 1);
				leaves(world, curr.north(),	distance + 1);
				leaves(world, curr.up(),	distance + 1);
				leaves(world, curr.down(),	distance + 1);
				leaves(world, curr.east(),	distance + 2);
				leaves(world, curr.west(),	distance + 2);
			}
		}
	}*/
}
