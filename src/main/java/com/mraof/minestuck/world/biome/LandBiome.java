package com.mraof.minestuck.world.biome;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public abstract class LandBiome
{
	
	public static Biome createNormalBiome(boolean hasPrecipitation, float temperature, float downfall)
	{
		Biome.BiomeBuilder builder = createBiomeBase().hasPrecipitation(hasPrecipitation)
				.temperature(temperature).downfall(downfall);
		
		return builder.build();
	}
	
	public static Biome createRoughBiome(boolean hasPrecipitation, float temperature, float downfall)
	{
		Biome.BiomeBuilder builder = createBiomeBase().hasPrecipitation(hasPrecipitation)
				.temperature(temperature).downfall(downfall);
		
		return builder.build();
	}
	
	public static Biome createOceanBiome(boolean hasPrecipitation, float temperature, float downfall)
	{
		Biome.BiomeBuilder builder = createBiomeBase().hasPrecipitation(hasPrecipitation)
				.temperature(temperature).downfall(downfall);
		
		return builder.build();
	}
	
	private static Biome.BiomeBuilder createBiomeBase()
	{
		Biome.BiomeBuilder builder = new Biome.BiomeBuilder();
		
		BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder().waterColor(0x3F76E4).waterFogColor(0x050533)
				.fogColor(0xC0D8FF).skyColor(0x7AA4FF);
		
		BiomeGenerationSettings.PlainBuilder generation = new BiomeGenerationSettings.PlainBuilder();
		
		return builder.specialEffects(effects.build()).mobSpawnSettings(new MobSpawnSettings.Builder().build())
				.generationSettings(generation.build());
	}
}