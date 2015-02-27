package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;

public class LandAspectSilence extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Silence";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"silence"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 2;
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 1) != 0)/*rain is noisy*/ && aspect.getDayCycleMode() != 1;
	}
	
}
