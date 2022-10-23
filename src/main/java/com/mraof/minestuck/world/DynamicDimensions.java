package com.mraof.minestuck.world;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.LandTypePair;
import commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import java.util.Random;

public class DynamicDimensions
{
	private static final ResourceKey<DimensionType> LAND_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "land"));
	
	public static ResourceKey<Level> createLand(MinecraftServer server, ResourceLocation baseName, LandTypePair landTypes)
	{
		ResourceKey<Level> worldKey = findUnusedWorldKey(server, baseName);
		
		InfiniverseAPI.get().getOrCreateLevel(server, worldKey, () -> {
			RandomSource random = WorldgenRandom.Algorithm.XOROSHIRO.newInstance(server.getWorldData().worldGenSettings().seed())
					.forkPositional().fromHashOf(worldKey.location());
			
			LandTypePair.Named named = landTypes.createNamedRandomly(new Random(random.nextLong()));
			long seed = random.nextLong();
			
			ChunkGenerator chunkGenerator = LandChunkGenerator.create(server.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), server.registryAccess().registryOrThrow(Registry.NOISE_REGISTRY), server.registryAccess().registryOrThrow(Registry.DENSITY_FUNCTION_REGISTRY),
					seed, named, server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
			return new LevelStem(server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrCreateHolder(LAND_TYPE), chunkGenerator);
		});
		
		return worldKey;
	}
	
	private static ResourceKey<Level> findUnusedWorldKey(MinecraftServer server, ResourceLocation baseName)
	{
		ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, baseName);
		
		for(int i = 0; server.getLevel(key) != null; i++)
		{
			key = ResourceKey.create(Registry.DIMENSION_REGISTRY,
					new ResourceLocation(baseName.getNamespace(), baseName.getPath() + "_" + i));
		}
		
		return key;
	}
}
