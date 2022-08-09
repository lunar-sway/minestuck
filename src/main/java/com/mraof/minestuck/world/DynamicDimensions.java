package com.mraof.minestuck.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created with assistance from https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
 */
public class DynamicDimensions
{
	private static final ResourceKey<DimensionType> LAND_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "land"));
	
	public static ResourceKey<Level> createLand(MinecraftServer server, ResourceLocation baseName, LandTypePair landTypes)
	{
		Map<ResourceKey<Level>, ServerLevel> worldMap = server.forgeGetWorldMap();
		
		ResourceKey<Level> worldKey = findUnusedWorldKey(worldMap, baseName);
		
		ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location());
		
		WorldData worldData = server.getWorldData();
		WorldGenSettings genSettings = worldData.worldGenSettings();
		
		ChunkGenerator chunkGenerator = new LandChunkGenerator(server.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), server.registryAccess().registryOrThrow(Registry.NOISE_REGISTRY),
				genSettings.seed() + worldKey.location().getPath().hashCode(), landTypes, server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
		LevelStem dimension = new LevelStem(server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrCreateHolder(LAND_TYPE), chunkGenerator);
		
		((WritableRegistry<LevelStem>) genSettings.dimensions()).register(dimensionKey, dimension, Lifecycle.experimental());
		
		LevelStorageSource.LevelStorageAccess levelSave = getLevelSave(server);
		DerivedLevelData worldInfo = new DerivedLevelData(worldData, worldData.overworldData());
		
		ServerLevel level = new ServerLevel(server, Util.backgroundExecutor(), levelSave, worldInfo, worldKey,
				dimension.typeHolder(), new LoggerChunkProgressListener(11), chunkGenerator, genSettings.isDebug(),
				BiomeManager.obfuscateSeed(genSettings.seed()), ImmutableList.of(), false);
		
		server.overworld().getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(level.getWorldBorder()));
		
		worldMap.put(worldKey, level);
		server.markWorldsDirty();
		
		MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(level));
		
		return worldKey;
	}
	
	private static ResourceKey<Level> findUnusedWorldKey(Map<ResourceKey<Level>, ServerLevel> worldMap, ResourceLocation baseName)
	{
		ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, baseName);
		
		for(int i = 0; worldMap.containsKey(key); i++)
		{
			key = ResourceKey.create(Registry.DIMENSION_REGISTRY,
					new ResourceLocation(baseName.getNamespace(), baseName.getPath() + "_" + i));
		}
		
		return key;
	}
	
	private static final Field LEVEL_SAVE_FIELD = ObfuscationReflectionHelper.findField(MinecraftServer.class, "f_129744_");
	
	private static LevelStorageSource.LevelStorageAccess getLevelSave(MinecraftServer server)
	{
		try
		{
			return (LevelStorageSource.LevelStorageAccess) (LEVEL_SAVE_FIELD.get(server));
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
