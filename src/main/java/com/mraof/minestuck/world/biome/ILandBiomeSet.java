package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;

import java.util.List;

public interface ILandBiomeSet
{
	Biome fromType(LandBiomeType type);
	
	List<Biome> getAll();
}
