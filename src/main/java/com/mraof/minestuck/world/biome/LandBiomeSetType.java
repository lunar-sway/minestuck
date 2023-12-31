package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.Optional;

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
	private final boolean hasPrecipitation;
	private final float temperature, downfall;
	
	public LandBiomeSetType(String mod, String name, boolean hasPrecipitation, float temperature, float downfall)
	{
		this.hasPrecipitation = hasPrecipitation;
		this.temperature = temperature;
		this.downfall = downfall;
		NORMAL = ResourceKey.create(Registries.BIOME, new ResourceLocation(mod, "land_"+name+"_normal"));
		ROUGH = ResourceKey.create(Registries.BIOME, new ResourceLocation(mod, "land_"+name+"_rough"));
		OCEAN = ResourceKey.create(Registries.BIOME, new ResourceLocation(mod, "land_"+name+"_ocean"));
	}
	
	public void createForDataGen(BootstapContext<Biome> context)
	{
		context.register(NORMAL, LandBiome.createNormalBiome(hasPrecipitation, temperature, downfall));
		context.register(ROUGH, LandBiome.createRoughBiome(hasPrecipitation, temperature, downfall));
		context.register(OCEAN, LandBiome.createOceanBiome(hasPrecipitation, temperature, downfall));
	}
	
	public boolean hasPrecipitation()
	{
		return hasPrecipitation;
	}
	
	public float getTemperature()
	{
		return temperature;
	}
}