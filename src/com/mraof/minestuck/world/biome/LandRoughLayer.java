package com.mraof.minestuck.world.biome;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;

public class LandRoughLayer implements IC1Transformer
{
	private final int roughChance;
	private final int NORMAL_BIOME = Registry.BIOME.getId(MSBiomes.LAND_NORMAL);	//TODO Is there a better way to do this?
	private final int ROUGH_BIOME = Registry.BIOME.getId(MSBiomes.LAND_ROUGH);
	
	public LandRoughLayer(float roughChance)
	{
		this.roughChance = (int) (Integer.MAX_VALUE * MathHelper.clamp(roughChance, 0, 1));
	}
	
	@Override
	public int apply(INoiseRandom context, int value)
	{
		if(value == NORMAL_BIOME && context.random(Integer.MAX_VALUE) < roughChance)
			return ROUGH_BIOME;
		else return value;
	}
}