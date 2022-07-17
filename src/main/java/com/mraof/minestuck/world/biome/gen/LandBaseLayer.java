package com.mraof.minestuck.world.biome.gen;
/*
import com.mraof.minestuck.world.biome.LandBiomeType;
import net.minecraft.util.Mth;

public class LandBaseLayer implements IAreaTransformer0
{
	private final int oceanChance;
	private final int NORMAL_BIOME = LandBiomeType.NORMAL.ordinal();
	private final int OCEAN_BIOME = LandBiomeType.OCEAN.ordinal();
	
	public LandBaseLayer(float oceanChance)
	{
		this.oceanChance = (int) (Integer.MAX_VALUE * Mth.clamp(oceanChance, 0, 1));
	}
	
	@Override
	public int applyPixel(INoiseRandom random, int x, int z)
	{
		if(random.nextRandom(Integer.MAX_VALUE) >= oceanChance)
			return NORMAL_BIOME;
		return OCEAN_BIOME;
	}
}*/