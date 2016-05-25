package com.mraof.minestuck.world.lands.title;

import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectThunder extends TitleLandAspect
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
		chunkProvider.rainfall += 0.1F;
		chunkProvider.oceanChance = Math.min(Math.max(0.5F, chunkProvider.oceanChance), chunkProvider.oceanChance*1.2F);
		
		chunkProvider.mergeFogColor(new Vec3d(0.1, 0.1, 0.2), 0.5F);
		
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getTemperature() >= 0.2;
	}
	
}
