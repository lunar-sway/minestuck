package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.util.Debug;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerLands extends GenLayer
{
	private int oceanChance;
	
	public GenLayerLands(long seed)
	{
		super(seed);
	}
	
	public void setOceanChance(float oceanChance)
	{
		this.oceanChance = (int) (Integer.MAX_VALUE*oceanChance);
		Debug.debugf("Ocean chance: %f. Computed to %d out of %d.", oceanChance, this.oceanChance, Integer.MAX_VALUE);
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] biomeGen = IntCache.getIntCache(areaWidth * areaHeight);
		
		for(int x = 0; x < areaWidth; x++)
			for(int y = 0; y < areaHeight; y++)
			{
				initChunkSeed(areaX + x, areaY + y);
				biomeGen[x + y*areaWidth] = BiomeGenBase.getIdForBiome(BiomeGenMinestuck.mediumNormal) + (nextInt(Integer.MAX_VALUE) >= oceanChance ? 0 : 1);
			}
		return biomeGen;
	}
	
	public static GenLayer[] generateBiomeGenLayers(long seed)
	{
		GenLayer layerLands = new GenLayerLands(413L);
		GenLayer layer = new GenLayerZoom(1000L, layerLands);
		layer = new GenLayerLandRough(414L, layerLands);
		layer = new GenLayerZoom(1001L, layer);
		layer = new GenLayerZoom(1002L, layer);
		layer = new GenLayerZoom(1003L, layer);
		layer = new GenLayerZoom(1004L, layer);
		layer = new GenLayerZoom(1005L, layer);
		
		GenLayerVoronoiZoom voronoiZoom = new GenLayerVoronoiZoom(10L, layer);
		layer.initWorldGenSeed(seed);
		voronoiZoom.initWorldGenSeed(seed);
		
		return new GenLayer[] {layerLands, layer, voronoiZoom};
	}
	
}