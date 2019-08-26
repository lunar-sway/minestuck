package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.LeaflessTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceMushroomGenerator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class ShadeLandAspect extends TerrainLandAspect
{
	
	private static final Vec3d skyColor = new Vec3d(0.16D, 0.38D, 0.54D);
	
	public ShadeLandAspect()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", MinestuckBlocks.BLUE_DIRT.getDefaultState());
		//registry.setBlockState("ocean", MinestuckBlocks.blockOil.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.SHADE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.SMOOTH_SHADE_STONE.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.SHADE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRAVEL.getDefaultState());
		//registry.setBlockState("light_block", MinestuckBlocks.glowingLog.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		registry.setBlockState("mushroom_1", MinestuckBlocks.GLOWING_MUSHROOM.getDefaultState());
		registry.setBlockState("mushroom_2", MinestuckBlocks.GLOWING_MUSHROOM.getDefaultState());
		registry.setBlockState("bush", MinestuckBlocks.GLOWING_MUSHROOM.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.CYAN_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"shade"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.MUSHROOM;
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new SurfaceMushroomGenerator(10, 64, ModBiomes.mediumNormal));
		list.add(new SurfaceMushroomGenerator(5, 32, ModBiomes.mediumRough));
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.GLOWING_LOG.getDefaultState(), 0.5F, ModBiomes.mediumNormal));
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.GLOWING_LOG.getDefaultState(), 2, ModBiomes.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 6, 7, 35));
		
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 0F;
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
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.SALAMANDER;
	}
}
