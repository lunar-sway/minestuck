package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;

public class LandAspectShade extends LandAspect 
{

	IBlockState[] surfaceBlocks = {Minestuck.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, 0)};
	IBlockState[] upperBlocks = {Blocks.stone.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.16D, 0.38D, 0.54D);
	
	@Override
	public IBlockState[] getSurfaceBlocks() {
		return surfaceBlocks;
	}
	
	@Override
	public IBlockState[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Minestuck.blockOil;
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
		return "Shade";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Shade"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.brown_mushroom_block.getDefaultState(), 5, 32));
		list.add(new DecoratorVein(Blocks.red_mushroom_block.getDefaultState(), 5, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 2;
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}


}
