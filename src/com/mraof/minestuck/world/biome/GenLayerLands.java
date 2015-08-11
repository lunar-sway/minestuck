package com.mraof.minestuck.world.biome;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerLands extends GenLayer
{
	public GenLayerLands(long seed)
	{
		super(seed);
	}
	
	@Override
	public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight)
	{
		int[] biomeGen = IntCache.getIntCache(areaWidth * areaHeight);
		
		for(int x = 0; x < areaWidth; x++)
			for(int z = 0; z < areaHeight; z++)
			{
				initChunkSeed(areaX + x, areaZ + z);
				biomeGen[x + z * areaHeight] = BiomeGenMinestuck.mediumNormal.biomeID + nextInt(2);
			}
		return biomeGen;
	}
	
	public static GenLayer[] generateBiomeGenLayers(long seed)
	{
		GenLayer layer = new GenLayerLands(413L);
		
		GenLayerVoronoiZoom voronoiZoom = new GenLayerVoronoiZoom(10L, layer);
		layer.initWorldGenSeed(seed);
		voronoiZoom.initWorldGenSeed(seed);
		
		return new GenLayer[] {layer, voronoiZoom};
	}
}