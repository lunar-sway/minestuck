package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class SandstoneLandAspect extends TerrainLandAspect
{
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sandstone");
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	
	public SandstoneLandAspect(Variant type)
	{
		super(GROUP_NAME);
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
			registry.setBlockState("sand", Blocks.SAND.getDefaultState());
			registry.setBlockState("ocean_surface", Blocks.SAND.getDefaultState());
			registry.setBlockState("upper", Blocks.SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.RED_SAND.getDefaultState());
		} else
		{
			registry.setBlockState("sand", Blocks.RED_SAND.getDefaultState());
			registry.setBlockState("ocean_surface", Blocks.RED_SAND.getDefaultState());
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
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		BlockState sand = blocks.getBlockState("sand");
		BlockState sandstone = blocks.getBlockState("upper");
		
		if(biome.staticBiome != MSBiomes.LAND_OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.DEAD_BUSH, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(15)));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(sand, 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(4)));
		}
		
		
		if(biome.staticBiome == MSBiomes.LAND_NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.BLOCK_BLOB, new BlockBlobConfig(sandstone, 0), Placement.FOREST_ROCK, new FrequencyConfig(3)));
		} else if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.BLOCK_BLOB, new BlockBlobConfig(sandstone, 0), Placement.FOREST_ROCK, new FrequencyConfig(5)));
		}
		
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, sandstone, 28), Placement.COUNT_RANGE, new CountRangeConfig(8, 0, 0, 256)));
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.REDSTONE_ORE.getDefaultState(), 8), Placement.COUNT_RANGE, new CountRangeConfig(12, 0, 0, 32)));
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
		return MSEntityTypes.TURTLE;
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