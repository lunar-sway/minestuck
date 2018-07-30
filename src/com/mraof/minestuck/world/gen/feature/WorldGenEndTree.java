package com.mraof.minestuck.world.gen.feature;

import java.util.HashMap;
import java.util.Random;

import com.mraof.minestuck.block.BlockEndLog;
import com.mraof.minestuck.block.BlockMinestuckLeaves1;
import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.BlockRainbowSapling;
import com.mraof.minestuck.block.MinestuckBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenEndTree extends WorldGenAbstractTree
{
	private static final IBlockState LOG = MinestuckBlocks.endLog.getDefaultState();
	private static final IBlockState LEAF = MinestuckBlocks.endLeaves.getDefaultState();
	
	private int minMax;
	private int maxMax;
	
	public WorldGenEndTree(boolean notify)
	{
		this(notify, 5, 5);
	}
	
	public WorldGenEndTree(boolean notify, int minimumMaxAge, int maximumMaxAge)
	{
		super(notify);
	}
	
	@Override
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
					((BlockEndLog) MinestuckBlocks.endLog).generateLeaves(worldIn, blockpos1, worldIn.getBlockState(blockpos1));
					blockpos1 = curr.up(i);
					worldIn.setBlockState(blockpos1, worldIn.getBlockState(blockpos1).withProperty(BlockEndLog.SECOND_AXIS, BlockLog.EnumAxis.fromFacingAxis(enumfacing.getAxis())), 2);
				}
			}
		}
		
		if (!flag)
		{
			worldIn.setBlockState(curr.up(i), LOG, 2);
			((BlockEndLog) MinestuckBlocks.endLog).generateLeaves(worldIn, curr.up(i), worldIn.getBlockState(curr.up(i)));
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
}
