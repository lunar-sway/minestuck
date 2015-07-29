package com.mraof.minestuck.world.lands.title;

import net.minecraft.util.Vec3;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

public class LandAspectThunder extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Thunder";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thunder", "lightning", "storm"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.weatherType = 4;
		chunkProvider.moisture += 0.1F;
		
		chunkProvider.mergeFogColor(new Vec3(0.1, 0.1, 0.2), 0.5F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getTemperature() >= 0.2;
	}
	
}
