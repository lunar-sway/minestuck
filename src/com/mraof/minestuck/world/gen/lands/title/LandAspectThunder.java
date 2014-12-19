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
	public float getRarity()
	{
		return 0.5F;
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.weatherType == -1)
			chunkProvider.weatherType = 6;
		else chunkProvider.weatherType |= 6;
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return !aspect.getPrimaryName().equals("Rain");	//Enough with one aspect focused on keeping it rainy
	}
	
}
