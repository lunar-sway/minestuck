package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectNull extends SecondaryAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Null";
	}

	@Override
	public String[] getNames()
	{
		return new String[] {"Null", "Undefined"};
	}

	@Override
	public float getRarity()
	{
		return 0;
	}

	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		
		
	}

}
