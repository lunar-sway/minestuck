package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerLandRough extends GenLayer
{
	
	GenLayer parent;
	
	public GenLayerLandRough(long seed, GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		//Standard generation
		/*int[] biomeGen = parent.getInts(areaX, areaY, areaWidth, areaHeight);
		
		for(int i = 0; i < biomeGen.length; i++)
		{
			initChunkSeed(areaX + i%areaWidth, areaY + i/areaWidth);
			if(biomeGen[i] == BiomeGenBase.getIdForBiome(BiomeGenMinestuck.mediumNormal) && nextInt(10) == 0)
				biomeGen[i] = BiomeGenBase.getIdForBiome(BiomeGenMinestuck.mediumRough);
		}*/
		
		//No generation by ocean
		int oceanId = BiomeGenBase.getIdForBiome(BiomeGenMinestuck.mediumOcean);
		int width = areaWidth + 2, height = areaHeight + 2, x = areaX - 1, y = areaY - 1;
		
		int[] parentGen = parent.getInts(x, y, width, height);
		int[] biomeGen = IntCache.getIntCache(areaWidth * areaHeight);
		
		for(int xCor = 0; xCor < areaWidth; xCor++)
			for(int yCor = 0; yCor < areaHeight; yCor++)
			{
				initChunkSeed(areaX + xCor, areaY + yCor);
				int biomeIndex = xCor + yCor*areaWidth;
				int parentIndex = (xCor + 1) + (yCor + 1)*width;
				if(parentGen[parentIndex] == BiomeGenBase.getIdForBiome(BiomeGenMinestuck.mediumNormal) && nextInt(5) == 0
						&& parentGen[parentIndex + 1] != oceanId && parentGen[parentIndex - 1] != oceanId
						&& parentGen[parentIndex + width] != oceanId && parentGen[parentIndex - width] != oceanId)
					biomeGen[biomeIndex] = BiomeGenBase.getIdForBiome(BiomeGenMinestuck.mediumRough);
				else biomeGen[biomeIndex] = parentGen[parentIndex];
			}
		
		return biomeGen;
	}
	
}