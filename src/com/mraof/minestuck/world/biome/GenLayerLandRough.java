package com.mraof.minestuck.world.biome;

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
			if(biomeGen[i] == BiomeGenMinestuck.mediumNormal.biomeID && nextInt(10) == 0)
				biomeGen[i] = BiomeGenMinestuck.mediumRough.biomeID;
		}*/
		
		//No generation by ocean
		int width = areaWidth + 2, height = areaHeight + 2, x = areaX - 1, y = areaY - 1;
		
		int[] parentGen = parent.getInts(x, y, width, height);
		
		for(int xCor = 1; xCor < width - 1; xCor++)
			for(int yCor = 1; yCor < height - 1; yCor++)
			{
				initChunkSeed(x + xCor, y + yCor);
				int i = xCor + yCor*width;
				if(parentGen[i] == BiomeGenMinestuck.mediumNormal.biomeID && nextInt(5) == 0
						&& parentGen[i + 1] != BiomeGenMinestuck.mediumOcean.biomeID && parentGen[i - 1] != BiomeGenMinestuck.mediumOcean.biomeID
						&& parentGen[i + width] != BiomeGenMinestuck.mediumOcean.biomeID && parentGen[i - width] != BiomeGenMinestuck.mediumOcean.biomeID)
					parentGen[i] = BiomeGenMinestuck.mediumRough.biomeID;
			}
		
		int[] biomeGen = IntCache.getIntCache(areaWidth * areaHeight);
		
		for(int yCor = 0; yCor < areaWidth; yCor++)
			System.arraycopy(parentGen, 1 + yCor*width, biomeGen, yCor*areaHeight, areaHeight);
		
		return biomeGen;
	}
	
}