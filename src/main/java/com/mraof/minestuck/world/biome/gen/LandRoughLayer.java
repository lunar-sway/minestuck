package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.biome.BiomeType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;

public class LandRoughLayer implements IC1Transformer
{
	private final int roughChance;
	private final int NORMAL_BIOME = BiomeType.NORMAL.ordinal();
	private final int ROUGH_BIOME = BiomeType.ROUGH.ordinal();
	
	public LandRoughLayer(float roughChance)
	{
		this.roughChance = (int) (Integer.MAX_VALUE * MathHelper.clamp(roughChance, 0, 1));
	}
	
	@Override
	public int apply(INoiseRandom context, int value)
	{
		if(value == NORMAL_BIOME && context.nextRandom(Integer.MAX_VALUE) < roughChance)
			return ROUGH_BIOME;
		else return value;
	}
}