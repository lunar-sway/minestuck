package com.mraof.minestuck.world.lands.gen;

import java.util.Random;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.gen.NoiseGeneratorTriangle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class DefaultTerrainGen extends LandTerrainGenBase
{
	
	public float normalHeight    = 0.3F, oceanHeight    = -0.2F, roughHeight     = 0.4F;
	public float normalVariation = 0.5F, oceanVariation =  0.2F, roughVariation = 0.8F;
	
	protected NoiseGeneratorOctaves noiseGens[] = new NoiseGeneratorOctaves[2];
	protected NoiseGeneratorTriangle noiseGeneratorTriangle;
	protected int[] topBlock;	//It's easier to just re-use all arrays
	protected double[] heightMap;
	protected double[] riverHeightMap;
	protected double[] biomeHeightMap, biomeVariationMap;
	protected Biome[] biomesForGeneration;
	public static float[] parabolicField;
	
	{
		parabolicField = new float[25];
		for(int x = -2; x <= 2; x++)
			for(int z = -2; z <= 2; z++)
				parabolicField[(x + 2)*5 + z + 2] = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
	}
	
	public DefaultTerrainGen(ChunkProviderLands chunkProvider, Random rand)
	{
		super(chunkProvider);
		this.noiseGens[0] = new NoiseGeneratorOctaves(rand, 10);
		this.noiseGens[1] = new NoiseGeneratorOctaves(rand, 1);
		noiseGeneratorTriangle = new NoiseGeneratorTriangle(rand);
		topBlock = new int[256];
		biomeHeightMap = new double[25];
		biomeVariationMap = new double[25];
	}
	
	@Override
	protected int[] getHeightMap(int chunkX, int chunkZ)
	{
		int[] topBlock = new int[256];
		
		heightMap = this.noiseGens[0].generateNoiseOctaves(heightMap, chunkX * 16, 10, chunkZ * 16, 16, 1, 16, 10, 1, 10);
//		double[] heightMapTriangles = noiseGeneratorTriangle.generateNoiseTriangle(heightMap, chunkX * 16, chunkZ * 16, 16, 16);
		generateBiomeData(chunkX, chunkZ);
		
		for(int x0 = 0; x0 < 4; x0++)
			for(int z0 = 0; z0 < 4; z0++)
			{
				float f1 = 0.25F;
				double height0 = biomeHeightMap[x0*5 + z0];
				double height1 = biomeHeightMap[x0*5 + z0 + 1];
				double height0Diff = (biomeHeightMap[(x0 + 1)*5 + z0] - height0)*f1;
				double height1Diff = (biomeHeightMap[(x0 + 1)*5 + z0 + 1] - height1)*f1;
				double variation0 = biomeVariationMap[x0*5 + z0];
				double variation1 = biomeVariationMap[x0*5 + z0 + 1];
				double variation0Diff = (biomeVariationMap[(x0 + 1)*5 + z0] - variation0)*f1;
				double variation1Diff = (biomeVariationMap[(x0 + 1)*5 + z0 + 1] - variation1)*f1;
				for(int x1 = 0; x1 < 4; x1++)
				{
					double biomeHeight = height0;
					double biomeHeightDiff = (height1 - height0)*f1;
					double biomeVariation = variation0;
					double biomeVariationDiff = (variation1 - variation0)*f1;
					for(int z1 = 0; z1 < 4; z1++)
					{
						double height = heightMap[(x0*4 + x1)*16 + z0*4 + z1]/40D;
						
						topBlock[(x0*4 + x1)*16 + z0*4 + z1] = (int) (62 + 32*biomeHeight + height*biomeVariation);
						
						biomeHeight += biomeHeightDiff;
						biomeVariation += biomeVariationDiff;
					}
					height0 += height0Diff;
					height1 += height1Diff;
					variation0 += variation0Diff;
					variation1 += variation1Diff;
				}
			}
		
		return topBlock;
	}
	
	@Override
	protected int[] getRiverHeightMap(int chunkX, int chunkZ)
	{
		int[] topRiverBlock = new int[256];
		
		/*riverHeightMap = this.noiseGens[1].generateNoiseOctaves(riverHeightMap, chunkX * 16, 1, chunkZ * 16, 16, 1, 16, .003, 0, .003);
		
		for(int i = 0; i < 256; i++)
		{
			topRiverBlock[i] = (int) (.025 / ((5 * riverHeightMap[i]) * (5 * riverHeightMap[i]) + 0.005));
			if(topRiverBlock[i] == 1)
				topRiverBlock[i] = 0;
		}*/
		
		return topRiverBlock;
	}
	
	protected void generateBiomeData(int chunkX, int chunkZ)
	{
		biomesForGeneration = provider.landWorld.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, chunkX*4 - 2, chunkZ*4 - 2, 9, 9);
		
		for(int x0 = 0; x0 < 5; x0++)
			for(int z0 = 0; z0 < 5; z0++)
			{
				float biomeHeight = getBiomeHeight(biomesForGeneration[(z0 + 2)*9 + x0 + 2]);
				double totalBiomeHeight = 0;
				double totalBiomeVariation = 0;
				float divisor = 0;
				for(int x1 = 0; x1 < 5; x1++)
					for(int z1 = 0; z1 < 5; z1++)
					{
						Biome biome = biomesForGeneration[(z0 + z1)*9 + x0 + x1];
						float multiplier = parabolicField[x1*5 + z1];
						float biomeHeight1 = getBiomeHeight(biome);
						if(biomeHeight1 > biomeHeight)
							multiplier /= 2F;
						
						totalBiomeHeight += biomeHeight1*multiplier;
						totalBiomeVariation += getBiomeVariation(biome)*multiplier;
						divisor += multiplier;
					}
				biomeHeightMap[x0*5 + z0] = totalBiomeHeight / divisor;
				biomeVariationMap[x0*5 + z0] = totalBiomeVariation / divisor;
			}
		
	}
	
	protected float getBiomeHeight(Biome biome)
	{
		if(biome == BiomeMinestuck.mediumOcean)
			return this.oceanHeight;
		else if(biome == BiomeMinestuck.mediumRough)
			return this.roughHeight;
		else return this.normalHeight;
	}
	
	protected float getBiomeVariation(Biome biome)
	{
		if(biome == BiomeMinestuck.mediumOcean)
			return this.oceanVariation;
		else if(biome == BiomeMinestuck.mediumRough)
			return this.roughVariation;
		else return this.normalVariation;
	}
	
}