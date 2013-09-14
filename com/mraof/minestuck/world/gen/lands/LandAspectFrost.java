package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;

public class LandAspectFrost extends LandAspect 
{
	
	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {{Block.snow.blockID, 0}};
	}

	@Override
	public int[][] getUpperBlocks() 
	{
		return new int[][] {{Block.stone.blockID,0}};
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
	public String getPrimaryName() {
		return "Frost";
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"Frost","Cold","Ice"};
	}
	
	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Block.dirt.blockID, 10, 32));
		list.add(new DecoratorVein(Block.ice.blockID, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		//return (new Random()).nextInt(3); //Random cycle between 0 and 2
		return 2;
	}

}
