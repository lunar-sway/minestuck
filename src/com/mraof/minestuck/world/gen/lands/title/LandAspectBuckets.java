package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.decorator.BucketDecorator;

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
		return new String[]{"Buckets"};
	}

	@Override
	public float getRarity()
	{
		return 0.1F;
	}

	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		
		chunkProvider.decorators.add(new BucketDecorator());
		chunkProvider.sortDecorators();
		
	}
	
}
