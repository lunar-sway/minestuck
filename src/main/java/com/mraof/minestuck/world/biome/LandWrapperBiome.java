package com.mraof.minestuck.world.biome;

/*
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
		if(MinestuckConfig.SERVER.generateCruxiteOre.get())
		{
			addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax))));
		}
		if(MinestuckConfig.SERVER.generateUraniumOre.get())
		{
			addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(uraniumVeinsPerChunk, uraniumStratumMin, uraniumStratumMin, uraniumStratumMax))));
		}
		setSurfaceBuilder(SurfaceBuilder.DEFAULT, blocks.getSurfaceBuilderConfig());
		this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(consortType, 2, 1, 3));
		
		if(staticBiome != MSBiomes.LAND_OCEAN)
			addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.RETURN_NODE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(128))));
		addDefaultStructures(blocks);
	}
	
	public <SC extends ISurfaceBuilderConfig> void setSurfaceBuilder(SurfaceBuilder<SC> builder, SC config)
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(builder, config);
	}
	
	private void addDefaultStructures(StructureBlockRegistry blocks)
	{
		addStructure(MSFeatures.LAND_GATE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		if(staticBiome == MSBiomes.LAND_NORMAL)
		{
			addStructure(MSFeatures.SMALL_RUIN.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
			addStructure(MSFeatures.CONSORT_VILLAGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		}
		if(staticBiome == MSBiomes.LAND_NORMAL || staticBiome == MSBiomes.LAND_ROUGH)
		{
			addStructure(MSFeatures.IMP_DUNGEON.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		}

		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.LAND_GATE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.SMALL_RUIN.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.IMP_DUNGEON.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.CONSORT_VILLAGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
	}
	
	@Override
	public void addSpawn(EntityClassification classification, SpawnListEntry entry)
	{
		super.addSpawn(classification, entry);
	}
}*/