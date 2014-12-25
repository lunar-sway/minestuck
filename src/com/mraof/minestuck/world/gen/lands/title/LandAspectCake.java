package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.decorator.CakeDecorator;

public class LandAspectCake extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Cake";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"Cake", "Desserts"};
	}
	
	@Override
	public float getRarity() {
		return 0.5F;
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.decorators.add(new CakeDecorator());
		
	}
	
}