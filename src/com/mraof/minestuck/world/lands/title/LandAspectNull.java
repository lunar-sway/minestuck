package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectNull extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Null";
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

}
