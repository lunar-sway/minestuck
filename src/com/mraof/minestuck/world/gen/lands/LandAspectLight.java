package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectLight extends SecondaryAspect
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
	public float getRarity()
	{
		return 0.5F;
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 1;
	}
	
}