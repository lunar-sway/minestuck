package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;

import com.mraof.minestuck.world.lands.decorator.RabbitSpawner;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectRabbits extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "rabbits";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rabbit", "bunny"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new RabbitSpawner());
			chunkProvider.sortDecorators();
			
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return !aspect.getOceanBlock().getMaterial().equals(Material.lava);
	}
	
}
