package com.mraof.minestuck.data.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static com.mraof.minestuck.world.gen.MSNoiseParameters.*;

public final class MSNoiseParametersProvider
{
	public static void register(BootstapContext<NormalNoise.NoiseParameters> context)
	{
		context.register(SKAIA_RIDGES, new NormalNoise.NoiseParameters(-8, 1, 1, 1, 1));
		
		context.register(LAND_CONTINENTS, new NormalNoise.NoiseParameters(-6, 1, 1, 1, 1));
		context.register(LAND_EROSION, new NormalNoise.NoiseParameters(-5, 1, 1, 1));
	}
}
