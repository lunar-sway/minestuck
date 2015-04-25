package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;

public class LandAspectSandstone extends TerrainAspect
{
	
	private final IBlockState[] surfaceBlocks;
	private final IBlockState[] upperBlocks;
	private final IBlockState[] structureBlocks;
	private final Vec3 skyColor;
	private final String name;
	private final List<ILandAspect> variations;
	
	public LandAspectSandstone()
	{
		this("Sandstone");
	}
	
	public LandAspectSandstone(String name)
	{
		variations = new ArrayList<ILandAspect>();
		this.name = name;
		if(name.equals("Sandstone"))
		{
			surfaceBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState()};
			upperBlocks = new IBlockState[] {Blocks.stone.getDefaultState()};
			structureBlocks = new IBlockState[] {Blocks.sandstone.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			skyColor = new Vec3(0.9D, 0.7D, 0.05D);
			variations.add(this);
			variations.add(new LandAspectSandstone("SandstoneRed"));
		}
		else
		{
			surfaceBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState()};
			upperBlocks = new IBlockState[] {Blocks.stone.getDefaultState()};
			structureBlocks = new IBlockState[] {Blocks.red_sandstone.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), Blocks.stonebrick.getDefaultState()};
			skyColor = new Vec3(0.9D, 0.5D, 0.05D);
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return name;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"sandstone", "desertStone"};
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
	public double[] generateTerrainMap()
	{
		return null;
	}
	
	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		return list;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		IBlockState sand = Blocks.sand.getDefaultState();
		if(name.equals("SandstoneRed"))
				sand = sand.withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
		list.add(new SurfaceDecoratorVein(sand, 10, 32));
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
	
	@Override
	public ILandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromName("Sandstone");
	}
	
	@Override
	public List<ILandAspect> getVariations()
	{
		return variations;
	}
	
}
