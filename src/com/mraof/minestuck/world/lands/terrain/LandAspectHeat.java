package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.FireFieldDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectHeat extends TerrainLandAspect 
{
	IBlockState[] structureBlocks = {Blocks.NETHER_BRICK.getDefaultState(), Blocks.OBSIDIAN.getDefaultState()};
	static Vec3d skyColor = new Vec3d(0.4D, 0.0D, 0.0D);
	
	@Override
	public IBlockState getUpperBlock() 
	{
		return Blocks.COBBLESTONE.getDefaultState();
	}
	
	@Override
	public IBlockState getGroundBlock()
	{
		return Blocks.NETHERRACK.getDefaultState();
	}
	
	@Override
	public IBlockState getOceanBlock()
	{
		return Blocks.LAVA.getDefaultState();
	}
	@Override
	public IBlockState getRiverBlock()
	{
		return Blocks.FLOWING_LAVA.getDefaultState();
	}

	@Override
	public String getPrimaryName()
	{
		return "heat";
	}

	@Override
	public String[] getNames() {
		return new String[] {"heat","flame","fire"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new FireFieldDecorator(7, BiomeMinestuck.mediumNormal));
		list.add(new FireFieldDecorator(10, BiomeMinestuck.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SOUL_SAND.getDefaultState(), 15, 32, BiomeMinestuck.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SOUL_SAND.getDefaultState(), 8, 32, BiomeMinestuck.mediumNormal));
		list.add(new SurfaceDecoratorVein(Blocks.GLOWSTONE.getDefaultState(), 5, 8, BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.coalOreNetherrack.getDefaultState(), 26, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.QUARTZ_ORE.getDefaultState(), 13, 8, 64));
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
		return skyColor;
	}
	
	@Override
	public IBlockState[] getStructureBlocks()
	{
		return structureBlocks;
	}
	
	@Override
	public float getTemperature()
	{
		return 2.0F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.0F;
	}
	
}
