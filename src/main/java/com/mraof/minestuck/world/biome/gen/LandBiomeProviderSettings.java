package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

public class LandBiomeProviderSettings implements IBiomeProviderSettings
{
	private long seed;
	private LandGenSettings genSettings;

	public LandBiomeProviderSettings(WorldInfo worldInfo)
	{
	}

	public long getSeed()
	{
		return seed;
	}
	
	public LandBiomeProviderSettings setSeed(long seed)
	{
		this.seed = seed;
		return this;
	}
	
	public LandGenSettings getGenSettings()
	{
		return genSettings;
	}
	
	public LandBiomeProviderSettings setGenSettings(LandGenSettings genSettings)
	{
		this.genSettings = genSettings;
		return this;
	}
	
	public LandBiomeSet getBiomes()
	{
		return genSettings.getLandTypes().terrain.getBiomeSet();
	}
}
