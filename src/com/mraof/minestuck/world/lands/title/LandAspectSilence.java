package com.mraof.minestuck.world.lands.title;

import net.minecraft.util.Vec3;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

public class LandAspectSilence extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Silence";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"silence"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 2;
		
		chunkProvider.mergeFogColor(new Vec3(0, 0, 0.1), 0.5F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 1) != 0)/*rain is noisy*/ && aspect.getDayCycleMode() != 1;
	}
	
}
