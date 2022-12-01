package com.mraof.minestuck.world.biome;


import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.mraof.minestuck.world.gen.feature.OreGeneration.*;

/**
 * A dimension-specific biome set with biomes created based on a specific land type pair.
 * Biomes in this set are not present in the biome registry,
 * and should thus not be used in a situation where they need to be serialized.
 */
public final class WorldGenBiomeSet implements LandBiomeAccess
{
	private final Holder<Biome> normalBiome, oceanBiome, roughBiome;
	public final RegistryBackedBiomeSet baseBiomes;
	
	@SuppressWarnings("ConstantConditions")
	public WorldGenBiomeSet(RegistryBackedBiomeSet biomes, LandGenSettings settings)
	{
		StructureBlockRegistry blocks = settings.getBlockRegistry();
		LandTypePair landTypes = settings.getLandTypes();
		Biome.BiomeCategory category = landTypes.getTerrain().getBiomeCategory();
		
		baseBiomes = biomes;
		
		GenerationBuilder generationBuilder = new GenerationBuilder();
		addBiomeGeneration(generationBuilder, blocks, landTypes);
		
		normalBiome = Holder.direct(createBiomeBase(biomes, generationBuilder, landTypes, LandBiomeType.NORMAL).biomeCategory(category).build()
				.setRegistryName(biomes.NORMAL.value().getRegistryName()));
		roughBiome = Holder.direct(createBiomeBase(biomes, generationBuilder, landTypes, LandBiomeType.ROUGH).biomeCategory(category).build()
				.setRegistryName(biomes.ROUGH.value().getRegistryName()));
		oceanBiome = Holder.direct(createBiomeBase(biomes, generationBuilder, landTypes, LandBiomeType.OCEAN).biomeCategory(Biome.BiomeCategory.OCEAN).build()
				.setRegistryName(biomes.OCEAN.value().getRegistryName()));
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
		return switch(type)
				{
					case NORMAL -> normalBiome;
					case ROUGH -> roughBiome;
					case OCEAN -> oceanBiome;
				};
	}
	
	private static Biome.BiomeBuilder createBiomeBase(RegistryBackedBiomeSet baseBiomes, GenerationBuilder generationBuilder, LandTypePair landTypes, LandBiomeType type)
	{
		Biome base = baseBiomes.fromType(type).value();
		Biome.BiomeBuilder builder = new Biome.BiomeBuilder().precipitation(base.getPrecipitation())
				.temperature(base.getBaseTemperature()).downfall(base.getDownfall()).specialEffects(base.getSpecialEffects());
		
		MobSpawnSettings spawnInfo = createMobSpawnInfo(landTypes, type);
		
		return builder.generationSettings(generationBuilder.settings.get(type).build()).mobSpawnSettings(spawnInfo);
	}
	
	private static MobSpawnSettings createMobSpawnInfo(LandTypePair landTypes, LandBiomeType type)
	{
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
		
		builder.addSpawn(MSEntityTypes.CONSORT, new MobSpawnSettings.SpawnerData(landTypes.getTerrain().getConsortType(), 2, 1, 3));
		
		landTypes.getTerrain().setSpawnInfo(builder, type);
		landTypes.getTitle().setSpawnInfo(builder, type);
		
		return builder.build();
	}
	
	private static void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandTypePair landTypes)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RETURN_NODE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		//TODO change these if land world heights are modified
		if(MinestuckConfig.SERVER.generateCruxiteOre.get())
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
							new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize),
							CountPlacement.of(cruxiteVeinsPerChunk), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(cruxiteStratumMin), VerticalAnchor.absolute(cruxiteStratumMax)), BiomeFilter.biome()),
					LandBiomeType.any());
		}
		if(MinestuckConfig.SERVER.generateUraniumOre.get())
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
							new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize),
							CountPlacement.of(uraniumVeinsPerChunk), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(uraniumStratumMinAboveBottom), VerticalAnchor.aboveBottom(uraniumStratumMaxAboveBottom)), BiomeFilter.biome()),
					LandBiomeType.any());
		}
		
		landTypes.getTerrain().addBiomeGeneration(builder, blocks);
		landTypes.getTitle().addBiomeGeneration(builder, blocks, landTypes.getTerrain().getBiomeSet());
	}
	
	private static class GenerationBuilder implements LandBiomeGenBuilder
	{
		private final Map<LandBiomeType, BiomeGenerationSettings.Builder> settings = new EnumMap<>(LandBiomeType.class);
		{
			for(LandBiomeType type : LandBiomeType.values())
				settings.put(type, new BiomeGenerationSettings.Builder());
		}
		
		@Override
		public void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature, LandBiomeType... types)
		{
			for(LandBiomeType type : types)
				settings.get(type).addFeature(step, feature);
		}
		
		@Override
		public void addCarver(GenerationStep.Carving step, Holder<? extends ConfiguredWorldCarver<?>> carver, LandBiomeType... types)
		{
			for(LandBiomeType type : types)
				settings.get(type).addCarver(step, carver);
		}
	}
}