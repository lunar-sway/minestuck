package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectHeat extends LandAspect 
{
	IBlockState[] upperBlocks = {Blocks.netherrack.getDefaultState(), Blocks.obsidian.getDefaultState()};
	IBlockState[] surfaceBlocks = {Blocks.soul_sand.getDefaultState(), Blocks.cobblestone.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.0D, 0.0D, 0.0D);

	@Override
	public IBlockState[] getSurfaceBlocks() 
	{
		return upperBlocks;
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
	public int getDayCycleMode()
	{
		return 0;
	}

	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
}
