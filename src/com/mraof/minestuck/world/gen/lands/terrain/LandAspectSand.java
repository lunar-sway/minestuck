
package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.gen.lands.decorator.LayeredBlockDecorator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectSand extends TerrainAspect
{
	IBlockState[] upperBlocks = {Blocks.sandstone.getDefaultState()};
	IBlockState[] structureBlocks = {Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
	IBlockState[] surfaceBlocks = {Blocks.sand.getDefaultState()};
	static Vec3 skyColor = new Vec3(0.99D, 0.8D, 0.05D);
	
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
		return Blocks.sand;
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
		return "Sand";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Sand", "Dunes", "Deserts"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
//		list.add(new DecoratorVein(Blocks.stonebrick.getDefaultState(), 10, 32));
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
//		list.add(new LayeredBlockDecorator(Minestuck.layeredSand, false));
		return list;
	}
	
	@Override
	public int getDayCycleMode() {
		return 0;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public Vec3 getFogColor() 
	{
		return skyColor;
	}
}
