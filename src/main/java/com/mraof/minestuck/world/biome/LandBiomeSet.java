package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.Optional;
import java.util.function.BiConsumer;

public class LandBiomeSet
{
	public final ResourceKey<Biome> NORMAL, ROUGH, OCEAN;
	private final Biome.Precipitation precipitation;
	private final float temperature, downfall;
	
	public LandBiomeSet(String mod, String name, Biome.Precipitation precipitation, float temperature, float downfall)
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
	
	public static Optional<LandBiomeSetWrapper> getSet(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator landGen)
			return Optional.of(landGen.biomes.baseBiomes);
		else return Optional.empty();
	}
	
	public Biome.Precipitation getPrecipitation()
	{
		return precipitation;
	}
}