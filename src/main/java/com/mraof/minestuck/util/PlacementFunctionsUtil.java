package com.mraof.minestuck.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.common.util.Constants;

public class PlacementFunctionsUtil
{
	/**
	 * takes in two blockpos, resorts the coordinates to match the positive direction. Within the region it starts at 0,0,0
	 */
	public static void createPlainSpiralStaircase(BlockPos minBlockPosIn, BlockPos maxBlockPosIn, BlockState blockState, IWorld world, MutableBoundingBox boundingBox1, MutableBoundingBox boundingBox2)
	{
		//TODO placement happens twice(because of two bounding boxes?), stairs can go beyond intended x/z bounds, stairs are fragmented(because it tries to generate in two bounding boxes?), stairs do not reverse
		int staircaseMinX = Math.min(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int staircaseMinY = Math.min(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int staircaseMinZ = Math.min(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		int staircaseMaxX = Math.max(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int staircaseMaxY = Math.max(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int staircaseMaxZ = Math.max(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		
		BlockPos minBlockPos = new BlockPos(staircaseMinX, staircaseMinY, staircaseMinZ); //resorts the two blockpos so that it doesnt try to count from positive to negative
		BlockPos maxBlockPos = new BlockPos(staircaseMaxX, staircaseMaxY, staircaseMaxZ);
		
		world.setBlockState(minBlockPos, Blocks.GOLD_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		world.setBlockState(maxBlockPos, Blocks.DIAMOND_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
		
		Debug.debugf("boundingBox = %s", boundingBox1);
		int xIterator = minBlockPos.getX();
		
		int zIterator = minBlockPos.getZ();
		
		boolean isXIterating = true; //spiral staircase starts moving up and in the positive x direction, but it switches to z once its hit the edge
		int xIteratingReverse = 1;
		int zIteratingReverse = 1;
		for(int yIterator = minBlockPos.getY(); yIterator <= maxBlockPos.getY(); yIterator++)
		{
			//BlockPos placementPos = new BlockPos(xPos, yPos, zPos);
			BlockPos iteratorPos = new BlockPos(xIterator, yIterator, zIterator);
			
			Debug.debugf("isXIterating = %s, isPosInsideBounding = %s, iteratorPos = %s", isXIterating, boundingBox1.isVecInside(iteratorPos), iteratorPos);
			
			if(xIterator >= maxBlockPos.getX())
			{
				isXIterating = false;
			} else if(zIterator >= maxBlockPos.getZ())
			{
				isXIterating = true;
			}
			
			if(boundingBox1.isVecInside(iteratorPos) || boundingBox2.isVecInside(iteratorPos))
			{
				Debug.debugf("placed at %s", iteratorPos);
				world.setBlockState(iteratorPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
				//
				
				//yIterator++;
				
				if(isXIterating)
					xIterator = xIterator + xIteratingReverse;
				else
					zIterator = zIterator + zIteratingReverse;
			}
			
			if(yIterator >= maxBlockPos.getY())
			{
				break;
			}
		}
		
		//boundingBox.offset(0, minBlockPosIn.getY() - boundingBox.minY, 0);
		/*int staircaseMinX = Math.min(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int staircaseMinY = Math.min(minBlockPosIn.getY(), maxBlockPosIn.getY());
		int staircaseMinZ = Math.min(minBlockPosIn.getZ(), maxBlockPosIn.getZ());
		int staircaseMaxX = Math.max(minBlockPosIn.getX(), maxBlockPosIn.getX());
		int staircaseMaxY = Math.max(minBlockPosIn.getY(), maxBlockPosIn.getY());;
		int staircaseMaxZ = Math.max(minBlockPosIn.getZ(), maxBlockPosIn.getZ());;
		
		BlockPos minBlockPos = new BlockPos(staircaseMinX, staircaseMinY, staircaseMinZ); //resorts the two blockpos so that it doesnt try to count from positive to negative
		BlockPos maxBlockPos = new BlockPos(staircaseMaxX, staircaseMaxY, staircaseMaxZ);
		
		Debug.debugf("boundingBox = %s", boundingBox);
		int xIterator = minBlockPos.getX();
		int yIterator = minBlockPos.getY();
		int zIterator = minBlockPos.getZ();
		
		boolean isXIterating = true; //spiral staircase starts moving up and in the positive x direction, but it switches to z once its hit the edge
		int xIteratingReverse = 1;
		int zIteratingReverse = 1;
		for(int xPos = minBlockPos.getX(); xPos <= maxBlockPos.getX(); xPos++)
		{
			for(int yPos = minBlockPos.getY(); yPos <= maxBlockPos.getY(); yPos++)
			{
				for(int zPos = minBlockPos.getZ(); zPos <= maxBlockPos.getZ(); zPos++)
				{
					BlockPos placementPos = new BlockPos(xPos, yPos, zPos);
					BlockPos iteratorPos = new BlockPos(xIterator, yIterator, zIterator);
					
					Debug.debugf("placementPos = %s, isXIterating = %s, isPosInsideBounding = %s, iteratorPosSet = %s, placement same as iterator = %s", placementPos, isXIterating, boundingBox.isVecInside(placementPos), iteratorPos, placementPos == iteratorPos);
					
					if(placementPos == iteratorPos/* && boundingBox.isVecInside(placementPos)*//*)
					{
						if(xIterator >= maxBlockPos.getX())
						{
							isXIterating = false;
						} else if(zIterator >= maxBlockPos.getZ())
						{
							isXIterating = true;
						}
						
						
						Debug.debugf("placed at %s", placementPos);
						world.setBlockState(placementPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
						
						
						yIterator++;
						
						if(isXIterating)
							xIterator = xIterator + xIteratingReverse;
						else
							zIterator = zIterator + zIteratingReverse;
					}
					
					if(yIterator >= maxBlockPos.getY())
					{
						break;
					}
				}
			}
		}*/
	}
	
	/*
	public static void createPlainRoundStaircase(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState)
	{
	
	}
	 */
	
	public static void fillWithBlocksFromPos(IWorld worldIn, MutableBoundingBox structurebb, BlockState blockState, BlockPos minBlockPos, BlockPos maxBlockPos)
	{
		for(int y = minBlockPos.getY(); y <= maxBlockPos.getY(); ++y)
		{
			for(int x = minBlockPos.getX(); x <= maxBlockPos.getX(); ++x)
			{
				for(int z = minBlockPos.getZ(); z <= maxBlockPos.getZ(); ++z)
				{
					BlockPos currentPos = new BlockPos(x,y,z);
					if(structurebb.isVecInside(currentPos))
					{
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
