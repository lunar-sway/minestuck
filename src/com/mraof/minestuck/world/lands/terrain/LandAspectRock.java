package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;

import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;

public class LandAspectRock extends TerrainAspect
{
	
	private IBlockState[] surfaceBlocks = {Blocks.gravel.getDefaultState(), Blocks.cobblestone.getDefaultState()};
	private IBlockState[] structureBlocks = {Blocks.cobblestone.getDefaultState(), Blocks.stonebrick.getDefaultState()};
	
	@Override
	public IBlockState[] getSurfaceBlocks()
	{
		return surfaceBlocks;
	}
	
	@Override
	public IBlockState[] getUpperBlocks()
	{
		return new IBlockState[] {Blocks.stone.getDefaultState()};
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public String getPrimaryName()
	{
		return "Rock";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rock", "stone", "ore"};
	}
	
	@Override
	public List<ILandDecorator> getOptionalDecorators()
	{
		List<ILandDecorator> decorators = new ArrayList<ILandDecorator>();
		decorators.add(new UndergroundDecoratorVein(Blocks.coal_ore.getDefaultState(), 35, 8, 70));
		decorators.add(new UndergroundDecoratorVein(Blocks.iron_ore.getDefaultState(), 24, 6, 64));
		decorators.add(new UndergroundDecoratorVein(Blocks.gold_ore.getDefaultState(), 18, 5, 30));
		decorators.add(new UndergroundDecoratorVein(Blocks.redstone_ore.getDefaultState(), 13, 4, 40));
		decorators.add(new UndergroundDecoratorVein(Blocks.lapis_ore.getDefaultState(), 10, 5, 35));
		decorators.add(new UndergroundDecoratorVein(Blocks.diamond_ore.getDefaultState(), 9, 4, 25));
		return decorators;
	}
	
	@Override
	public List<ILandDecorator> getRequiredDecorators()
	{
		return new ArrayList<ILandDecorator>();
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 2;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return new Vec3(0.5, 0.5, 0.55);
	}
	
	@Override
	public IBlockState getDecorativeBlockFor(IBlockState state)
	{
		if(state.getBlock() == Blocks.stonebrick)
			return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		return state;
	}
	
}