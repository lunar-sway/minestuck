package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.decorator.BasicTowerDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectTowers extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Towers";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"tower"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new BasicTowerDecorator());
			chunkProvider.sortDecorators();
		}
	}
	
}