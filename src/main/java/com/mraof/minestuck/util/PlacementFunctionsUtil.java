package com.mraof.minestuck.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.util.Constants;

public class PlacementFunctionsUtil
{
	public static void createPlainSpiralStaircase(BlockPos minBlockPos, BlockPos maxBlockPos, BlockState blockState, IWorld world, MutableBoundingBox boundingBox)
	{
		int xIterator = minBlockPos.getX();
		int yIterator = minBlockPos.getZ();
		int zIterator = minBlockPos.getY();
		
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
					
					if(xIterator == maxBlockPos.getX())
					{
						isXIterating = false;
					} else if(zIterator == maxBlockPos.getZ())
					{
						isXIterating = true;
					}
					
					Debug.debugf("placementPos = %s, isXIterating = %s, isPosInsideBounding = %s", placementPos, isXIterating, boundingBox.isVecInside(placementPos));
					
					if(xPos == xIterator && yPos == yIterator && zPos == zIterator && boundingBox.isVecInside(placementPos))
					{
						Debug.debugf("placed at %s", placementPos);
						world.setBlockState(placementPos, blockState, Constants.BlockFlags.BLOCK_UPDATE);
					}
					yIterator++;
					
					if(isXIterating)
						xIterator = xIterator + xIteratingReverse;
					else
						zIterator = zIterator + zIteratingReverse;
				}
			}
		}
	}
	
	/*
	public static void createPlainRoundStaircase(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState)
	{
	
	}
	 */
}
