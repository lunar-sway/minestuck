package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.world.gen.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;

public class LandAspectShade extends TerrainAspect 
{

	IBlockState[] surfaceBlocks = {Minestuck.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, BlockColoredDirt.BlockType.BLUE)};
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
		return Blocks.water;//Minestuck.blockOil;
	}

	@Override
	public double[] generateTerrainMap() {
		return null;
	}
	
	@Override
	public String getPrimaryName()
	{
		return "Shade";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Shade"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceDecoratorVein(Blocks.brown_mushroom_block.getDefaultState(), 5, 32));
		list.add(new SurfaceDecoratorVein(Blocks.red_mushroom_block.getDefaultState(), 5, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		return new ArrayList<ILandDecorator>();
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
	
	@Override
	public int getWeatherType()
	{
		return 0;
	}
	
}
