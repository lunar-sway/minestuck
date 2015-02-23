package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;

public class LandAspectLight extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Light";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"Light", "Brightness"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 1;
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getDayCycleMode() != 2 && (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 2) == 0);
	}
	
}