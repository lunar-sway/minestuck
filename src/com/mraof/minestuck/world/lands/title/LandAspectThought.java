package com.mraof.minestuck.world.lands.title;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

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
		return new String[]{"thought"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		
		if(chunkProvider.surfaceBlock.getBlock().getMaterial() == Material.ground)
		{
			chunkProvider.surfaceBlock = Minestuck.coloredDirt.getDefaultState().withProperty(BlockColoredDirt.BLOCK_TYPE, BlockColoredDirt.BlockType.THOUGHT);
			if(chunkProvider.decorators != null)
			{
				ArrayList<ILandDecorator> decoratorsToRemove = new ArrayList<ILandDecorator>();
				for(ILandDecorator decorator : chunkProvider.decorators)
					if(decorator instanceof SurfaceDecoratorVein)
					{
						IBlockState blockState = ((SurfaceDecoratorVein)decorator).block;
						if(blockState.getBlock().getMaterial() == Material.ground)
							decoratorsToRemove.add(decorator);
					}
				if(!decoratorsToRemove.isEmpty())
					chunkProvider.decorators.removeAll(decoratorsToRemove);
			}
		}
		chunkProvider.riverBlock = Minestuck.blockBrainJuice.getDefaultState();
		chunkProvider.oceanBlock = Minestuck.blockBrainJuice.getDefaultState() ;
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getOceanBlock().getBlock().getMaterial() != Material.lava;
	}
	
}
