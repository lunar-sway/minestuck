package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectThought extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "thought";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thought"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		if(chunkProvider.decorators != null)
		{
			
		}
		chunkProvider.riverBlock = MinestuckBlocks.blockBrainJuice.getDefaultState();
		chunkProvider.oceanBlock = MinestuckBlocks.blockBrainJuice.getDefaultState();
		
		chunkProvider.mergeFogColor(new Vec3d(0.8, 0.3, 0.8), 0.8F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getOceanBlock().getMaterial() != Material.lava;
	}
	
}
