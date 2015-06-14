package com.mraof.minestuck.world.lands.gen;

import java.util.Random;

import com.mraof.minestuck.world.gen.NoiseGeneratorTriangle;

import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class DefaultTerrainGen extends LandTerrainGenBase
{
	
	public NoiseGeneratorOctaves noiseGens[] = new NoiseGeneratorOctaves[2];
	public NoiseGeneratorTriangle noiseGeneratorTriangle;
	
	public DefaultTerrainGen(ChunkProviderLands chunkProvider, Random rand)
	{
		super(chunkProvider);
		this.noiseGens[0] = new NoiseGeneratorOctaves(rand, 7);
		this.noiseGens[1] = new NoiseGeneratorOctaves(rand, 1);
		noiseGeneratorTriangle = new NoiseGeneratorTriangle(rand);
	}
	
	@Override
	public int[] getHeightMap(int chunkX, int chunkZ)
	{
		double[] heightMap = new double[256];
		double[] heightMapTriangles = new double[256];
		int[] topBlock = new int[256];
		
		heightMap = this.noiseGens[0].generateNoiseOctaves(heightMap, chunkX * 16, 10, chunkZ * 16, 16, 1, 16, .1, 0, .1);
		heightMapTriangles = noiseGeneratorTriangle.generateNoiseTriangle(heightMap, chunkX * 16, chunkZ * 16, 16, 16);
		
		for(int i = 0; i < 256; i++)
		{
			int y = (int) (96 + heightMap[i] + heightMapTriangles[i]);
			topBlock[i] = ((y & 511) <= 255  ? y & 255 : 255 - y & 255);
		}
		return topBlock;
	}
	
	@Override
	public int[] getRiverHeightMap(int chunkX, int chunkZ)
	{
		double[] riverHeightMap = new double[256];
		int[] topRiverBlock = new int[256];
		
		riverHeightMap = this.noiseGens[1].generateNoiseOctaves(riverHeightMap, chunkX * 16, 1, chunkZ * 16, 16, 1, 16, .003, 0, .003);
		
		for(int i = 0; i < 256; i++)
		{
			topRiverBlock[i] = (int) (.025 / ((5 * riverHeightMap[i]) * (5 * riverHeightMap[i]) + 0.005));
			if(topRiverBlock[i] == 1)
				topRiverBlock[i] = 0;
		}
		
		return topRiverBlock;
	}
	
}
