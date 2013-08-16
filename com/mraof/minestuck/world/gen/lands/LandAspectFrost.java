package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.util.Title;

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

	@Override
	public float getWeight(Title playerTitle) {
		if (playerTitle.getHeroClass() == 10) { 
			return (long) 0.75; //Only if player's a Witch
		} else {
			return (long) 0.25;
		}
	}

	@Override
	public double[] generateMainTerrainMap() 
	{
		return null;
	}

	@Override
	public double[] generateMinorTerrainMap() 
	{
		return null;
	}

}
