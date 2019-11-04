
package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class SandLandAspect extends TerrainLandAspect
{
	public static final String SAND = "minestuck.sand";
	public static final String DUNES = "minestuck.dunes";
	public static final String DESERTS = "minestuck.deserts";
	public static final String LUSH_DESERTS = "minestuck.lush_deserts";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sand");
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	
	public SandLandAspect(Variant variation)
	{
		super(GROUP_NAME);
		type = variation;
		
		if(type == Variant.SAND)
		{
			fogColor = new Vec3d(0.99D, 0.8D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.8D, 0.1D);
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
			registry.setGroundState(Blocks.SANDSTONE.getDefaultState(), MSFillerBlockTypes.SANDSTONE);
			registry.setBlockState("upper", Blocks.SAND.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.RED_SAND.getDefaultState());
		} else
		{
			registry.setGroundState(Blocks.RED_SANDSTONE.getDefaultState(), MSFillerBlockTypes.RED_SANDSTONE);
			registry.setBlockState("upper", Blocks.RED_SAND.getDefaultState());
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
	public String[] getNames()
	{
		if(type == Variant.LUSH_DESERTS) {
			return new String[] {LUSH_DESERTS};
		} else {
			return new String[] {SAND, DUNES, DESERTS};
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
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(type == Variant.LUSH_DESERTS)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MSBlocks.DESERT_BUSH.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(1)));
			if(biome.staticBiome == MSBiomes.LAND_NORMAL)
			{
				biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.OASIS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(128)));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MSBlocks.BLOOMING_CACTUS.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(12)));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.CACTUS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(15)));
			}
			if(biome.staticBiome == MSBiomes.LAND_ROUGH)
			{
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MSBlocks.BLOOMING_CACTUS.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(10)));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.CACTUS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(10)));
			}
		} else
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.DEAD_BUSH, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
			if(biome.staticBiome == MSBiomes.LAND_NORMAL)
			{
				biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.OASIS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(128)));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.CACTUS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(5)));
			}
			if(biome.staticBiome == MSBiomes.LAND_ROUGH)
			{
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MSBlocks.BLOOMING_CACTUS.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.CACTUS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
			}
		}
		
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(blocks.getBlockState("upper"), 7, 2, Lists.newArrayList(blocks.getBlockState("ground"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(8)));
		if(type != Variant.RED_SAND)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SANDSTONE_IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SANDSTONE_GOLD_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 32)));
		} else
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_GOLD_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 32)));
		}
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
		SAND,
		LUSH_DESERTS,
		RED_SAND;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}
