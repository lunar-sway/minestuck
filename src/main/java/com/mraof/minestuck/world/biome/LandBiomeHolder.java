package com.mraof.minestuck.world.biome;


import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;

public class LandBiomeHolder implements ILandBiomeSet
{
	private final Holder<Biome> normalBiome, oceanBiome, roughBiome;
	public final LandBiomeSetWrapper baseBiomes;
	
	public LandBiomeHolder(LandBiomeSetWrapper biomes, LandGenSettings settings, LandProperties properties)
	{
		StructureBlockRegistry blocks = settings.getBlockRegistry();
		
		baseBiomes = biomes;
		
		normalBiome = Holder.direct(createNormal(biomes, blocks, properties, settings.getLandTypes()));
		roughBiome = Holder.direct(createRough(biomes, blocks, properties, settings.getLandTypes()));
		oceanBiome = Holder.direct(createOcean(biomes, blocks, properties, settings.getLandTypes()));
	}
	
	public Holder<Biome> getBiomeFromBase(Holder<Biome> biome)
	{
		return fromType(baseBiomes.getTypeFromBiome(biome));
	}
	
	@Override
	public List<Holder<Biome>> getAll()
	{
		return ImmutableList.of(normalBiome, roughBiome, oceanBiome);
	}
	
	@Override
	public Holder<Biome> fromType(LandBiomeType type)
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
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.NORMAL).biomeCategory(properties.category).build();
	}
	
	private static Biome createRough(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandProperties properties, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.ROUGH).biomeCategory(properties.category).build();
	}
	
	private static Biome createOcean(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandProperties properties, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.OCEAN).biomeCategory(Biome.BiomeCategory.OCEAN).build();
	}
	
	private static Biome.BiomeBuilder createBiomeBase(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandTypePair landTypes, LandBiomeType type)
	{
		Biome base = baseBiomes.fromType(type).value();
		Biome.BiomeBuilder builder = new Biome.BiomeBuilder().precipitation(base.getPrecipitation())
				.temperature(base.getBaseTemperature()).downfall(base.getDownfall()).specialEffects(base.getSpecialEffects());
		
		MobSpawnSettings spawnInfo = createMobSpawnInfo(landTypes, type);
		
		BiomeGenerationSettings generation = createGenerationSettings(base, blocks, landTypes, type);
		
		return builder.generationSettings(generation).mobSpawnSettings(spawnInfo);
	}
	
	private static MobSpawnSettings createMobSpawnInfo(LandTypePair landTypes, LandBiomeType type)
	{
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
		
		builder.addSpawn(MSEntityTypes.CONSORT, new MobSpawnSettings.SpawnerData(landTypes.getTerrain().getConsortType(), 2, 1, 3));
		
		landTypes.getTerrain().setSpawnInfo(builder, type);
		landTypes.getTitle().setSpawnInfo(builder, type);
		
		return builder.build();
	}
	
	private static BiomeGenerationSettings createGenerationSettings(Biome base, StructureBlockRegistry blocks, LandTypePair landTypes, LandBiomeType type)
	{
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder()/*TODO surface rule
				.surfaceBuilder(() -> SurfaceBuilder.DEFAULT.configured(blocks.getSurfaceBuilderConfig(type)))*/;
		/*
		if(type != LandBiomeType.OCEAN)
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSCFeatures.RETURN_NODE.placed(RarityFilter.onAverageOnceEvery(128), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
		*/
		addDefaultOres(builder, blocks);
		
		addDefaultStructures(builder, type);
		
		landTypes.getTerrain().setBiomeGeneration(builder, blocks, type, base);
		landTypes.getTitle().setBiomeGeneration(builder, blocks, type, base);
		
		return builder.build();
	}
	
	private static void addDefaultOres(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks)
	{/*TODO
		if(MinestuckConfig.SERVER.generateCruxiteOre.get())
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize))
					.placed(CountPlacement.of(cruxiteVeinsPerChunk), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(cruxiteStratumMax)), BiomeFilter.biome()));
		}
		if(MinestuckConfig.SERVER.generateUraniumOre.get())
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize))
					.placed(CountPlacement.of(uraniumVeinsPerChunk), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(uraniumStratumMax)), BiomeFilter.biome()));
		}*/
	}
	
	private static void addDefaultStructures(BiomeGenerationSettings.Builder builder, LandBiomeType type)
	{/*TODO should go somewhere else
		builder.addStructureStart(MSFeatures.LAND_GATE.configured(FeatureConfiguration.NONE));
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addStructureStart(MSFeatures.SMALL_RUIN.configured(FeatureConfiguration.NONE));
			builder.addStructureStart(MSFeatures.CONSORT_VILLAGE.configured(FeatureConfiguration.NONE));
		}
		if(type == LandBiomeType.NORMAL || type == LandBiomeType.ROUGH)
			builder.addStructureStart(MSFeatures.IMP_DUNGEON.configured(FeatureConfiguration.NONE));
			*/
	}
}