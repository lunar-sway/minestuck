package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.world.biome.Biome;

public class LandBiomeHolder
{
	private final LandTypePair landTypes;
	private final LandWrapperBiome normalBiome, oceanBiome, roughBiome;
	
	public LandBiomeHolder(LandProperties properties, LandTypePair landTypes)
	{
		this.landTypes = landTypes;
		
		normalBiome = properties.biomes.NORMAL.get().createWrapper(properties);
		roughBiome = properties.biomes.ROUGH.get().createWrapper(properties);
		oceanBiome = properties.biomes.OCEAN.get().createWrapper(properties);
	}
	
	public void initBiomesWith(StructureBlockRegistry blocks)
	{
		for(LandWrapperBiome biome : getBiomes())
		{
			biome.init(blocks, landTypes.terrain.getConsortType());
			landTypes.terrain.setBiomeSettings(biome, blocks);
			landTypes.title.setBiomeSettings(biome, blocks);
		}
	}
	
	public LandWrapperBiome localBiomeFrom(Biome biome)
	{
		for(LandWrapperBiome wrapperBiome : getBiomes())
		{
			if(wrapperBiome.staticBiome == biome)
				return wrapperBiome;
		}
		
		return normalBiome;
	}
	
	public LandWrapperBiome[] getBiomes()
	{
		return new LandWrapperBiome[]{normalBiome, roughBiome, oceanBiome};
	}
}