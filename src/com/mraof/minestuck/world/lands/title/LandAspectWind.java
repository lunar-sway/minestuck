package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.decorator.RockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectWind extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "wind";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"wind"};
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.1, 0.2, 0.8), 0.3F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.weatherType == -1)
			chunkProvider.weatherType = 0;
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.LIGHT_BLUE_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CYAN_CARPET.getDefaultState());
		chunkProvider.decorators.add(new RockDecorator());
	}
}
