package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectWind extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Wind";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"wind"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.weatherType == -1)
			chunkProvider.weatherType = 0;
	}
	
}
