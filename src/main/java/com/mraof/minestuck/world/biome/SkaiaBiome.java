package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

public class SkaiaBiome
{
	public static Biome makeBiome()
	{
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_PAWN.get(), 2, 1, 10));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_BISHOP.get(), 1, 1, 1));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.DERSITE_ROOK.get(), 1, 1, 1));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_PAWN.get(), 2, 1, 10));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_BISHOP.get(), 1, 1, 1));
		spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(MSEntityTypes.PROSPITIAN_ROOK.get(), 1, 1, 1));
		
		BiomeGenerationSettingsBuilder genSettings = new BiomeGenerationSettingsBuilder(new BiomeGenerationSettings.Builder().build());
		
		BiomeSpecialEffects.Builder ambience = new BiomeSpecialEffects.Builder().waterColor(0x3F76E4).waterFogColor(0x050533);
		ambience.fogColor(0xCDCDFF).skyColor(0x7AA4FF);
		
		return new Biome.BiomeBuilder().precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NONE).temperature(0.5F).downfall(0.5F).specialEffects(ambience.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(genSettings.build()).build();
	}
}