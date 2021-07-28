package com.mraof.minestuck.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.biome.SkaiaBiome;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class MinestuckBiomeProvider implements IDataProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	
	public MinestuckBiomeProvider(DataGenerator generator)
	{
		this.generator = generator;
	}
	
	protected void generateBiomes(BiConsumer<RegistryKey<Biome>, Biome> consumer)
	{
		consumer.accept(MSBiomes.SKAIA, SkaiaBiome.makeBiome());
	}
	
	@Override
	public void run(DirectoryCache cache) throws IOException
	{
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> writtenBiomes = Sets.newHashSet();
		
		generateBiomes((key, biome) -> {
			ResourceLocation location = key.location();
			if(!writtenBiomes.add(location))
				throw new IllegalStateException("Duplicate biome " + location);
			else
			{
				try
				{
					Optional<JsonElement> result = Biome.CODEC.encodeStart(JsonOps.INSTANCE, () -> biome).result();
					if(result.isPresent())
					{
						Path biomePath = path.resolve("data/" + location.getNamespace() + "/worldgen/biome/" + location.getPath() + ".json");
						IDataProvider.save(GSON, cache, result.get(), biomePath);
					} else LOGGER.error("Couldn't serialize biome {}", location);
				} catch(IOException e)
				{
					LOGGER.error("Couldn't save biome {}", location, e);
				}
			}
		});
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Biomes";
	}
}
