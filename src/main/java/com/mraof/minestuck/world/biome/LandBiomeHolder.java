package com.mraof.minestuck.world.biome;


import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import java.util.List;

import static com.mraof.minestuck.world.gen.OreGeneration.*;

public class LandBiomeHolder implements ILandBiomeSet
{
	private final Biome normalBiome, oceanBiome, roughBiome;
	public final LandBiomeSetWrapper baseBiomes;
	
	public LandBiomeHolder(LandBiomeSetWrapper biomes, LandGenSettings settings, LandProperties properties)
	{
		StructureBlockRegistry blocks = settings.getBlockRegistry();
		
		baseBiomes = biomes;
		
		normalBiome = createNormal(biomes, blocks, properties, settings.getLandTypes());
		roughBiome = createRough(biomes, blocks, properties, settings.getLandTypes());
		oceanBiome = createOcean(biomes, blocks, properties, settings.getLandTypes());
	}
	
	public Biome getBiomeFromBase(Biome biome)
	{
		return fromType(baseBiomes.getTypeFromBiome(biome));
	}
	
	@Override
	public List<Biome> getAll()
	{
		return ImmutableList.of(normalBiome, roughBiome, oceanBiome);
	}
	
	@Override
	public Biome fromType(LandBiomeType type)
	{
		switch(type)
		{
			case NORMAL: default: return normalBiome;
			case ROUGH: return roughBiome;
			case OCEAN: return oceanBiome;
		}
	}
	
	private static Biome createNormal(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandProperties properties, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.NORMAL).biomeCategory(properties.category)
				.depth(properties.normalBiomeDepth).scale(properties.normalBiomeScale).build();
	}
	
	private static Biome createRough(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandProperties properties, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.ROUGH).biomeCategory(properties.category)
				.depth(properties.roughBiomeDepth).scale(properties.roughBiomeScale).build();
	}
	
	private static Biome createOcean(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandProperties properties, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.OCEAN).biomeCategory(Biome.Category.OCEAN)
				.depth(properties.oceanBiomeDepth).scale(properties.oceanBiomeScale).build();
	}
	
	private static Biome.Builder createBiomeBase(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandTypePair landTypes, LandBiomeType type)
	{
		Biome base = baseBiomes.fromType(type);
		Biome.Builder builder = new Biome.Builder().precipitation(base.getPrecipitation())
				.temperature(base.getBaseTemperature()).downfall(base.getDownfall()).specialEffects(base.getSpecialEffects());
		
		MobSpawnInfo spawnInfo = createMobSpawnInfo(landTypes, type);
		
		BiomeGenerationSettings generation = createGenerationSettings(base, blocks, landTypes, type);
		
		return builder.generationSettings(generation).mobSpawnSettings(spawnInfo);
	}
	
	private static MobSpawnInfo createMobSpawnInfo(LandTypePair landTypes, LandBiomeType type)
	{
		MobSpawnInfo.Builder builder = new MobSpawnInfo.Builder();
		
		builder.addSpawn(MSEntityTypes.CONSORT, new MobSpawnInfo.Spawners(landTypes.getTerrain().getConsortType(), 2, 1, 3));
		
		landTypes.getTerrain().setSpawnInfo(builder, type);
		landTypes.getTitle().setSpawnInfo(builder, type);
		
		return builder.build();
	}
	
	private static BiomeGenerationSettings createGenerationSettings(Biome base, StructureBlockRegistry blocks, LandTypePair landTypes, LandBiomeType type)
	{
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(() -> SurfaceBuilder.DEFAULT.configured(blocks.getSurfaceBuilderConfig(type)));
		
		if(type != LandBiomeType.OCEAN)
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.RETURN_NODE.configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(128));
		
		addDefaultOres(builder, blocks);
		
		addDefaultStructures(builder, type);
		
		landTypes.getTerrain().setBiomeGeneration(builder, blocks, type, base);
		landTypes.getTitle().setBiomeGeneration(builder, blocks, type, base);
		
		return builder.build();
	}
	
	private static void addDefaultOres(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks)
	{
		if(MinestuckConfig.SERVER.generateCruxiteOre.get())
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize))
					.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax))).squared().count(cruxiteVeinsPerChunk));
		}
		if(MinestuckConfig.SERVER.generateUraniumOre.get())
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize))
					.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(uraniumStratumMin, uraniumStratumMin, uraniumStratumMax))).squared().count(uraniumVeinsPerChunk));
		}
	}
	
	private static void addDefaultStructures(BiomeGenerationSettings.Builder builder, LandBiomeType type)
	{
		builder.addStructureStart(MSFeatures.LAND_GATE.configured(IFeatureConfig.NONE));
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addStructureStart(MSFeatures.SMALL_RUIN.configured(IFeatureConfig.NONE));
			builder.addStructureStart(MSFeatures.CONSORT_VILLAGE.configured(IFeatureConfig.NONE));
		}
		if(type == LandBiomeType.NORMAL || type == LandBiomeType.ROUGH)
			builder.addStructureStart(MSFeatures.IMP_DUNGEON.configured(IFeatureConfig.NONE));
	}
}