package com.mraof.minestuck.world.gen.lands.decorator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class SimpleStructureDecorator implements ILandDecorator
{
	
	protected boolean rotation;
	protected int xCoord, yCoord, zCoord;
	
	protected void placeBlocks(World world, IBlockState block, int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
	{
		for(int x = fromX; x <= toX; x++)
			for(int y = fromY; y <= toY; y++)
				for(int z = fromZ; z <= toZ; z++)
					placeBlock(world, block, x, y, z);
	}
	
	protected void placeBlock(World world, IBlockState block, int xOffset, int yOffset, int zOffset)
	{
		int xPos = xCoord + (rotation ? zOffset : xOffset);
		int yPos = yCoord + yOffset;
		int zPos = zCoord + (rotation ? xOffset : zOffset);
		
		world.setBlockState(new BlockPos(xPos, yPos, zPos), block, 2);
	}
	
}
