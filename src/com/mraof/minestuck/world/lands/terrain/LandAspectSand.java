
package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class LandAspectSand extends TerrainLandAspect
{
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectSand()
	{
		this(Variant.SAND);
	}
	
	public LandAspectSand(Variant variation)
	{
		variations = new ArrayList<>();
		type = variation;
		
		if(type == Variant.SAND)
		{
			fogColor = new Vec3d(0.99D, 0.8D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.8D, 0.1D);
			
			variations.add(this);
			variations.add(new LandAspectSand(Variant.SAND_RED));
			variations.add(new LandAspectSand(Variant.LUSH_DESERTS));
		} else
		{
			fogColor = new Vec3d(0.99D, 0.6D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.6D, 0.1D);
		}
		
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SAND || type == Variant.LUSH_DESERTS)
		{
			registry.setBlockState("upper", Blocks.SAND.getDefaultState());
			registry.setBlockState("ground", Blocks.SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.RED_SAND.getDefaultState());
		} else
		{
			registry.setBlockState("upper", Blocks.RED_SAND.getDefaultState());
			registry.setBlockState("ground", Blocks.RED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_RED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.SAND.getDefaultState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.ACACIA_PLANKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.ACACIA_SLAB.getDefaultState());
		registry.setBlockState("river", registry.getBlockState("upper"));
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.getDefaultState());
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
	}

	@Override
	public String[] getNames()
	{
		if(type == Variant.LUSH_DESERTS) {
			return new String[] {"lush_deserts"};
		} else {
			return new String[] {"sand", "dune", "desert"};
		}
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.DESERT;
		settings.downfall = 0.0F;
		settings.temperature = 2.0F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 0.0F;
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		/*list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
		list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		list.add(new OasisDecorator());
		if(type == Variant.LUSH_DESERTS) {
			list.add(new WorldGenDecorator(new WorldGenCactus(), 45, 0.4F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenCactus(), 30, 0.4F, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 55, 15, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 65, 15, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.desertBush, true, 1, 1, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.desertBush, true, 1, 1, BiomeMinestuck.mediumRough));
		} else {
			list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 5, 32, BiomeMinestuck.mediumRough));
		}
		list.add(new OasisDecorator(BiomeMinestuck.mediumNormal));
		
		list.add(new UndergroundDecoratorVein(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, type == Variant.SAND_RED?BlockSand.EnumType.RED_SAND:BlockSand.EnumType.SAND), 8, 28, 256));
		list.add(new UndergroundDecoratorVein((type == Variant.SAND_RED?MinestuckBlocks.ironOreSandstoneRed:MinestuckBlocks.ironOreSandstone).getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein((type == Variant.SAND_RED?MinestuckBlocks.goldOreSandstoneRed:MinestuckBlocks.goldOreSandstone).getDefaultState(), 6, 9, 32));
		*/
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
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("sand");
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.TURTLE;
	}
	
	public enum Variant
	{
		SAND,
		LUSH_DESERTS,
		SAND_RED;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}
