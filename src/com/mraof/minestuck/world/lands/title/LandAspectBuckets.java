package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.structure.BucketDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.Blocks;

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
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.BLUE_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.BLACK_CARPET.getDefaultState());
		chunkProvider.decorators.add(new BucketDecorator(BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		//chunkProvider.sortDecorators();
	}
}