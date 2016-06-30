package com.mraof.minestuck.world.lands.title;

import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.world.lands.decorator.RockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;

public class LandAspectWind extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "wind";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"wind"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.weatherType == -1)
			chunkProvider.weatherType = 0;
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new RockDecorator());
			if(chunkProvider.terrainGenerator instanceof DefaultTerrainGen)
			{
				DefaultTerrainGen terrainGen = (DefaultTerrainGen) chunkProvider.terrainGenerator;
				terrainGen.normalVariation *= 0.6F;
				terrainGen.roughtVariation *= 0.6F;
				terrainGen.roughHeight = (terrainGen.roughHeight + terrainGen.normalHeight)/2;
			}
		}
		
		chunkProvider.mergeFogColor(new Vec3d(0.1, 0.2, 0.8), 0.3F);
	}
	
}
