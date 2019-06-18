package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.*;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LandAspectHeat extends TerrainLandAspect 
{
	static Vec3d skyColor = new Vec3d(0.4D, 0.0D, 0.0D);
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", Blocks.COBBLESTONE.getDefaultState());
		registry.setBlockState("ground", Blocks.NETHERRACK.getDefaultState());
		registry.setBlockState("ocean", Blocks.LAVA.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.NETHER_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.NETHER_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.CAST_IRON.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.CHISELED_CAST_IRON.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.CAST_IRON_STAIRS.getDefaultState());
		registry.setBlockState("fall_fluid", Blocks.WATER.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.getDefaultState());
		registry.setBlockState("village_path", Blocks.QUARTZ_BLOCK.getDefaultState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.getDefaultState());
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
		ArrayList<ILandDecorator> list = new ArrayList<>();
		list.add(new FireFieldDecorator(7, BiomeMinestuck.mediumNormal));
		list.add(new FireFieldDecorator(10, BiomeMinestuck.mediumRough));
		list.add(new OceanRundown(0.5F, 3));
		list.add(new SurfaceDecoratorVein(Blocks.SOUL_SAND.getDefaultState(), 15, 32, BiomeMinestuck.mediumRough));
		list.add(new SurfaceDecoratorVein(Blocks.SOUL_SAND.getDefaultState(), 8, 32, BiomeMinestuck.mediumNormal));
		list.add(new SurfaceDecoratorVein(Blocks.GLOWSTONE.getDefaultState(), 5, 8, BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.COAL_ORE_NETHERRACK.getDefaultState(), 26, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.NETHER_QUARTZ_ORE.getDefaultState(), 13, 8, 64));
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 1/2F;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
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
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.NAKAGATOR;
	}
}
