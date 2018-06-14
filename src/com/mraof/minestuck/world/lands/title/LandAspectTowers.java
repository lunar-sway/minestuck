package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.decorator.structure.BasicTowerDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

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
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	protected void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW));
		chunkProvider.decorators.add(new BasicTowerDecorator());
		chunkProvider.decorators.add(new PillarDecorator("structure_primary", 1, true, BiomeMinestuck.mediumRough));
		chunkProvider.sortDecorators();
	}
	
}