package com.mraof.minestuck.world.gen.lands.title;

import net.minecraft.block.material.Material;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectThought extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Thought";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"Thought"};
	}
	
	@Override
	public float getRarity()
	{
		return 0.5F;
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.surfaceBlock.getBlock().getMaterial() == Material.ground)
		{
			chunkProvider.surfaceBlock = Minestuck.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, BlockColoredDirt.BlockType.THOUGHT);
		}
		else
		{
//			chunkProvider.riverBlock = Minestuck.blockBrainJuice;
//			chunkProvider.oceanBlock = Minestuck.blockBrainJuice;
		}
	}
	
}
