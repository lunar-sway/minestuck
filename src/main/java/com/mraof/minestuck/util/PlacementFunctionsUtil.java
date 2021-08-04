package com.mraof.minestuck.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;

/**
 * Functions in this often require using get[X/Y/Z]WithOffset
 */
public class PlacementFunctionsUtil
{
	/**
	 * Use reordered min and max blockpos. Will start from min blockpos and start building up in the positive x direction first
	 */
	public static void createPlainSpiralStaircase(BlockPos minBlockPosIn, BlockPos maxBlockPosIn, BlockState blockState, IWorld world, MutableBoundingBox boundingBox1, MutableBoundingBox boundingBox2)
	{
		//TODO placement happens twice(because of two bounding boxes?), one set of stairs can go beyond intended x/z bounds, stairs are fragmented(because it tries to generate in two bounding boxes?)
		
		world.setBlockState(minBlockPosIn, Blocks.GOLD_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		world.setBlockState(maxBlockPosIn, Blocks.DIAMOND_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		
		//Debug.debugf("createPlainSpiralStaircase. boundingBox = %s", boundingBox1);
		int xIterator = minBlockPosIn.getX();
		
		int zIterator = minBlockPosIn.getZ();
		
		boolean isXIterating = true; //toggles between moving in x or z direction
		boolean isXIterateReversed = false; //moving in negative x direction if positive
		boolean isZIterateReversed = false; //moving in negative z direction if positive
		for(int yIterator = minBlockPosIn.getY(); yIterator <= maxBlockPosIn.getY(); yIterator++)
		{
			BlockPos iteratorPos = new BlockPos(xIterator, yIterator, zIterator);
			
			/*Debug.debugf("createPlainSpiralStaircase. isXIterating = %s, isPosInsideBounding = %s, iteratorPos = %s",
					isXIterating, boundingBox1.isVecInside(iteratorPos), iteratorPos);*/
			
			if(xIterator >= maxBlockPosIn.getX() && !isXIterateReversed) //moving positive x
			{
				isXIterating = false;
				isXIterateReversed = true;
			} else if(zIterator >= maxBlockPosIn.getZ() && !isZIterateReversed) //moving positive z
			{
				isXIterating = true;
				isZIterateReversed = true;
			} else if(xIterator <= minBlockPosIn.getX() && isXIterateReversed) //moving negative x
			{
				isXIterating = false;
				isXIterateReversed = false;
			} else if(zIterator <= minBlockPosIn.getZ() && isZIterateReversed) //moving negative z
			{
				isXIterating = true;
				isZIterateReversed = true;
			}
			
			if(boundingBox1.isVecInside(iteratorPos) || boundingBox2.isVecInside(iteratorPos))
			{
				//Debug.debugf("createPlainSpiralStaircase. placed at %s", iteratorPos);
				world.setBlockState(iteratorPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
				
				if(isXIterating && !isXIterateReversed)
				{
					xIterator++;
				} else if(!isXIterating && !isZIterateReversed)
				{
					zIterator++;
				} else if(isXIterating && isXIterateReversed)
				{
					xIterator--;
				} else if(!isXIterating && isZIterateReversed)
				{
					zIterator--;
				}
				/*
				if(isXIterating && !isXIterateReversed)
				{
					xIterator++;
				}
				if(!isXIterating && !isZIterateReversed)
				{
					zIterator++;
				}
				if(isXIterating && isXIterateReversed)
				{
					xIterator--;
				}
				if(!isXIterating && isZIterateReversed)
				{
					zIterator--;
				}
				 */
			}
			
			if(yIterator >= maxBlockPosIn.getY())
			{
				break;
			}
		}
	}
	
	/**
	 * Use reordered blockpos for min and max BlockPos parameters(axisAlignBlockPosGetMin/Max)
	 */
	public static void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos)
	{
		for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); ++y)
		{
			for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); ++x)
			{
				for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					if(structurebb.isVecInside(currentPos))
					{
						worldIn.setBlockState(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	/*public static void fillWithAirCheckWater(IWorld worldIn, MutableBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		for(int y = minY; y <= maxY; ++y)
		{
			for(int x = minX; x <= maxX; ++x)
			{
				for(int z = minZ; z <= maxZ; ++z)
				{
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
					if(structurebb.isVecInside(pos) && !this.getBlockStateFromPos(worldIn, x, y, z, structurebb).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //ensures that the chunk is loaded before attempted to remove block, setBlockState already does this check
						worldIn.removeBlock(pos, false);
				}
			}
		}
	}*/
	
	/**
	 * normal trimmed down fill command except that it waterlogs blocks if it is replacing water, blockState parameter must be waterloggable
	 */
	public static void fillWithBlocksCheckWater(IWorld worldIn, MutableBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					BlockPos currentPos = new BlockPos(x, y, z);
					//Debug.debugf("fillWithBlocksCheckWater. currentPos = %s, bb = %s", currentPos, boundingboxIn);
					if(boundingboxIn.isVecInside(currentPos))
					{
						if(worldIn.getBlockState(currentPos).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //has no inside vs outside blockstates or existingOnly
							blockState = blockState.with(BlockStateProperties.WATERLOGGED, true); //only works with waterloggable blocks
						worldIn.setBlockState(currentPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}
		}
	}
	
	public static BlockPos axisAlignBlockPosGetMin(BlockPos minBlockPosIn, BlockPos maxBlockPosIn)
	{
		int blockPosMinX = Math.min(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int blockPosMinY = Math.min(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int blockPosMinZ = Math.min(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		BlockPos minBlockPos = new BlockPos(blockPosMinX, blockPosMinY, blockPosMinZ);
		
		return minBlockPos;
	}
	
	public static BlockPos axisAlignBlockPosGetMax(BlockPos minBlockPosIn, BlockPos maxBlockPosIn)
	{
		int blockPosMaxX = Math.max(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int blockPosMaxY = Math.max(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int blockPosMaxZ = Math.max(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		BlockPos maxBlockPos = new BlockPos(blockPosMaxX, blockPosMaxY, blockPosMaxZ);
		
		return maxBlockPos;
	}
}
