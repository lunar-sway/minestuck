package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;
import java.util.Random;

import com.mraof.minestuck.world.gen.lands.decorator.DecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectHeat extends TerrainAspect 
{
	IBlockState[] upperBlocks = {Blocks.netherrack.getDefaultState(), Blocks.obsidian.getDefaultState()};
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
		return Blocks.lava;
	}
	@Override
	public Block getRiverBlock() {
		return Blocks.flowing_lava;
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
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.soul_sand.getDefaultState(), 10, 32));
		list.add(new DecoratorVein(Blocks.glowstone.getDefaultState(), 5, 8));
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
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
}
