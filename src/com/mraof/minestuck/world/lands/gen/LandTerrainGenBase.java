package com.mraof.minestuck.world.lands.gen;

import net.minecraft.world.chunk.ChunkPrimer;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public abstract class LandTerrainGenBase
{
	
	protected ChunkProviderLands provider;
	
	public LandTerrainGenBase(ChunkProviderLands chunkProvider)
	{
		this.provider = chunkProvider;
	}
	
	public abstract ChunkPrimer createChunk(int chunkX, int chunkZ);
	
}
