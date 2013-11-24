package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;

import com.mraof.minestuck.Minestuck;

public class LandAspectPulse extends LandAspect {

	@Override
	public int[][] getSurfaceBlocks() 
	{
		return new int[][] {{Block.obsidian.blockID, 0}};
	}

	@Override
	public int[][] getUpperBlocks() {
		return new int[][] {{Block.netherrack.blockID, 0}};
	}
	
	@Override
	public int getOceanBlock() 
	{
		return Minestuck.blockBlood.blockID;
	}

	@Override
	public double[] generateTerrainMap() {
		return null;
	}

	@Override
	public float getRarity() {
		return 0.5F;
	}

	@Override
	public String getPrimaryName() {
		return "Pulse";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Pulse"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Block.mushroomCapBrown.blockID, 10, 32));
//		list.add(new DecoratorVein(Block.ice.blockID, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 0;
	}

}
