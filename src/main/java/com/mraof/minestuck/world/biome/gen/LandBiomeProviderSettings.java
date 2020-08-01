package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;

public class LandBiomeProviderSettings implements IBiomeProviderSettings
{
	private long seed;
	private WorldType worldType;
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

	public WorldType getWorldType() {
		return this.worldType;
	}
	
	public LandBiomeProviderSettings setGenSettings(LandGenSettings genSettings)
	{
		this.genSettings = genSettings;
		this.worldType = WorldType.DEFAULT;
		return this;
	}
}
