package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.biome.LandBiomeSet;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

public class CastToBiomeLayer implements IC0Transformer
{
	private final LandBiomeSet biomes;
	
	public CastToBiomeLayer(LandBiomeSet biomes)
	{
		this.biomes = biomes;
	}
	
	@Override
	public int apply(INoiseRandom context, int value)
	{
		//This makes assumptions about the value passed it that should probably be
		// fine as long as people understand how these layers work
		return 0;//Registry.BIOME.getId(biomes.fromType(BiomeType.values()[value]));TODO
	}
}