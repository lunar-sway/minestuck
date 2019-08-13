package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import net.minecraft.world.biome.Biome;

public class LandBiomeHolder
{
	public Biome.RainType rainType = Biome.RainType.NONE;
	public float temperature = 0.5F, downfall = 0.5F;
	public float normalBiomeDepth = ModBiomes.LAND_NORMAL.getDepth(), normalBiomeScale = ModBiomes.LAND_NORMAL.getScale();
	public float roughBiomeDepth = ModBiomes.LAND_ROUGH.getDepth(), roughBiomeScale = ModBiomes.LAND_ROUGH.getScale();
	public float oceanBiomeDepth = ModBiomes.LAND_OCEAN.getDepth(), oceanBiomeScale = ModBiomes.LAND_OCEAN.getScale();
	
	private final LandAspects landAspects;
	private final LandWrapperBiome normalBiome, oceanBiome, roughBiome;
	
	public LandBiomeHolder(LandAspects landAspects)
	{
		this.landAspects = landAspects;
		this.landAspects.aspectTerrain.setBiomeSettings(this);
		this.landAspects.aspectTitle.setBiomeSettings(this);
		
		normalBiome = ModBiomes.LAND_NORMAL.createWrapper(this);
		roughBiome = ModBiomes.LAND_ROUGH.createWrapper(this);
		oceanBiome = ModBiomes.LAND_OCEAN.createWrapper(this);
	}
	
	public void initBiomesWith(LandGenSettings settings)
	{
		LandWrapperBiome[] biomes = {normalBiome, roughBiome, oceanBiome};
		for(LandWrapperBiome biome : biomes)
		{
			biome.init(settings);
			landAspects.aspectTerrain.setBiomeGenSettings(biome, settings.getBlockRegistry());
			landAspects.aspectTitle.setBiomeGenSettings(biome, settings.getBlockRegistry());
		}
	}
	
	public LandWrapperBiome localBiomeFrom(Biome biome) {
		if(biome == ModBiomes.LAND_NORMAL)
			return normalBiome;
		if(biome == ModBiomes.LAND_ROUGH)
			return roughBiome;
		if(biome == ModBiomes.LAND_OCEAN)
			return oceanBiome;
		
		return normalBiome;
	}
}