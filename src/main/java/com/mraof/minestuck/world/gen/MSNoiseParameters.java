package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class MSNoiseParameters
{
	public static final ResourceKey<NormalNoise.NoiseParameters> SKAIA_RIDGES = key("skaia/ridges");
	
	public static final ResourceKey<NormalNoise.NoiseParameters> LAND_CONTINENTS = key("land/continents");
	public static final ResourceKey<NormalNoise.NoiseParameters> LAND_EROSION = key("land/erosion");
	
	private static ResourceKey<NormalNoise.NoiseParameters> key(String name)
	{
		return ResourceKey.create(Registries.NOISE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
}
