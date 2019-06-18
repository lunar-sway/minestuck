package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class GenLayerLands //extends GenLayer
{
	private int oceanChance;
	private GenLayerLandRough roughGen;
	
	/*public GenLayerLands(long seed)
	{
		super(seed);
	}
	
	public void setChance(float oceanChance, float roughChance)
	{
		this.oceanChance = (int) (Integer.MAX_VALUE*oceanChance);
		roughGen.setRoughChance(roughChance);
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] biomeGen = IntCache.getIntCache(areaWidth * areaHeight);
		
		for(int x = 0; x < areaWidth; x++)
			for(int y = 0; y < areaHeight; y++)
			{
				initChunkSeed(areaX + x, areaY + y);
				biomeGen[x + y*areaWidth] = Biome.getIdForBiome((nextInt(Integer.MAX_VALUE) >= oceanChance ? BiomeMinestuck.mediumNormal : BiomeMinestuck.mediumOcean));
			}
		return biomeGen;
	}
	
	public static GenLayer[] generateBiomeGenLayers(long seed)
	{
		GenLayerLands layerLands = new GenLayerLands(413L);
		GenLayer layer = new GenLayerZoom(1000L, layerLands);
		layer = layerLands.roughGen = new GenLayerLandRough(414L, layerLands);
		layer = new GenLayerZoom(1001L, layer);
		layer = new GenLayerZoom(1002L, layer);
		layer = new GenLayerZoom(1003L, layer);
		layer = new GenLayerZoom(1004L, layer);
		layer = new GenLayerZoom(1005L, layer);
		
		GenLayerVoronoiZoom voronoiZoom = new GenLayerVoronoiZoom(10L, layer);
		layer.initWorldGenSeed(seed);
		voronoiZoom.initWorldGenSeed(seed);
		
		return new GenLayer[] {layerLands, layer, voronoiZoom};
	}*/
	
}