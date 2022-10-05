package com.mraof.minestuck.world.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

/**
 * Interface that provide holders for biomes for some land.
 * Whether those are backed by a registry or created for a specific dimension depends on the implementor.
 */
public interface LandBiomeAccess
{
	Holder<Biome> fromType(LandBiomeType type);
	
	List<Holder<Biome>> getAll();
}
