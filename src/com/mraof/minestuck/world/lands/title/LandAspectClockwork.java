package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.decorator.CogDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

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
		return new String[]{"clockwork", "gear"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new CogDecorator());
			chunkProvider.sortDecorators();
		}
		
	}
	
}
