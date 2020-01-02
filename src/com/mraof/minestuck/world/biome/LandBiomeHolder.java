package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.biome.Biome;

public class LandBiomeHolder
{
	public Biome.Category category = Biome.Category.NONE;
	public Biome.RainType rainType = Biome.RainType.NONE;
	public float temperature = 0.7F, downfall = 0.5F;
	public float normalBiomeDepth = MSBiomes.LAND_NORMAL.getDepth(), normalBiomeScale = MSBiomes.LAND_NORMAL.getScale();
	public float roughBiomeDepth = MSBiomes.LAND_ROUGH.getDepth(), roughBiomeScale = MSBiomes.LAND_ROUGH.getScale();
	public float oceanBiomeDepth = MSBiomes.LAND_OCEAN.getDepth(), oceanBiomeScale = MSBiomes.LAND_OCEAN.getScale();
	
	private final LandTypePair landTypes;
	private final LandWrapperBiome normalBiome, oceanBiome, roughBiome;
	
	public LandBiomeHolder(LandTypePair landTypes, boolean isFake)
	{
		this.landTypes = landTypes;
		this.landTypes.terrain.setBiomeSettings(this);
		this.landTypes.title.setBiomeSettings(this);
		
		if(!isFake)
		{
			normalBiome = MSBiomes.LAND_NORMAL.createWrapper(this);
			roughBiome = MSBiomes.LAND_ROUGH.createWrapper(this);
			oceanBiome = MSBiomes.LAND_OCEAN.createWrapper(this);
		} else normalBiome = roughBiome = oceanBiome = null;
	}
	
	public void initBiomesWith(LandGenSettings settings)
	{
		for(LandWrapperBiome biome : getBiomes())
		{
			biome.init(settings);
			landTypes.terrain.setBiomeGenSettings(biome, settings.getBlockRegistry());
			landTypes.title.setBiomeGenSettings(biome, settings.getBlockRegistry());
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