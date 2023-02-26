package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Reference to a set of land biomes in the biome registry.
 * Defines the general properties of the biome set and refers to the biomes of the set as registry keys.
 * The properties gets used during datagen to generate json data for the biomes in the set.
 * <p>
 * Useful when checking the characteristics of a land type,
 * or to reference a biome set outside the context of a specific world.
 */
public final class LandBiomeSetType
{
	public final ResourceKey<Biome> NORMAL, ROUGH, OCEAN;
	private final Biome.Precipitation precipitation;
	private final float temperature, downfall;
	
	public LandBiomeSetType(String mod, String name, Biome.Precipitation precipitation, float temperature, float downfall)
	{
		this.precipitation = precipitation;
		this.temperature = temperature;
		this.downfall = downfall;
		NORMAL = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(mod, "land_"+name+"_normal"));
		ROUGH = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(mod, "land_"+name+"_rough"));
		OCEAN = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(mod, "land_"+name+"_ocean"));
	}
	
	public void createForDataGen(BiConsumer<ResourceKey<Biome>, Biome> consumer)
	{
		consumer.accept(NORMAL, LandBiome.createNormalBiome(precipitation, temperature, downfall));
		consumer.accept(ROUGH, LandBiome.createRoughBiome(precipitation, temperature, downfall));
		consumer.accept(OCEAN, LandBiome.createOceanBiome(precipitation, temperature, downfall));
	}
	
	public static Optional<RegistryBackedBiomeSet> getSet(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator landGen)
			return Optional.of(landGen.biomes.baseBiomes);
		else return Optional.empty();
	}
	
	public Biome.Precipitation getPrecipitation()
	{
		return precipitation;
	}
	
	public float getTemperature()
	{
		return temperature;
	}
}