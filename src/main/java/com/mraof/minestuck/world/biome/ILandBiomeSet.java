package com.mraof.minestuck.world.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public interface ILandBiomeSet
{
	Holder<Biome> fromType(LandBiomeType type);
	
	List<Holder<Biome>> getAll();
}
