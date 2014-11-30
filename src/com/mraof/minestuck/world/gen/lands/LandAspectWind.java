package com.mraof.minestuck.world.gen.lands;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectWind extends SecondaryAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Wind";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"Breath", "Wind", "Zephyr", "Breeze"};
	}
	
	@Override
	public float getRarity()
	{
		return 0.5F;
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		// TODO Auto-generated method stub
	}
	
}
