package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.SkaiaObjects;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static com.mraof.minestuck.world.gen.MSNoiseParameters.LAND_CONTINENTS;
import static com.mraof.minestuck.world.gen.MSNoiseParameters.LAND_EROSION;

public final class MSNoiseParametersProvider
{
	public static void register(BootstapContext<NormalNoise.NoiseParameters> context)
	{
		context.register(SkaiaObjects.SKAIA_RIDGES_NOISE, new NormalNoise.NoiseParameters(-8, 1, 1, 1, 1));
		
		context.register(LAND_CONTINENTS, new NormalNoise.NoiseParameters(-6, 1, 1, 1, 1));
		context.register(LAND_EROSION, new NormalNoise.NoiseParameters(-5, 1, 1, 1));
	}
}
