package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class LandAspectForest extends TerrainLandAspect
{
	private final Variant type;
	private final List<TerrainLandAspect> variations;
	
	public LandAspectForest()
	{
		this(Variant.FOREST);
	}
	
	public LandAspectForest(Variant variation)
	{
		variations = new ArrayList<>();
		type = variation;
		
		if(type == Variant.FOREST)
		{
			variations.add(this);
			variations.add(new LandAspectForest(Variant.TAIGA));
		}
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", type == Variant.TAIGA ? Blocks.PODZOL.getDefaultState() : Blocks.GRASS.getDefaultState());
		registry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		if(type == Variant.TAIGA) {
			registry.setBlockState("structure_primary", Blocks.SPRUCE_WOOD.getDefaultState());
			registry.setBlockState("structure_primary_decorative", MinestuckBlocks.FROST_WOOD.getDefaultState());
		} else {
			registry.setBlockState("structure_primary", MinestuckBlocks.VINE_WOOD.getDefaultState());
			registry.setBlockState("structure_primary_decorative", MinestuckBlocks.FLOWERY_VINE_WOOD.getDefaultState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.getDefaultState());
		registry.setBlockState("bush", Blocks.FERN.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.GREEN_WOOL.getDefaultState());
		if(type == Variant.TAIGA) {
			registry.setBlockState("structure_wool_3", Blocks.LIGHT_BLUE_WOOL.getDefaultState());
		} else {
			registry.setBlockState("structure_wool_3", Blocks.BROWN_WOOL.getDefaultState());
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.FOREST) {
			return new String[] {"forest", "tree"};
		} else {
			return new String[] {"taiga", "boreal_forest", "cold_forest"};
		}
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		if(biome.staticBiome == ModBiomes.LAND_NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(Blocks.GRASS.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
			
			switch(this.type)
			{
				case FOREST:
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(new Feature[]{Feature.BIRCH_TREE, Feature.FANCY_TREE}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG}, new float[]{0.2F, 0.1F}, Feature.NORMAL_TREE, IFeatureConfig.NO_FEATURE_CONFIG), Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(5, 0.1F, 1)));
					break;
				case TAIGA:
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(new Feature[]{Feature.PINE_TREE}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG}, new float[]{1/3F}, Feature.SPRUCE_TREE, IFeatureConfig.NO_FEATURE_CONFIG), Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(5, 0.1F, 1)));
					break;
			}
		} else if(biome.staticBiome == ModBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(Blocks.GRASS.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(3)));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(Blocks.FERN.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(1)));
			
			switch(this.type)
			{
				case FOREST:
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(new Feature[]{Feature.BIRCH_TREE, Feature.FANCY_TREE}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG}, new float[]{0.2F, 0.1F}, Feature.NORMAL_TREE, IFeatureConfig.NO_FEATURE_CONFIG), Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(10, 0.1F, 1)));
					break;
				case TAIGA:
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(new Feature[]{Feature.PINE_TREE}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG}, new float[]{0.33333334F}, Feature.SPRUCE_TREE, IFeatureConfig.NO_FEATURE_CONFIG), Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(10, 0.1F, 1)));
					break;
			}
		} else if(biome.staticBiome == ModBiomes.LAND_OCEAN)
		{
			DefaultBiomeFeatures.addSwampClayDisks(biome);
		}
		
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.DIRT.getDefaultState(), 33), Placement.COUNT_RANGE, new CountRangeConfig(10, 0, 0, 256)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.GRAVEL.getDefaultState(), 33), Placement.COUNT_RANGE, new CountRangeConfig(8, 0, 0, 256)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.COAL_ORE.getDefaultState(), 17), Placement.COUNT_RANGE, new CountRangeConfig(20, 0, 0, 128)));
		DefaultBiomeFeatures.addExtraEmeraldOre(biome);
		
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.0D, 1.0D, 0.6D);
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return new Vec3d(0.4D, 0.7D, 1.0D);
	}
	
	@Override
	public float getRainfall()	//Same as vanilla forest
	{
		return 0.8F;
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTerrain("forest");
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.IGUANA;
	}
	
	public enum Variant
	{
		FOREST,
		TAIGA;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}