package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class MSBiomeProvider
{
	public static void register(BootstrapContext<Biome> context)
	{
		HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
		
		context.register(MSBiomes.SKAIA, skaiaBiome());
		context.register(MSBiomes.PROSPIT, prospitBiome());
		context.register(MSBiomes.DERSE, derseBiome());
		context.register(MSBiomes.VEIL, veilBiome(features));
		context.register(MSBiomes.PROSPIT_WFC_DEMO,
				new Biome.BiomeBuilder().temperature(0.5F).downfall(0.5F)
						.specialEffects(new BiomeSpecialEffects.Builder().waterColor(0x3F76E4).waterFogColor(0x050533)
								.fogColor(0xCECEA7).skyColor(0x979CA8).build())
						.mobSpawnSettings(new MobSpawnSettings.Builder().build())
						.generationSettings(new BiomeGenerationSettings.PlainBuilder().build())
						.build());
		
		MSBiomes.DEFAULT_LAND.createForDataGen(context);
		MSBiomes.HIGH_HUMID_LAND.createForDataGen(context);
		MSBiomes.NO_RAIN_LAND.createForDataGen(context);
		MSBiomes.SNOW_LAND.createForDataGen(context);
	}
	
	private static Biome skaiaBiome()
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
		
		return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(0.5F).downfall(0.5F).specialEffects(ambience.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(genSettings.build()).build();
	}
	
	private static Biome prospitBiome()
	{
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_PAWN.get(), 2, 1, 10));
		
		BiomeGenerationSettings.PlainBuilder genSettings = new BiomeGenerationSettings.PlainBuilder();
		
		BiomeSpecialEffects.Builder ambience = new BiomeSpecialEffects.Builder()
				.waterColor(0x00ffd0)
				.waterFogColor(0x00c2b8)
				.grassColorOverride(0xFFB300)
				.foliageColorOverride(0xEBC100);
		ambience.fogColor(0x000000)
				.skyColor(0x000000);
		
		return new Biome.BiomeBuilder().hasPrecipitation(false)
				.temperature(0.5F)
				.downfall(0.5F)
				.specialEffects(ambience.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(genSettings.build()).build();
	}
	
	private static Biome derseBiome()
	{
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_PAWN.get(), 2, 1, 10));
		
		BiomeGenerationSettings.PlainBuilder genSettings = new BiomeGenerationSettings.PlainBuilder();
		
		BiomeSpecialEffects.Builder ambience = new BiomeSpecialEffects.Builder()
				.waterColor(0x2600fc)
				.waterFogColor(0x2000d6);
		ambience.fogColor(0x000000)
				.skyColor(0x000000);
		
		return new Biome.BiomeBuilder().hasPrecipitation(false)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects(ambience.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(genSettings.build()).build();
	}
	
	private static Biome veilBiome(HolderGetter<PlacedFeature> features)
	{
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
		
		BiomeGenerationSettings.PlainBuilder genSettings = new BiomeGenerationSettings.PlainBuilder();
		genSettings.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, features.getOrThrow(MSPlacedFeatures.VEIL_CRATER));
		
		BiomeSpecialEffects.Builder ambience = new BiomeSpecialEffects.Builder()
				.waterColor(0x3F76E4)
				.waterFogColor(0x050533);
		ambience.fogColor(0x000000)
				.skyColor(0x000000);
		
		return new Biome.BiomeBuilder().hasPrecipitation(false)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects(ambience.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(genSettings.build()).build();
	}
}
