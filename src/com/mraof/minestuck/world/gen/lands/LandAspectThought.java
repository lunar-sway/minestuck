package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectThought extends PrimaryAspect 
{
	IBlockState[] surfaceBlocks = {Minestuck.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, BlockColoredDirt.BlockType.THOUGHT)};
	private IBlockState[] upperBlocks = {Blocks.stone.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.66, 0.39, 0.2);

	@Override
	public IBlockState[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public IBlockState[] getUpperBlocks() 
	{
		return upperBlocks ;
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
	public Block getOceanBlock() 
	{
		return Blocks.water;//Minestuck.blockBrainJuice;
	}

	@Override
	public String getPrimaryName() 
	{
		return "Thought";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Thought"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
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
