package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;

public class LandBiomeProviderSettings implements IBiomeProviderSettings
{
	private long seed;
	private LandGenSettings genSettings;
	
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
}
