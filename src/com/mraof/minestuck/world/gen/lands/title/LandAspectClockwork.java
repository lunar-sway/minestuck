package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectClockwork extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Clockwork";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"Clockwork", "Gears"};
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
