package com.mraof.minestuck.world.lands.gen;

import java.util.Random;

public class RiverFreeTerrainGen extends DefaultTerrainGen
{
	
	public RiverFreeTerrainGen(ChunkProviderLands chunkProvider, Random rand)
	{
		super(chunkProvider, rand);
	}
	
	@Override
	public int[] getRiverHeightMap(int chunkX, int chunkZ)
	{
		return new int[256];
	}
	
}
