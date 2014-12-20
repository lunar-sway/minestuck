package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;
import java.util.Random;

import com.mraof.minestuck.world.gen.lands.decorator.DecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.gen.lands.decorator.IceAndSnowDecorator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectFrost extends TerrainAspect 
{
	IBlockState[] surfaceBlocks = {Blocks.grass.getDefaultState()};
	private IBlockState[] upperBlocks = {Blocks.stone.getDefaultState()};
	IBlockState[] structureBlocks = {Blocks.stone.getDefaultState(), Blocks.stonebrick.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.45D, 0.5D, 0.98D);

	@Override
	public IBlockState[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public IBlockState[] getUpperBlocks() 
	{
		return upperBlocks;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
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
	public Block getRiverBlock() 
	{
		return Blocks.ice;
	}

	@Override
	public String getPrimaryName() 
	{
		return "Frost";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Frost", "Ice", "Snow"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.dirt.getDefaultState(), 10, 32));
		list.add(new DecoratorVein(Blocks.ice.getDefaultState(), 5, 8));
		list.add(new IceAndSnowDecorator());
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
	
	@Override
	public int getWeatherType()
	{
		return 1;
	}
	
}
