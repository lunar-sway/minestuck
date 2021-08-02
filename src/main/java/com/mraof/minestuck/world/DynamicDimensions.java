package com.mraof.minestuck.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.chunk.listener.LoggingChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created with assistance from https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
 */
public class DynamicDimensions
{
	private static final RegistryKey<DimensionType> LAND_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "land"));
	
	public static RegistryKey<World> createLand(MinecraftServer server, ResourceLocation baseName, LandTypePair landTypes)
	{
		Map<RegistryKey<World>, ServerWorld> worldMap = server.forgeGetWorldMap();
		
		RegistryKey<World> worldKey = findUnusedWorldKey(worldMap, baseName);
		
		RegistryKey<Dimension> dimensionKey = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location());
		
		IServerConfiguration serverConfig = server.getWorldData();
		DimensionGeneratorSettings genSettings = serverConfig.worldGenSettings();
		
		ChunkGenerator chunkGenerator = new LandChunkGenerator(genSettings.seed(), landTypes);
		Dimension dimension = new Dimension(() -> server.registryAccess().dimensionTypes().get(LAND_TYPE), chunkGenerator);
		
		genSettings.dimensions().register(dimensionKey, dimension, Lifecycle.experimental());
		
		SaveFormat.LevelSave levelSave = getLevelSave(server);
		DerivedWorldInfo worldInfo = new DerivedWorldInfo(serverConfig, serverConfig.overworldData());
		
		ServerWorld world = new ServerWorld(server, Util.backgroundExecutor(), levelSave, worldInfo, worldKey,
				dimension.type(), new LoggingChunkStatusListener(11), chunkGenerator, genSettings.isDebug(),
				BiomeManager.obfuscateSeed(genSettings.seed()), ImmutableList.of(), false);
		
		server.overworld().getWorldBorder().addListener(new IBorderListener.Impl(world.getWorldBorder()));
		
		worldMap.put(worldKey, world);
		server.markWorldsDirty();
		
		MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
		
		return worldKey;
	}
	
	private static RegistryKey<World> findUnusedWorldKey(Map<RegistryKey<World>, ServerWorld> worldMap, ResourceLocation baseName)
	{
		RegistryKey<World> key = RegistryKey.create(Registry.DIMENSION_REGISTRY, baseName);
		
		for(int i = 0; worldMap.containsKey(key); i++)
		{
			key = RegistryKey.create(Registry.DIMENSION_REGISTRY,
					new ResourceLocation(baseName.getNamespace(), baseName.getPath() + "_" + i));
		}
		
		return key;
	}
	
	private static final Field LEVEL_SAVE_FIELD = ObfuscationReflectionHelper.findField(MinecraftServer.class, "field_71310_m");
	
	private static SaveFormat.LevelSave getLevelSave(MinecraftServer server)
	{
		try
		{
			return (SaveFormat.LevelSave) (LEVEL_SAVE_FIELD.get(server));
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
