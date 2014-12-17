package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public abstract class SecondaryAspect
{
	//This way, the aspect could change individual parameters on the chunk provider, but also create an own instance if necessary.
	
	public abstract String getPrimaryName();
	
	public abstract String[] getNames();
	
	public abstract float getRarity();
	
	protected abstract void prepareChunkProvider(ChunkProviderLands chunkProvider);
	
	public ChunkProviderLands createChunkProvider(WorldProviderLands land)
	{
		ChunkProviderLands chunkProvider = new ChunkProviderLands(land.getWorld(), land, land.getWorld().isRemote ? Minestuck.worldSeed : land.getSeed());
		prepareChunkProvider(chunkProvider);
		return chunkProvider;
	}
	
}
