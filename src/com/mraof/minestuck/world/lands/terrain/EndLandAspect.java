package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class EndLandAspect extends TerrainLandAspect
{
	private static final Vec3d fogColor = new Vec3d(0.0D, 0.4D, 0.2D);
	private static final Vec3d skyColor = new Vec3d(0.3D, 0.1D, 0.5D);
	
	public EndLandAspect()
	{
		super(null);
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", MinestuckBlocks.END_GRASS.getDefaultState());
		registry.setBlockState("upper", MinestuckBlocks.COARSE_END_STONE.getDefaultState());
		registry.setBlockState("ground", Blocks.END_STONE.getDefaultState());
		//registry.setBlockState("ocean", MinestuckBlocks.blockEnder.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.END_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.PURPUR_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlockState("structure_primary_stairs", Blocks.PURPUR_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", Blocks.PURPUR_BLOCK.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		//registry.setBlockState("fall_fluid", MinestuckBlocks.blockEnder.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.getDefaultState());
		registry.setBlockState("village_path", MinestuckBlocks.COARSE_END_STONE.getDefaultState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.GREEN_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"end", "dimension"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.THEEND;
		settings.downfall = 0.0F;
		settings.temperature = 1.2F;
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		/*list.add(new WorldgenTreeDecorator(2, new EndTree(), BiomeMinestuck.mediumRough));
		list.add(new WorldgenTreeDecorator(3, new EndTree(), BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 8, 33, 256));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT), 8, 16, 128));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.ironOreEndStone.getDefaultState(), 20, 9, 64));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.redstoneOreEndStone.getDefaultState(), 10, 8, 32));*/
		return list;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return fogColor;
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return skyColor;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.NAKAGATOR;
	}
}