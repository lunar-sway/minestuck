package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import net.minecraft.world.biome.Biome;

public class LandBiomeHolder
{
	public Biome.Category category = Biome.Category.NONE;
	public Biome.RainType rainType = Biome.RainType.NONE;
	public float temperature = 0.7F, downfall = 0.5F;
	public float normalBiomeDepth = MSBiomes.LAND_NORMAL.getDepth(), normalBiomeScale = MSBiomes.LAND_NORMAL.getScale();
	public float roughBiomeDepth = MSBiomes.LAND_ROUGH.getDepth(), roughBiomeScale = MSBiomes.LAND_ROUGH.getScale();
	public float oceanBiomeDepth = MSBiomes.LAND_OCEAN.getDepth(), oceanBiomeScale = MSBiomes.LAND_OCEAN.getScale();
	
	private final LandAspects landAspects;
	private final LandWrapperBiome normalBiome, oceanBiome, roughBiome;
	
	public LandBiomeHolder(LandAspects landAspects, boolean isFake)
	{
		this.landAspects = landAspects;
		this.landAspects.terrain.setBiomeSettings(this);
		this.landAspects.title.setBiomeSettings(this);
		
		if(!isFake)
		{
			normalBiome = MSBiomes.LAND_NORMAL.createWrapper(this);
			roughBiome = MSBiomes.LAND_ROUGH.createWrapper(this);
			oceanBiome = MSBiomes.LAND_OCEAN.createWrapper(this);
		} else normalBiome = roughBiome = oceanBiome = null;
	}
	
	public void initBiomesWith(LandGenSettings settings)
	{
		LandWrapperBiome[] biomes = {normalBiome, roughBiome, oceanBiome};
		for(LandWrapperBiome biome : biomes)
		{
			biome.init(settings);
			landAspects.terrain.setBiomeGenSettings(biome, settings.getBlockRegistry());
			landAspects.title.setBiomeGenSettings(biome, settings.getBlockRegistry());
		}
	}
	
	public LandWrapperBiome localBiomeFrom(Biome biome) {
		LandWrapperBiome[] biomes = {normalBiome, roughBiome, oceanBiome};
		for(LandWrapperBiome wrapperBiome : biomes)
		{
			if(wrapperBiome.staticBiome == biome)
				return wrapperBiome;
		}
		
		return normalBiome;
	}
}