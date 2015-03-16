package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.decorator.CogDecorator;

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
