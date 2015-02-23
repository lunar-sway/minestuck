package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;

public class LandAspectFear extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Fear";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"Fear"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 2;
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getDayCycleMode() != 1;
	}
	
}
