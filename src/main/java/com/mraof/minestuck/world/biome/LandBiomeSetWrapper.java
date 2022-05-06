package com.mraof.minestuck.world.biome;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class LandBiomeSetWrapper implements ILandBiomeSet
{
	public final Biome NORMAL, ROUGH, OCEAN;
	
	public LandBiomeSetWrapper(LandBiomeSet biomes, Registry<Biome> registry)
	{
		NORMAL = registry.get(biomes.NORMAL);
		ROUGH = registry.get(biomes.ROUGH);
		OCEAN = registry.get(biomes.OCEAN);
	}
	
	@Override
	public List<Biome> getAll()
	{
		return ImmutableList.of(NORMAL, ROUGH, OCEAN);
	}
	
	@Override
	public Biome fromType(LandBiomeType type)
	{
		switch(type)
		{
			case NORMAL: default: return NORMAL;
			case ROUGH: return ROUGH;
			case OCEAN: return OCEAN;
		}
	}
	
	public LandBiomeType getTypeFromBiome(Biome biome)
	{
		if(biome == NORMAL)
			return LandBiomeType.NORMAL;
		else if(biome == ROUGH)
			return LandBiomeType.ROUGH;
		else if(biome == OCEAN)
			return LandBiomeType.OCEAN;
		else throw new IllegalArgumentException("Got biome \"" + biome + "\" which is not in the biome set.");
	}
	
}
