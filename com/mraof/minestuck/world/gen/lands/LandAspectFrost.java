package com.mraof.minestuck.world.gen.lands;

import net.minecraft.block.Block;

public class LandAspectFrost extends LandAspect 
{
	
	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {
				{Block.snow.blockID, 0},
				{0, 0}
		};
	}

	@Override
	public int[][] getUpperBlocks() 
	{
		return new int[][] {{Block.stone.blockID, Block.dirt.blockID},
		{0, 0}};
	}

}
