package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.decorator.structure.BucketDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;

public class BucketsLandAspect extends TitleLandAspect	//Yes, buckets
{
	public BucketsLandAspect()
	{
		super(EnumAspect.SPACE);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"bucket"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.BLUE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.BLACK_CARPET.getDefaultState());
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.decorators.add(new BucketDecorator(MSBiomes.mediumNormal, MSBiomes.mediumRough));
		//chunkProvider.sortDecorators();
	}
}