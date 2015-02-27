package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.gen.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.GrassDecorator;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.gen.lands.decorator.IceDecorator;
import com.mraof.minestuck.world.gen.lands.decorator.LayeredBlockDecorator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectFrost extends TerrainAspect 
{
	IBlockState[] surfaceBlocks = {Blocks.dirt.getDefaultState()};
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
	public double[] generateTerrainMap() 
	{
		return null;
	}
	@Override
	public IBlockState getRiverBlock() 
	{
		return Blocks.ice.getDefaultState();
	}

	@Override
	public String getPrimaryName() 
	{
		return "Frost";
	}

	@Override
	public String[] getNames() {
		return new String[] {"frost", "ice", "snow"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceDecoratorVein(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT), 10, 32));
		list.add(new SurfaceDecoratorVein(Blocks.ice.getDefaultState(), 5, 8));
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new IceDecorator());
		list.add(new LayeredBlockDecorator(Blocks.snow_layer, true));
		list.add(new GrassDecorator());
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
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.stonebrick)
			return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		return state;
	}
	
}
