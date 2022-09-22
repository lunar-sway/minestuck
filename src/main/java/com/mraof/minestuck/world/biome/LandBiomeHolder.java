package com.mraof.minestuck.world.biome;


import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.List;

import static com.mraof.minestuck.world.gen.OreGeneration.*;

public class LandBiomeHolder implements ILandBiomeSet
{
	private final Holder<Biome> normalBiome, oceanBiome, roughBiome;
	public final LandBiomeSetWrapper baseBiomes;
	
	@SuppressWarnings("ConstantConditions")
	public LandBiomeHolder(LandBiomeSetWrapper biomes, LandGenSettings settings)
	{
		StructureBlockRegistry blocks = settings.getBlockRegistry();
		Biome.BiomeCategory category = settings.getLandTypes().getTerrain().getBiomeCategory();
		
		baseBiomes = biomes;
		
		normalBiome = Holder.direct(createNormal(biomes, blocks, category, settings.getLandTypes())
				.setRegistryName(biomes.NORMAL.value().getRegistryName()));
		roughBiome = Holder.direct(createRough(biomes, blocks, category, settings.getLandTypes())
				.setRegistryName(biomes.ROUGH.value().getRegistryName()));
		oceanBiome = Holder.direct(createOcean(biomes, blocks, settings.getLandTypes())
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
	
	private static Biome createNormal(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, Biome.BiomeCategory category, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.NORMAL).biomeCategory(category).build();
	}
	
	private static Biome createRough(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, Biome.BiomeCategory category, LandTypePair landTypes)
	{
		return createBiomeBase(baseBiomes, blocks, landTypes, LandBiomeType.ROUGH).biomeCategory(category).build();
	}
	
	private static Biome createOcean(LandBiomeSetWrapper baseBiomes, StructureBlockRegistry blocks, LandTypePair landTypes)
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
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder();
		
		if(type != LandBiomeType.OCEAN)
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RETURN_NODE.getHolder().orElseThrow());
		
		addDefaultOres(builder, blocks);
		
		landTypes.getTerrain().setBiomeGeneration(builder, blocks, type, base);
		landTypes.getTitle().setBiomeGeneration(builder, blocks, type, base);
		
		return builder.build();
	}
	
	private static void addDefaultOres(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks)
	{
		//TODO change these if land world heights are modified
		if(MinestuckConfig.SERVER.generateCruxiteOre.get())
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("cruxite_ore"), baseCruxiteVeinSize),
					CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)), BiomeFilter.biome()));
		}
		if(MinestuckConfig.SERVER.generateUraniumOre.get())
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), blocks.getBlockState("uranium_ore"), baseUraniumVeinSize),
					CountPlacement.of(5), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(35)), BiomeFilter.biome()));
		}
	}
}