package com.mraof.minestuck.world.biome;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

/**
 * A land biome set that is backed by a registry.
 * Should be used as a land biome access in a situation where the biome must be serializable,
 * such as the biomes that get stored in a chunk and get saved to disk or synced to client side.
 * <p>
 * Is tied directly to an instance of {@link LandBiomeSetType}.
 */
public final class RegistryBackedBiomeSet implements LandBiomeAccess
{
	public final Holder.Reference<Biome> NORMAL, ROUGH, OCEAN;
	
	public RegistryBackedBiomeSet(LandBiomeSetType biomes, HolderGetter<Biome> registry)
	{
		NORMAL = registry.getOrThrow(biomes.NORMAL);
		ROUGH = registry.getOrThrow(biomes.ROUGH);
		OCEAN = registry.getOrThrow(biomes.OCEAN);
	}
	
	@Override
	public List<Holder<Biome>> getAll()
	{
		return ImmutableList.of(NORMAL, ROUGH, OCEAN);
	}
	
	@Override
	public Holder<Biome> fromType(LandBiomeType type)
	{
		switch(type)
		{
			case NORMAL: default: return NORMAL;
			case ROUGH: return ROUGH;
			case OCEAN: return OCEAN;
		}
	}
	
	public LandBiomeType getTypeFromBiome(Holder<Biome> biome)
	{
		if(biome == NORMAL)
			return LandBiomeType.NORMAL;
		else if(biome == ROUGH)
			return LandBiomeType.ROUGH;
		else if(biome == OCEAN)
			return LandBiomeType.OCEAN;
		else
			return LandBiomeType.NORMAL;
	}
	
}
