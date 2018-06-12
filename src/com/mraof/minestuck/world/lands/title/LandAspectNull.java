package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectNull extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "null";
	}

	@Override
	public String[] getNames()
	{
		return new String[] {"Null"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	protected void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
	}
	
}