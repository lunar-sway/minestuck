package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.TallGrassDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.BasicTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;

public class LandAspectForest extends TerrainLandAspect
{
	
	@Override
	public IBlockState getSurfaceBlock()
	{
		return Blocks.GRASS.getDefaultState();
	}
	
	@Override
	public IBlockState getUpperBlock()
	{
		return Blocks.DIRT.getDefaultState();
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
	public String getPrimaryName()
	{
		return "forest";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"forest", "tree"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new BasicTreeDecorator(5, BiomeMinestuck.mediumNormal));
		list.add(new BasicTreeDecorator(8, BiomeMinestuck.mediumRough));
		list.add(new TallGrassDecorator(0.3F, BiomeMinestuck.mediumNormal));
		list.add(new TallGrassDecorator(0.5F, 0.2F, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 33, 64));	//Have 64 be the highest value because stone is used as a building material for structures right now
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 2, 28, 64));
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 8, 3, 32));
		return list;
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 0;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.0D, 1.0D, 0.6D);
	}
	
	@Override
	public float getRainfall()	//Same as vanilla forest
	{
		return 0.8F;
	}
	
}