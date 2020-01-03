package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.world.biome.Biome;

public class LandBiomeHolder
{
	private final LandTypePair landTypes;
	private final LandWrapperBiome normalBiome, oceanBiome, roughBiome;
	
	public LandBiomeHolder(LandProperties properties, LandTypePair landTypes)
	{
		this.landTypes = landTypes;
		
		normalBiome = MSBiomes.LAND_NORMAL.createWrapper(properties);
		roughBiome = MSBiomes.LAND_ROUGH.createWrapper(properties);
		oceanBiome = MSBiomes.LAND_OCEAN.createWrapper(properties);
	}
	
	public void initBiomesWith(LandGenSettings settings)
	{
		for(LandWrapperBiome biome : getBiomes())
		{
			biome.init(settings);
			landTypes.terrain.setBiomeSettings(biome, settings.getBlockRegistry());
			landTypes.title.setBiomeSettings(biome, settings.getBlockRegistry());
		}
	}
	
	public LandWrapperBiome localBiomeFrom(Biome biome)
	{
		for(LandWrapperBiome wrapperBiome : getBiomes())
		{
			if(wrapperBiome.staticBiome == biome)
				return wrapperBiome;
		}
		
		return normalBiome;
	}
	
	public LandWrapperBiome[] getBiomes()
	{
		return new LandWrapperBiome[]{normalBiome, roughBiome, oceanBiome};
	}
}