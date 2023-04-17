package com.mraof.minestuck.data.worldgen;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.biome.SkaiaBiome;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class MinestuckBiomeProvider implements DataProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final DataGenerator generator;
	protected final ExistingFileHelper existingFileHelper;
	
	public MinestuckBiomeProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		this.generator = generator;
		this.existingFileHelper = existingFileHelper;
	}
	
	protected void generateBiomes(BiConsumer<ResourceKey<Biome>, Biome> consumer)
	{
		consumer.accept(MSBiomes.SKAIA, SkaiaBiome.makeBiome());
		
		MSBiomes.DEFAULT_LAND.createForDataGen(consumer);
		MSBiomes.HIGH_HUMID_LAND.createForDataGen(consumer);
		MSBiomes.NO_RAIN_LAND.createForDataGen(consumer);
		MSBiomes.SNOW_LAND.createForDataGen(consumer);
	}
	
	@Override
	public void run(CachedOutput cache) throws IOException
	{
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> writtenBiomes = Sets.newHashSet();
		
		generateBiomes((key, biome) -> {
			ResourceLocation location = key.location();
			if(!writtenBiomes.add(location))
				throw new IllegalStateException("Duplicate biome " + location);
			else
			{
				existingFileHelper.trackGenerated(location, PackType.SERVER_DATA, ".json", "worldgen/biome");
				try
				{
					Optional<JsonElement> result = Biome.CODEC.encodeStart(JsonOps.INSTANCE, Holder.direct(biome)).result();
					if(result.isPresent())
					{
						Path biomePath = path.resolve("data/" + location.getNamespace() + "/worldgen/biome/" + location.getPath() + ".json");
						DataProvider.saveStable(cache, result.get(), biomePath);
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
