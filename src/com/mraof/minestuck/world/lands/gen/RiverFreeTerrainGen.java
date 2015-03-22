package com.mraof.minestuck.world.lands.gen;

import java.util.Random;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class RiverFreeTerrainGen extends DefaultTerrainGen
{
	
	public RiverFreeTerrainGen(ChunkProviderLands chunkProvider, Random rand)
	{
		super(chunkProvider, rand);
	}
	
	@Override
	protected int[] getRiverHeightMap(int chunkX, int chunkZ)
	{
		return new int[256];
	}
	
}
