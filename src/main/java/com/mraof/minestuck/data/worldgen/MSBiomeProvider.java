package com.mraof.minestuck.data.worldgen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.biome.SkaiaBiome;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.function.BiConsumer;

public final class MSBiomeProvider
{
	public static DataProvider create(RegistryOps<JsonElement> registryOps, DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		DataEntriesBuilder<Biome> biomes = new DataEntriesBuilder<>();
		generate(biomes::add);
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				registryOps, Registry.BIOME_REGISTRY, biomes.getMap());
	}
	
	private static void generate(BiConsumer<ResourceKey<Biome>, Biome> consumer)
	{
		consumer.accept(MSBiomes.SKAIA, SkaiaBiome.makeBiome());
		
		MSBiomes.DEFAULT_LAND.createForDataGen(consumer);
		MSBiomes.HIGH_HUMID_LAND.createForDataGen(consumer);
		MSBiomes.NO_RAIN_LAND.createForDataGen(consumer);
		MSBiomes.SNOW_LAND.createForDataGen(consumer);
	}
}
