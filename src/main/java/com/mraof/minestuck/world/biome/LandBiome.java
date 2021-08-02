package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public abstract class LandBiome
{
	public static Biome createNormalBiome(Biome.RainType precipitation, float temperature, float downfall)
	{
		Biome.Builder builder = createBiomeBase().precipitation(precipitation).biomeCategory(Biome.Category.NONE)
				.depth(0.125F).scale(0.05F).temperature(temperature).downfall(downfall);
		
		return builder.build();
	}
	
	public static Biome createRoughBiome(Biome.RainType precipitation, float temperature, float downfall)
	{
		Biome.Builder builder = createBiomeBase().precipitation(precipitation).biomeCategory(Biome.Category.NONE)
				.depth(0.45F).scale(0.3F).temperature(temperature).downfall(downfall);
		
		return builder.build();
	}
	
	public static Biome createOceanBiome(Biome.RainType precipitation, float temperature, float downfall)
	{
		Biome.Builder builder = createBiomeBase().precipitation(precipitation).biomeCategory(Biome.Category.OCEAN)
				.depth(-1.0F).scale(0.1F).temperature(temperature).downfall(downfall);
		
		return builder.build();
	}
	
	private static Biome.Builder createBiomeBase()
	{
		Biome.Builder builder = new Biome.Builder();
		
		BiomeAmbience.Builder ambience = new BiomeAmbience.Builder().waterColor(0x3F76E4).waterFogColor(0x050533)
				.fogColor(0xC0D8FF).skyColor(0x7AA4FF);
		
		BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(() -> SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_GRASS));
		
		return builder.specialEffects(ambience.build()).mobSpawnSettings(new MobSpawnInfo.Builder().build())
				.generationSettings(generation.build());
	}
}