package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectHeat extends TerrainAspect 
{
	IBlockState[] upperBlocks = {Blocks.netherrack.getDefaultState()};
	IBlockState[] surfaceBlocks = {Blocks.soul_sand.getDefaultState(), Blocks.cobblestone.getDefaultState()};
	IBlockState[] structureBlocks = {Blocks.nether_brick.getDefaultState(), Blocks.obsidian.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.0D, 0.0D, 0.0D);

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
	public double[] generateTerrainMap() 
	{
		return null;
	}

	@Override
	public IBlockState getOceanBlock()
	{
		return Blocks.lava.getDefaultState();
	}
	@Override
	public IBlockState getRiverBlock()
	{
		return Blocks.flowing_lava.getDefaultState();
	}

	@Override
	public String getPrimaryName() {
		return "Heat";
	}

	@Override
	public String[] getNames() {
		return new String[] {"heat","flame","fire"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceDecoratorVein(Blocks.soul_sand.getDefaultState(), 10, 32));
		list.add(new SurfaceDecoratorVein(Blocks.glowstone.getDefaultState(), 5, 8));
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		return new ArrayList<ILandDecorator>();
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 0;
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
}
