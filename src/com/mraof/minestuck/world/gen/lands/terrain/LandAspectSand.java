
package com.mraof.minestuck.world.gen.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.lands.ILandAspect;
import com.mraof.minestuck.world.gen.lands.LandAspectRegistry;
import com.mraof.minestuck.world.gen.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.gen.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.gen.lands.decorator.LayeredBlockDecorator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

public class LandAspectSand extends TerrainAspect
{
	private final IBlockState[] surfaceBlocks;
	private final IBlockState[] upperBlocks;
	private final IBlockState[] structureBlocks;
	private final Vec3 skyColor;
	private final String name;
	private final List<ILandAspect> variations;
	
	public LandAspectSand()
	{
		name = "Sand";
		skyColor = new Vec3(0.99D, 0.8D, 0.05D);
		
		surfaceBlocks = new IBlockState[] {Blocks.sand.getDefaultState()};
		upperBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState()};
		structureBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
		
		variations = new ArrayList<ILandAspect>();
		variations.add(this);
		variations.add(new LandAspectSand("SandRed"));
	}
	
	public LandAspectSand(String variation)
	{
		name = variation;
		
		{
			skyColor = new Vec3(0.99D, 0.6D, 0.05D);
			
			surfaceBlocks = new IBlockState[] {Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)};
			upperBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState()};
			structureBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
		}
		
		variations = null;
	}
	
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
	public IBlockState getOceanBlock() 
	{
		return surfaceBlocks[0];
	}
	
	@Override
	public double[] generateTerrainMap() {
		return null;
	}
	
	@Override
	public String getPrimaryName()
	{
		return name;
	}

	@Override
	public String[] getNames()
	{
		return new String[] {"Sand", "Dunes", "Deserts"};
	}

	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		if(name.equals("SandRed"))
			list.add(new SurfaceDecoratorVein(Blocks.sand.getDefaultState(), 10, 32));
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
	public int getDayCycleMode()
	{
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
	
	@Override
	public List<ILandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public ILandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromName("Sand");
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.stonebrick)
			return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		else if(state.getBlock() == Blocks.sandstone)
			return Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED);
		else return Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED);
	}
	
}
