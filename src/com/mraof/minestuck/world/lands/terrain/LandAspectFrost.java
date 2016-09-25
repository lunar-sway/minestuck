package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.IceDecorator;
import com.mraof.minestuck.world.lands.decorator.LayeredBlockDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectFrost extends TerrainLandAspect 
{
	static Vec3d skyColor = new Vec3d(0.45D, 0.5D, 0.98D);
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS.getDefaultState());
		registry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.PRISMARINE.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS));
		registry.setBlockState("structure_secondary", MinestuckBlocks.stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_BRICK));
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.frostBrickStairs.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_CHISELED));
		registry.setBlockState("river", Blocks.ICE.getDefaultState());
		registry.setBlockState("light_block", Blocks.SEA_LANTERN.getDefaultState());
		registry.setBlockState("bucket1", Blocks.SNOW.getDefaultState());
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
		list.add(new LayeredBlockDecorator(Blocks.SNOW_LAYER, true));
//		list.add(new SpruceTreeDecorator());
		
		list.add(new SurfaceDecoratorVein(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT), 10, 32, BiomeMinestuck.mediumRough, BiomeMinestuck.mediumOcean));
		list.add(new SurfaceDecoratorVein(Blocks.ICE.getDefaultState(), 5, 8, BiomeMinestuck.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SNOW.getDefaultState(), 8, 16, BiomeMinestuck.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SNOW.getDefaultState(), 15, 16, BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.PACKED_ICE.getDefaultState(), 2, 8, 64));
		list.add(new UndergroundDecoratorVein(Blocks.SNOW.getDefaultState(), 3, 16, 64));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 28, 64));
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(Blocks.DIAMOND_ORE.getDefaultState(), 3, 6, 24));
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
		return 1/4F;
	}
	
}