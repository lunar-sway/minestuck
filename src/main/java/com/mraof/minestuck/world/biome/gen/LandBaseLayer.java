package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.biome.MSBiomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class LandBaseLayer implements IAreaTransformer0
{
	private final int oceanChance;
	private final int NORMAL_BIOME = ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(MSBiomes.LAND_NORMAL);	//TODO Is there a better way to do this?
	private final int OCEAN_BIOME = ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(MSBiomes.LAND_OCEAN);
	
	public LandBaseLayer(float oceanChance)
	{
		this.oceanChance = (int) (Integer.MAX_VALUE * MathHelper.clamp(oceanChance, 0, 1));
	}
	
	@Override
	public int apply(INoiseRandom random, int x, int z)
	{
		if(random.random(Integer.MAX_VALUE) >= oceanChance)
			return NORMAL_BIOME;
		return OCEAN_BIOME;
	}
}