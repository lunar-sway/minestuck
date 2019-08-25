package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.*;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class SandstoneLandAspect extends TerrainLandAspect
{
	
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	
	public static TerrainLandAspect[] createTypes()
	{
		SandstoneLandAspect parent = new SandstoneLandAspect(Variant.SANDSTONE, null);
		return new TerrainLandAspect[]{parent.setRegistryName("sandstone"),
				new SandstoneLandAspect(Variant.RED_SANDSTONE, parent).setRegistryName("red_sandstone")};
	}
	
	private SandstoneLandAspect(Variant type, SandstoneLandAspect parent)
	{
		super(parent);
		this.type = type;
		if(type == Variant.SANDSTONE)
		{
			fogColor = new Vec3d(0.9D, 0.7D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.6D, 0.2D);
		} else
		{
			fogColor = new Vec3d(0.7D, 0.4D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.5D, 0.1D);
			
		}
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SANDSTONE)
		{
			registry.setBlockState("upper", Blocks.SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.RED_SAND.getDefaultState());
		} else
		{
			registry.setBlockState("upper", Blocks.RED_SANDSTONE.getDefaultState());
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
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.WHITE_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"sandstone", "desertStone"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.MESA;
		settings.downfall = 0.0F;
		settings.temperature = 1.8F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 1/10F;
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		List<ILandDecorator> list = new ArrayList<ILandDecorator>();
		BlockState sand = Blocks.SAND.getDefaultState();
		BlockState sandstone = Blocks.SANDSTONE.getDefaultState();
		if(type == Variant.RED_SANDSTONE)
		{
			sand = Blocks.RED_SAND.getDefaultState();
			sandstone = Blocks.RED_SANDSTONE.getDefaultState();
		}
		list.add(new SurfaceDecoratorVein(sand, 10, 32));
		list.add(new BlockBlobDecorator(sandstone, 0, 3, ModBiomes.mediumNormal));
		list.add(new BlockBlobDecorator(sandstone, 0, 5, ModBiomes.mediumRough));
		//list.add(new WorldGenDecorator(new WorldGenDeadBush(), 15, 0.4F));
		
		list.add(new UndergroundDecoratorVein(sandstone, 8, 28, 256));
		list.add(new UndergroundDecoratorVein(Blocks.IRON_ORE.getDefaultState(), 24, 9, 64));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 12, 8, 32));
		return list;
	}
	
	@Override
	public float getSkylightBase()
	{
		return 3/4F;
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
		return ModEntityTypes.TURTLE;
	}
	
	public enum Variant
	{
		SANDSTONE,
		RED_SANDSTONE;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}