package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class MSBiomeProvider
{
	public static void register(BootstapContext<Biome> context)
	{
		HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
		
		context.register(MSBiomes.SKAIA, skaiaBiome(features));
		
		MSBiomes.DEFAULT_LAND.createForDataGen(context);
		MSBiomes.HIGH_HUMID_LAND.createForDataGen(context);
		MSBiomes.NO_RAIN_LAND.createForDataGen(context);
		MSBiomes.SNOW_LAND.createForDataGen(context);
	}
	
	private static Biome skaiaBiome(HolderGetter<PlacedFeature> features)
	{
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_PAWN.get(), 2, 1, 10));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_BISHOP.get(), 1, 1, 1));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_ROOK.get(), 1, 1, 1));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_PAWN.get(), 2, 1, 10));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_BISHOP.get(), 1, 1, 1));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_ROOK.get(), 1, 1, 1));
		
		BiomeGenerationSettings.PlainBuilder genSettings = new BiomeGenerationSettings.PlainBuilder();
		
		BiomeSpecialEffects.Builder ambience = new BiomeSpecialEffects.Builder().waterColor(0x3F76E4).waterFogColor(0x050533);
		ambience.fogColor(0xCDCDFF).skyColor(0x7AA4FF);
		
		genSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, features.getOrThrow(MSPlacedFeatures.ROOK_OUTPOST));
		
		return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(0.5F).downfall(0.5F).specialEffects(ambience.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(genSettings.build()).build();
	}
}
