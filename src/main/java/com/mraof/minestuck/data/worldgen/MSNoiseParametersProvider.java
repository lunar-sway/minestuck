package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.function.BiConsumer;

import static com.mraof.minestuck.world.gen.MSNoiseParameters.*;

public final class MSNoiseParametersProvider
{
	public static DataProvider create(RegistryAccess.Writable registryAccess, DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		var registry = registryAccess.ownedWritableRegistryOrThrow(Registry.NOISE_REGISTRY);
		DataEntriesBuilder<NormalNoise.NoiseParameters> builder = new DataEntriesBuilder<>(registry);
		generate(builder::add);
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				RegistryOps.create(JsonOps.INSTANCE, registryAccess), Registry.NOISE_REGISTRY, builder.getMap());
	}
	
	private static void generate(BiConsumer<ResourceKey<NormalNoise.NoiseParameters>, NormalNoise.NoiseParameters> consumer)
	{
		consumer.accept(SKAIA_RIDGES, new NormalNoise.NoiseParameters(-8, 1, 1, 1, 1));
		
		consumer.accept(LAND_CONTINENTS, new NormalNoise.NoiseParameters(-6, 1, 1, 1, 1));
		consumer.accept(LAND_EROSION, new NormalNoise.NoiseParameters(-5, 1, 1, 1));
	}
}
