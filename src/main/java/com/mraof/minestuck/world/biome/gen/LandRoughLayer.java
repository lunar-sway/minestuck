package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.biome.MSBiomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class LandRoughLayer implements IC1Transformer
{
	private final int roughChance;
	private final int NORMAL_BIOME = ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(MSBiomes.LAND_NORMAL);	//TODO Is there a better way to do this?
	private final int ROUGH_BIOME = ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(MSBiomes.LAND_ROUGH);
	
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