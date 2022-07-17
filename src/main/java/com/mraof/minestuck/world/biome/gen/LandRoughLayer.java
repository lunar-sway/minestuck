package com.mraof.minestuck.world.biome.gen;

/*TODO
public class LandRoughLayer implements IC1Transformer
{
	private final int roughChance;
	private final int NORMAL_BIOME = LandBiomeType.NORMAL.ordinal();
	private final int ROUGH_BIOME = LandBiomeType.ROUGH.ordinal();
	
	public LandRoughLayer(float roughChance)
	{
		this.roughChance = (int) (Integer.MAX_VALUE * Mth.clamp(roughChance, 0, 1));
	}
	
	@Override
	public int apply(INoiseRandom context, int value)
	{
		if(value == NORMAL_BIOME && context.nextRandom(Integer.MAX_VALUE) < roughChance)
			return ROUGH_BIOME;
		else return value;
	}
}*/