package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;

public class LandAspectHeat extends LandAspect 
{
	
	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {{Block.slowSand.blockID, 0}};
	}

	@Override
	public int[][] getUpperBlocks() 
	{
		return new int[][] {{Block.netherrack.blockID,0}};
	}

	@Override
	public float getRarity() {
		return 0.5F;
	}

	@Override
	public double[] generateTerrainMap() 
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
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVien(Block.slowSand.blockID, 10, 32));
		list.add(new DecoratorVien(Block.glowStone.blockID, 5, 8));
		return list;
	}


}
