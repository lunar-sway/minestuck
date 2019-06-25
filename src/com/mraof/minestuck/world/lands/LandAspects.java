package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

public class LandAspects
{
	public LandAspects(TerrainLandAspect terrainAspect, TitleLandAspect titleAspect)
	{
		if(terrainAspect == null || titleAspect == null)
			throw new IllegalArgumentException("Parameters may not be null");
		this.aspectTerrain = terrainAspect;
		this.aspectTitle = titleAspect;
	}
	
	public final TerrainLandAspect aspectTerrain;
	/**
	 * Not to be confused with EnumAspect.
	 */
	public final TitleLandAspect aspectTitle;
}