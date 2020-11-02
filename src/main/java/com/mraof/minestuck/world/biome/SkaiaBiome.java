package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.MSSurfaceBuilders;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

public class SkaiaBiome
{
	public static Biome makeBiome()
	{
		MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();
		spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(MSEntityTypes.DERSITE_PAWN, 2, 1, 10));
		spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(MSEntityTypes.DERSITE_BISHOP, 1, 1, 1));
		spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(MSEntityTypes.DERSITE_ROOK, 1, 1, 1));
		spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(MSEntityTypes.PROSPITIAN_PAWN, 2, 1, 10));
		spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(MSEntityTypes.PROSPITIAN_BISHOP, 1, 1, 1));
		spawnInfo.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(MSEntityTypes.PROSPITIAN_ROOK, 1, 1, 1));
		
		BiomeGenerationSettingsBuilder genSettings = new BiomeGenerationSettingsBuilder(new BiomeGenerationSettings.Builder().withSurfaceBuilder(() -> MSSurfaceBuilders.SKAIA.get().func_242929_a(SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG)).build());	//TODO configured surface builders are now a registry. Make sure to register it
		genSettings.getStructures().add(() -> MSFeatures.SKAIA_CASTLE.withConfiguration(NoFeatureConfig.field_236559_b_));	//TODO configured structure is also being registered in vanilla
		
		BiomeAmbience.Builder ambience = new BiomeAmbience.Builder().setWaterColor(0x3F76E4).setWaterFogColor(0x050533);
		ambience.setFogColor(0xC0D8FF).withSkyColor(0x7AA4FF);
		
		return new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).setEffects(ambience.build()).withMobSpawnSettings(spawnInfo.copy()).withGenerationSettings(genSettings.build()).build();
	}
}