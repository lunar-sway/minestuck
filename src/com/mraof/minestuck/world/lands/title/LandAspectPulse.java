package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectPulse extends TitleLandAspect
{

	@Override
	public String getPrimaryName()
	{
		return "pulse";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		if(chunkProvider.decorators != null)
		{
			chunkProvider.oceanBlock = MinestuckBlocks.blockBlood.getDefaultState();
			chunkProvider.riverBlock = MinestuckBlocks.blockBlood.getDefaultState();
			
		}
		
		chunkProvider.mergeFogColor(new Vec3d(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getOceanBlock().getMaterial() != Material.LAVA;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
}