package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.util.Title;

import net.minecraft.block.Block;

public class LandAspectHeat extends LandAspect 
{
	
	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {
				{Block.netherrack.blockID, 0},
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
	public float getRarity(Title playerTitle) {
		if (playerTitle.getHeroClass() == 6) { 
			return (long) 0.75; //Only if player's a Knight
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
	
	@Override
	public int getOceanBlock()
	{
		return Block.lavaStill.blockID;
	}
	
	@Override
	public String getPrimaryName() {
		return "Heat";
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"Heat","Flame","Fire"};
	}

}
