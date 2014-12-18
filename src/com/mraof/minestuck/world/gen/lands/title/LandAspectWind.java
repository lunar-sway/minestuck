package com.mraof.minestuck.world.gen.lands.title;

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
		return new String[] {"Breath", "Wind"};
	}
	
	@Override
	public float getRarity()
	{
		return 0.5F;
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		
	}
	
}
