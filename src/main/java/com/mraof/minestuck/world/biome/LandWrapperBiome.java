package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import static com.mraof.minestuck.MinestuckConfig.*;

public class LandWrapperBiome extends LandBiome
{
	public final LandBiome staticBiome;
	
	public LandWrapperBiome(LandBiome biome, Category category, RainType rainType, float temperature, float downfall, float depth, float scale)
	{
		super(new Biome.Builder().category(category).precipitation(rainType).temperature(temperature).downfall(downfall).depth(depth).scale(scale).waterColor(0x3F76E4).waterFogColor(0x050533));
		this.staticBiome = biome;
	}
	
	public void init(StructureBlockRegistry blocks, EntityType<? extends ConsortEntity> consortType)
	{
		if(generateCruxiteOre.get())
		{
			addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize), Placement.COUNT_RANGE, new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax)));
		}
		if(generateUraniumOre.get())
		{
			addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize), Placement.COUNT_RANGE, new CountRangeConfig(uraniumVeinsPerChunk, uraniumStratumMin, uraniumStratumMin, uraniumStratumMax)));
		}
		setSurfaceBuilder(SurfaceBuilder.DEFAULT, blocks.getSurfaceBuilderConfig());
		this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(consortType, 2, 1, 3));
		
		if(staticBiome != MSBiomes.LAND_OCEAN)
			addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature(MSFeatures.RETURN_NODE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_HEIGHTMAP, new ChanceConfig(128)));
		addDefaultStructures(blocks);
	}
	
	public <SC extends ISurfaceBuilderConfig> void setSurfaceBuilder(SurfaceBuilder<SC> builder, SC config)
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(builder, config);
	}
	
	private void addDefaultStructures(StructureBlockRegistry blocks)
	{
		addStructure(MSFeatures.LAND_GATE, IFeatureConfig.NO_FEATURE_CONFIG);
		if(staticBiome == MSBiomes.LAND_NORMAL)
		{
			addStructure(MSFeatures.SMALL_RUIN, IFeatureConfig.NO_FEATURE_CONFIG);
			addStructure(MSFeatures.CONSORT_VILLAGE, IFeatureConfig.NO_FEATURE_CONFIG);
		}
		if(staticBiome == MSBiomes.LAND_NORMAL || staticBiome == MSBiomes.LAND_ROUGH)
		{
			addStructure(MSFeatures.IMP_DUNGEON, IFeatureConfig.NO_FEATURE_CONFIG);
		}
		
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, createDecoratedFeature(MSFeatures.LAND_GATE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, createDecoratedFeature(MSFeatures.SMALL_RUIN, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, createDecoratedFeature(MSFeatures.IMP_DUNGEON, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, createDecoratedFeature(MSFeatures.CONSORT_VILLAGE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
	}
	
	@Override
	public void addSpawn(EntityClassification classification, SpawnListEntry entry)
	{
		super.addSpawn(classification, entry);
	}
}