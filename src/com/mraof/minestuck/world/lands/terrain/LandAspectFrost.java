package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.IceDecorator;
import com.mraof.minestuck.world.lands.decorator.LayeredBlockDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectFrost extends TerrainLandAspect 
{
	IBlockState[] structureBlocks = {Blocks.stone.getDefaultState(), Blocks.stonebrick.getDefaultState()};
	static Vec3d skyColor = new Vec3d(0.45D, 0.5D, 0.98D);
	
	@Override
	public IBlockState getSurfaceBlock() 
	{
		return Blocks.grass.getDefaultState();
	}

	@Override
	public IBlockState getUpperBlock() 
	{
		return Blocks.dirt.getDefaultState();
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public IBlockState getRiverBlock() 
	{
		return Blocks.ice.getDefaultState();
	}

	@Override
	public String getPrimaryName() 
	{
		return "frost";
	}

	@Override
	public String[] getNames() {
		return new String[] {"frost", "ice", "snow"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new IceDecorator());
		list.add(new LayeredBlockDecorator(Blocks.snow_layer, true));
//		list.add(new SpruceTreeDecorator());
		
		list.add(new SurfaceDecoratorVein(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT), 10, 32));
		list.add(new SurfaceDecoratorVein(Blocks.ice.getDefaultState(), 5, 8));
		list.add(new SurfaceDecoratorVein(Blocks.snow.getDefaultState(), 10, 16));
		
		list.add(new UndergroundDecoratorVein(Blocks.packed_ice.getDefaultState(), 2, 8, 64));	//Have 64 be the highest value because stone is used as a building material for structures right now
		list.add(new UndergroundDecoratorVein(Blocks.snow.getDefaultState(), 3, 16, 64));
		list.add(new UndergroundDecoratorVein(Blocks.dirt.getDefaultState(), 3, 28, 64));
		list.add(new UndergroundDecoratorVein(Blocks.coal_ore.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(Blocks.diamond_ore.getDefaultState(), 3, 6, 24));
		return list;
	}
	
	@Override
	public int getDayCycleMode() {
		return 0;
	}

	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public int getWeatherType()
	{
		return 1;
	}
	
	@Override
	public float getTemperature()
	{
		return 0.0F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 1/3F;
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.stonebrick)
			return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		return state;
	}
	
}
