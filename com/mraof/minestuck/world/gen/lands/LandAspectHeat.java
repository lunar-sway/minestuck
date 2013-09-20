package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;

public class LandAspectHeat extends LandAspect 
{
	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {{Block.slowSand.blockID, Block.cobblestone.blockID, 0}};
	}

	@Override
	public int[][] getUpperBlocks() 
	{
		return new int[][] {{Block.netherrack.blockID, Block.obsidian.blockID, 0}};
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
		list.add(new DecoratorVein(Block.slowSand.blockID, 10, 32));
		list.add(new DecoratorVein(Block.glowStone.blockID, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return (new Random()).nextInt(3); //Random cycle between 0 and 2
	}


}
