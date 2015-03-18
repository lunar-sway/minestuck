package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import com.mraof.minestuck.world.lands.decorator.GrassDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.TreeDecorator;

public class LandAspectForest extends TerrainAspect
{
	
	
	@Override
	public IBlockState[] getSurfaceBlocks()
	{
		return new IBlockState[] {Blocks.dirt.getDefaultState()};
	}
	
	@Override
	public IBlockState[] getUpperBlocks()
	{
		return new IBlockState[] {Blocks.stone.getDefaultState()};
	}
	
	@Override
	public double[] generateTerrainMap()
	{
		return null;
	}
	
	@Override
	public String getPrimaryName()
	{
		return "Forest";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"forest", "tree"};
	}
	
	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new GrassDecorator());
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new TreeDecorator());
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
		return new Vec3(0.0D, 1.0D, 0.6D);
	}
	
}