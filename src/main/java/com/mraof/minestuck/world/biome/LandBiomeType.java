package com.mraof.minestuck.world.biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LandBiomeType
{
	NORMAL, ROUGH, OCEAN;
	
	public static LandBiomeType[] any()
	{
		return values();
	}
	
	public static LandBiomeType[] anyExcept(LandBiomeType... excepted)
	{
		List<LandBiomeType> types = new ArrayList<>(Arrays.asList(LandBiomeType.values()));
		types.removeAll(Arrays.asList(excepted));
		return types.toArray(new LandBiomeType[0]);
	}
}