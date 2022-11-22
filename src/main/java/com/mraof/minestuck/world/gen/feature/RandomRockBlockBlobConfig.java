package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class RandomRockBlockBlobConfig implements FeatureConfiguration
{
	public static Codec<RandomRockBlockBlobConfig> CODEC = Codec.INT.xmap(RandomRockBlockBlobConfig::new, config -> config.startRadius);
	
	public final int startRadius;
	
	public RandomRockBlockBlobConfig(int startRadius)
	{
		this.startRadius = startRadius;
	}
	
}
