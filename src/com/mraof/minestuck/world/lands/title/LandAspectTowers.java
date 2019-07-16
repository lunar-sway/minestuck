package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.decorator.structure.BasicTowerDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.Blocks;

public class LandAspectTowers extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "towers";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"tower"};
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.LIGHT_BLUE_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.YELLOW_CARPET.getDefaultState());
		chunkProvider.decorators.add(new BasicTowerDecorator());
		chunkProvider.decorators.add(new PillarDecorator("structure_primary", 1, true, BiomeMinestuck.mediumRough));
		//chunkProvider.sortDecorators();
	}
	
}