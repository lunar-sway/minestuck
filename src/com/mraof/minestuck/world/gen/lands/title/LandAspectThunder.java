package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;

public class LandAspectThunder extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Thunder";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"Thunder", "Lightning", "Storms"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.weatherType = 6;
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getWeatherType() == -1 || ((aspect.getWeatherType() & 1) == 0);
	}
	
}
