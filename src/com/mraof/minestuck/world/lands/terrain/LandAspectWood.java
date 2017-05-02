package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.FireFieldDecorator;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectWood extends TerrainLandAspect 
{
	static Vec3d skyColor = new Vec3d(0.0D, 0.16D, 0.38D);
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.PLANKS.getDefaultState());
		registry.setBlockState("upper", Blocks.LOG.getDefaultState());
		registry.setBlockState("ocean", Blocks.WATER.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.LOG.getDefaultState());
		registry.setBlockState("structure_primary_decorative", MinestuckBlocks.glowingLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		registry.setBlockState("structure_primary_stairs", Blocks.OAK_STAIRS.getDefaultState());
		registry.setBlockState("fall_fluid", Blocks.WATER.getDefaultState());
	}
	
	@Override
	public String getPrimaryName()
	{
		return "wood";
	}

	@Override
	public String[] getNames() {
		return new String[] {"wood","oak","lumber"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceDecoratorVein(Blocks.LEAVES.getDefaultState(), 15, 32, BiomeMinestuck.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.OAK_FENCE.getDefaultState(), 8, 32, BiomeMinestuck.mediumNormal));
		list.add(new SurfaceDecoratorVein(Blocks.NETHERRACK.getDefaultState(), 5, 8, BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 26, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 4, 7, 32));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 8, 3, 24));
		return list;
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 2;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public float getTemperature()
	{
		return 1.0F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.5F;
	}
	
}
