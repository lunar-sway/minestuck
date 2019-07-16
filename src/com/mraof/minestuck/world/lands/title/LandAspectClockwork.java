package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.decorator.structure.CogDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectClockwork extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "clockwork";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"clockwork", "gear"};
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.5, 0.5, 0.5), 0.5F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.RED_CARPET.getDefaultState());
		chunkProvider.decorators.add(new CogDecorator());
		//chunkProvider.sortDecorators();
	}
	
}
