package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.lands.decorator.DecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;

public class LandAspectPulse extends TerrainAspect 
{
	IBlockState[] upperBlocks = {Blocks.netherrack.getDefaultState()};
	IBlockState[] surfaceBlocks = {Blocks.obsidian.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.36D, 0.01D, 0.01D);
	
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
		return Blocks.water;//Minestuck.blockBlood;
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
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.nether_brick.getDefaultState(), 10, 32));
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
