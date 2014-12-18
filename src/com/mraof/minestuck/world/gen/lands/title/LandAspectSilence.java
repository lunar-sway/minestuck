package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

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
		return new String[]{"Silence"};
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
