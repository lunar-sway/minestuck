package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import com.mraof.minestuck.util.Title;

import net.minecraft.block.Block;

public class LandAspectHeat extends LandAspect 
{
	
	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {
				{Block.slowSand.blockID, 0},
				{0, 0}
		};
	}

	@Override
	public int[][] getUpperBlocks() 
	{
		return new int[][] {{Block.netherrack.blockID, Block.glowStone.blockID},
		{0, 0}};
	}

	@Override
	public float getRarity() {
		return 0.5F;
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
	
	@Override
	public ArrayList getDecorators() {
		return new ArrayList();
	}


}
