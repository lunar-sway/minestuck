package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceMushroomGenerator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;

public class LandAspectShade extends TerrainLandAspect 
{
	
	static Vec3d skyColor = new Vec3d(0.16D, 0.38D, 0.54D);
	
	public LandAspectShade()
	{
	}
	
	@Override
	public IBlockState getUpperBlock()
	{
		return MinestuckBlocks.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, BlockColoredDirt.BlockType.BLUE);
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return new IBlockState[] {Blocks.STONE.getDefaultState(), Blocks.STONEBRICK.getDefaultState()};
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.STONEBRICK)
			return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		else return state;
	}
	
	@Override
	public IBlockState getOceanBlock() 
	{
		return MinestuckBlocks.blockOil.getDefaultState();
	}
	
	@Override
	public String getPrimaryName()
	{
		return "shade";
	}

	@Override
	public String[] getNames() {
		return new String[] {"shade"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceMushroomGenerator(10, 64, BiomeMinestuck.mediumNormal));
		list.add(new SurfaceMushroomGenerator(5, 32, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 6, 7, 35));
		
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 2;
	}

	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public int getWeatherType()
	{
		return 0;
	}
	
}
