package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Optional;
import java.util.function.BiConsumer;

public class LandBiomeSet
{
	public final RegistryKey<Biome> NORMAL, ROUGH, OCEAN;
	private final Biome.RainType precipitation;
	private final float temperature, downfall;
	
	public LandBiomeSet(String mod, String name, Biome.RainType precipitation, float temperature, float downfall)
	{
		this.precipitation = precipitation;
		this.temperature = temperature;
		this.downfall = downfall;
		NORMAL = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(mod, "land_"+name+"_normal"));
		ROUGH = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(mod, "land_"+name+"_rough"));
		OCEAN = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(mod, "land_"+name+"_ocean"));
	}
	
	public void createForDataGen(BiConsumer<RegistryKey<Biome>, Biome> consumer)
	{
		consumer.accept(NORMAL, LandBiome.createNormalBiome(precipitation, temperature, downfall));
		consumer.accept(ROUGH, LandBiome.createRoughBiome(precipitation, temperature, downfall));
		consumer.accept(OCEAN, LandBiome.createOceanBiome(precipitation, temperature, downfall));
	}
	
	public static Optional<LandBiomeSetWrapper> getSet(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator)
			return Optional.of(((LandChunkGenerator) generator).biomes.baseBiomes);
		else return Optional.empty();
	}
	
	public Biome.RainType getPrecipitation()
	{
		return precipitation;
	}
}