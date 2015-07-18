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
		chunkProvider.weatherType = 6;
		
		chunkProvider.mergeFogColor(new Vec3(0.1, 0.1, 0.2), 0.5F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getWeatherType() == -1 || ((aspect.getWeatherType() & 1) == 0);
	}
	
}
