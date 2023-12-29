package com.mraof.minestuck.world.biome;


import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

import static com.mraof.minestuck.world.gen.feature.OreGeneration.*;

/**
 * A dimension-specific biome set with biomes created based on a specific land type pair.
 * Biomes in this set are not present in the biome registry,
 * and should thus not be used in a situation where they need to be serialized.
 */
public final class LandCustomBiomeSettings
{
	private final BiomeProperties normalBiome, oceanBiome, roughBiome;
	public final RegistryBackedBiomeSet baseBiomes;
	
	@SuppressWarnings("ConstantConditions")
	public LandCustomBiomeSettings(RegistryBackedBiomeSet biomes, LandGenSettings settings, LandTypeExtensions extensions,
								   HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers)
	{
		StructureBlockRegistry blocks = settings.getBlockRegistry();
		LandTypePair landTypes = settings.getLandTypes();
		
		baseBiomes = biomes;
		
		GenerationBuilder generationBuilder = new GenerationBuilder(features, carvers);
		addBiomeGeneration(generationBuilder, blocks, landTypes, extensions);
		
		normalBiome = createBiomeProperties(generationBuilder, extensions, landTypes, LandBiomeType.NORMAL);
		roughBiome = createBiomeProperties(generationBuilder, extensions, landTypes, LandBiomeType.ROUGH);
		oceanBiome = createBiomeProperties(generationBuilder, extensions, landTypes, LandBiomeType.OCEAN);
	}
	
	public BiomeProperties propertiesFor(Holder<Biome> biome)
	{
		return switch(baseBiomes.getTypeFromBiome(biome))
		{
			case NORMAL -> normalBiome;
			case ROUGH -> roughBiome;
			case OCEAN -> oceanBiome;
		};
	}
	
	public BiomeGenerationSettings generationFor(Holder<Biome> baseBiome)
	{
		return propertiesFor(baseBiome).generationSettings();
	}
	
	public MobSpawnSettings customMobSpawnsFor(Holder<Biome> baseBiome)
	{
		return propertiesFor(baseBiome).mobSettings();
	}
	
	private static BiomeProperties createBiomeProperties(GenerationBuilder generationBuilder, LandTypeExtensions extensions, LandTypePair landTypes, LandBiomeType type)
	{
		BiomeGenerationSettings generationSettings = generationBuilder.settings.get(type).build();
		MobSpawnSettings spawnSettings = createMobSpawnInfo(extensions, landTypes, type);
		return new BiomeProperties(generationSettings, spawnSettings);
	}
	
	private static MobSpawnSettings createMobSpawnInfo(LandTypeExtensions extensions, LandTypePair landTypes, LandBiomeType type)
	{
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
		
		builder.addSpawn(MSEntityTypes.CONSORT, new MobSpawnSettings.SpawnerData(landTypes.getTerrain().getConsortType(), 2, 1, 3));
		
		landTypes.getTerrain().setSpawnInfo(builder, type);
		landTypes.getTitle().setSpawnInfo(builder, type);
		extensions.addMobSpawnExtensions(builder, type, landTypes);
		
		return builder.build();
	}
	
	private static void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandTypePair landTypes, LandTypeExtensions extensions)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RETURN_NODE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize),
						CountPlacement.of(cruxiteVeinsPerChunk), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(cruxiteStratumMin), VerticalAnchor.absolute(cruxiteStratumMax)), BiomeFilter.biome()),
				LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize),
						CountPlacement.of(uraniumVeinsPerChunk), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(uraniumStratumMinAboveBottom), VerticalAnchor.aboveBottom(uraniumStratumMaxAboveBottom)), BiomeFilter.biome()),
				LandBiomeType.any());
		
		landTypes.getTerrain().addBiomeGeneration(builder, blocks);
		landTypes.getTitle().addBiomeGeneration(builder, blocks, landTypes.getTerrain().getBiomeSet());
		
		extensions.addBiomeGenExtensions(builder, landTypes);
	}
	
	private static class GenerationBuilder implements LandBiomeGenBuilder
	{
		private final HolderGetter<PlacedFeature> features;
		private final Map<LandBiomeType, BiomeGenerationSettings.Builder> settings = new EnumMap<>(LandBiomeType.class);
		
		private GenerationBuilder(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers)
		{
			this.features = features;
			for(LandBiomeType type : LandBiomeType.values())
				settings.put(type, new BiomeGenerationSettings.Builder(features, carvers));
		}
		
		@Override
		public void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types)
		{
			if(modifier != null)
				feature = modifier.map(feature);
			
			for(LandBiomeType type : types)
				this.settings.get(type).addFeature(step, feature);
		}
		
		@Override
		public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types)
		{
			this.addFeature(step, features.getOrThrow(feature), modifier, types);
		}
		
		@Override
		public void addCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> carver, LandBiomeType... types)
		{
			for(LandBiomeType type : types)
				settings.get(type).addCarver(step, carver);
		}
		
		@Override
		public void addCarver(GenerationStep.Carving step, Holder<ConfiguredWorldCarver<?>> carver, LandBiomeType... types)
		{
			for(LandBiomeType type : types)
				settings.get(type).addCarver(step, carver);
		}
	}
	
	public record BiomeProperties(BiomeGenerationSettings generationSettings, MobSpawnSettings mobSettings)
	{}
}
