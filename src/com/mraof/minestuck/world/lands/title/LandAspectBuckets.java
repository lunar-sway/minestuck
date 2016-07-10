package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.structure.BucketDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectBuckets extends TitleLandAspect	//Yes, buckets
{

	@Override
	public String getPrimaryName()
	{
		return "buckets";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"bucket"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	protected void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.decorators.add(new BucketDecorator(BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		chunkProvider.sortDecorators();
	}
}