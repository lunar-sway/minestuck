package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.decorator.RabbitSpawner;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectRabbits extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Rabbits";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rabbit", "bunny"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new RabbitSpawner());
			chunkProvider.sortDecorators();
		}
	}
	
}
