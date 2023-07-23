package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.biome.SkaiaBiome;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;

public final class MSBiomeProvider
{
	public static void register(BootstapContext<Biome> context)
	{
		context.register(MSBiomes.SKAIA, SkaiaBiome.makeBiome());
		
		MSBiomes.DEFAULT_LAND.createForDataGen(context);
		MSBiomes.HIGH_HUMID_LAND.createForDataGen(context);
		MSBiomes.NO_RAIN_LAND.createForDataGen(context);
		MSBiomes.SNOW_LAND.createForDataGen(context);
	}
}
