package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.decorator.BucketDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectBuckets extends TitleAspect	//Yes, buckets
{

	@Override
	public String getPrimaryName()
	{
		return "Buckets";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"bucket"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new BucketDecorator());
			chunkProvider.sortDecorators();
		}
		
	}
	
}
