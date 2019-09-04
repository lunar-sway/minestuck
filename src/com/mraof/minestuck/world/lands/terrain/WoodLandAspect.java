package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.decorator.SurfaceMushroomGenerator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class WoodLandAspect extends TerrainLandAspect
{
	private static final Vec3d fogColor = new Vec3d(0.0D, 0.16D, 0.38D);
	
	public WoodLandAspect()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("ground", Blocks.STONE.getDefaultState());
		registry.setBlockState("upper", Blocks.OAK_LOG.getDefaultState());
		registry.setBlockState("surface", MinestuckBlocks.TREATED_PLANKS.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.JUNGLE_WOOD.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.DARK_OAK_LOG.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", Blocks.JUNGLE_PLANKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.DARK_OAK_PLANKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.getDefaultState());
		registry.setBlockState("light_block", MinestuckBlocks.GLOWING_WOOD.getDefaultState());
		registry.setBlockState("bush", Blocks.RED_MUSHROOM.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.PURPLE_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GREEN_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"wood","oak","lumber"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.downfall = 0.6F;
		settings.temperature = 1.0F;
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<>();
		list.add(new SurfaceDecoratorVein(Blocks.OAK_LEAVES.getDefaultState(), 15, 32, ModBiomes.mediumRough));
		//list.add(new SurfaceDecoratorVein(MinestuckBlocks.log.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE), 8, 32, BiomeMinestuck.mediumNormal));
		list.add(new SurfaceDecoratorVein(Blocks.NETHERRACK.getDefaultState(), 6, 8, ModBiomes.mediumNormal));
		
		list.add(new SurfaceMushroomGenerator(Blocks.BROWN_MUSHROOM, true, 10, 64, ModBiomes.mediumNormal));
		list.add(new SurfaceMushroomGenerator(Blocks.BROWN_MUSHROOM, true, 5, 32, ModBiomes.mediumRough));
		list.add(new SurfaceMushroomGenerator(Blocks.RED_MUSHROOM, true, 10, 64, ModBiomes.mediumNormal));
		list.add(new SurfaceMushroomGenerator(Blocks.RED_MUSHROOM, true, 5, 32, ModBiomes.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 18, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 8, 7, 32));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 12, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 8, 3, 24));
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
		return fogColor;
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return new Vec3d(0.0D, 0.3D, 0.4D);
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.SALAMANDER;
	}
}
