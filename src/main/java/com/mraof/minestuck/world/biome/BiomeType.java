package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;

public enum BiomeType
{
	NORMAL, ROUGH, OCEAN;
	
	public boolean isBiomeOfType(Biome biome)
	{
		return false;//biome instanceof LandBiome && ((LandBiome) biome).type == this; TODO
	}
}