package com.mraof.minestuck.world;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import java.util.Objects;

public class DynamicDimensions
{
	public static final ResourceKey<DimensionType> LAND_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Minestuck.MOD_ID, "land"));
	private static final ResourceLocation LAND_BASE_ID = new ResourceLocation(Minestuck.MOD_ID, "land");
	
	public static ResourceLocation landIdBaseForPLayer(PlayerIdentifier player)
	{
		ResourceLocation dimensionName = ResourceLocation.tryBuild(Minestuck.MOD_ID, "land_" + player.getUsername().toLowerCase());
		return Objects.requireNonNullElse(dimensionName, LAND_BASE_ID);
	}
	
	public static ResourceKey<Level> createLand(MinecraftServer server, ResourceLocation baseName, LandTypePair landTypes)
	{
		ResourceKey<Level> worldKey = findUnusedWorldKey(server, baseName);
		
		InfiniverseAPI.get().getOrCreateLevel(server, worldKey, () -> {
			RandomSource random = WorldgenRandom.Algorithm.XOROSHIRO.newInstance(server.getWorldData().worldGenOptions().seed())
					.forkPositional().fromHashOf(worldKey.location());
			
			LandTypePair.Named named = landTypes.createNamedRandomly(random.fork());
			
			RegistryAccess registryAccess = server.registryAccess();
			LandChunkGenerator chunkGenerator = LandChunkGenerator.create(registryAccess.lookupOrThrow(Registries.NOISE), registryAccess.lookupOrThrow(Registries.DENSITY_FUNCTION), registryAccess.lookupOrThrow(Registries.STRUCTURE),
					named, registryAccess.lookupOrThrow(Registries.BIOME), registryAccess.lookupOrThrow(Registries.PLACED_FEATURE), registryAccess.lookupOrThrow(Registries.CONFIGURED_CARVER));
			chunkGenerator.tryInit(LandTypeExtensions.get());
			Holder<DimensionType> dimensionType = registryAccess.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(LAND_TYPE);
			return new LevelStem(dimensionType, chunkGenerator);
		});
		
		return worldKey;
	}
	
	private static ResourceKey<Level> findUnusedWorldKey(MinecraftServer server, ResourceLocation baseName)
	{
		ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, baseName);
		
		for(int i = 0; server.getLevel(key) != null; i++)
		{
			key = ResourceKey.create(Registries.DIMENSION,
					new ResourceLocation(baseName.getNamespace(), baseName.getPath() + "_" + i));
		}
		
		return key;
	}
}
