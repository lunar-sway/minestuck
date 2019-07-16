package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.Layer;

public class GenLayerLandRough //extends GenLayer
{
	
	int roughChance;
	Layer parent;
	
	/*public GenLayerLandRough(long seed, GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	public void setRoughChance(float roughChance)
	{
		this.roughChance = (int) (roughChance*Integer.MAX_VALUE);
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		//Standard generation
		int[] biomeGen = parent.getInts(areaX, areaY, areaWidth, areaHeight);
		
		for(int i = 0; i < biomeGen.length; i++)
		{
			initChunkSeed(areaX + i%areaWidth, areaY + i/areaWidth);
			if(biomeGen[i] == Biome.getIdForBiome(BiomeMinestuck.mediumNormal) && nextInt(Integer.MAX_VALUE) < roughChance)
				biomeGen[i] = Biome.getIdForBiome(BiomeMinestuck.mediumRough);
		}
		
		//No generation by ocean
		/*int oceanId = Biome.getIdForBiome(BiomeMinestuck.mediumOcean);
		int width = areaWidth + 2, height = areaHeight + 2, x = areaX - 1, y = areaY - 1;
		
		int[] parentGen = parent.getInts(x, y, width, height);
		int[] biomeGen = IntCache.getIntCache(areaWidth * areaHeight);
		
		for(int xCor = 0; xCor < areaWidth; xCor++)
			for(int yCor = 0; yCor < areaHeight; yCor++)
			{
				initChunkSeed(areaX + xCor, areaY + yCor);
				int biomeIndex = xCor + yCor*areaWidth;
				int parentIndex = (xCor + 1) + (yCor + 1)*width;
				if(parentGen[parentIndex] == Biome.getIdForBiome(BiomeMinestuck.mediumNormal) && nextInt(Integer.MAX_VALUE) < roughChance
						&& parentGen[parentIndex + 1] != oceanId && parentGen[parentIndex - 1] != oceanId
						&& parentGen[parentIndex + width] != oceanId && parentGen[parentIndex - width] != oceanId)
					biomeGen[biomeIndex] = Biome.getIdForBiome(BiomeMinestuck.mediumRough);
				else biomeGen[biomeIndex] = parentGen[parentIndex];
			}*//*
		
		return biomeGen;
	}*/
	
}