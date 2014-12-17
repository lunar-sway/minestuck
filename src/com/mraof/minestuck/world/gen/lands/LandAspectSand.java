
package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectSand extends PrimaryAspect
{
	IBlockState[] upperBlocks = {Blocks.sandstone.getDefaultState()};
	IBlockState[] surfaceBlocks = {Blocks.sand.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.99D, 0.8D, 0.05D);
	
	@Override
	public IBlockState[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public IBlockState[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Blocks.sand;
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
		return "Sand";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Sand"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.stonebrick.getDefaultState(), 10, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 0;
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
}
