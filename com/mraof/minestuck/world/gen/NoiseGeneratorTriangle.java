package com.mraof.minestuck.world.gen;

import java.util.Random;


public class NoiseGeneratorTriangle
{
	double[] amplitudes = new double[5];
	double[] frequencies = new double[5];
	public NoiseGeneratorTriangle(Random random)
	{
		double averageAmplitudes = 0;
		for(int i = 0; i < amplitudes.length; i++)
		{
			amplitudes[i] = random.nextDouble() * 96;
			averageAmplitudes += amplitudes[i];
		}
		averageAmplitudes /= amplitudes.length;
		for(int i = 0; i < frequencies.length; i++)
			frequencies[i] = averageAmplitudes / amplitudes[i] * (random.nextDouble() * 1.5 + .5 );
	}
	double triangleNoise(double length, double amplitude, double frequency)
	{
		length *= frequency;
		return length - 2 * amplitude * (int) (length / amplitude / 2) - amplitude;
	}
	double height(double x, double z)
	{
		return (triangleNoise(x, amplitudes[0], frequencies[0]) + triangleNoise(x, amplitudes[1], frequencies[1]) + triangleNoise(x, amplitudes[2], frequencies[1]) + triangleNoise(z, amplitudes[3], frequencies[3]) + triangleNoise(z, amplitudes[4], frequencies[4])) / 5;
	}
	
	double[] generateNoiseTriangle(double[] heightMap, double xOffset, double zOffset, int xSize, int zSize)
	{
		double[] noise = new double[xSize * zSize];
		for(int x = 0; x < xSize; x++)
			for(int z = 0; z < zSize; z++)
				noise[x + z * xSize] = height(heightMap[x + z * xSize], heightMap[x + z * xSize]);
		return noise;
	}

}
