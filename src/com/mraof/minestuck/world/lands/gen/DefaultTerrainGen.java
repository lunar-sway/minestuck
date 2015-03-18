package com.mraof.minestuck.world.lands.gen;

import java.util.Random;

import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.NoiseGeneratorTriangle;

import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class DefaultTerrainGen extends LandTerrainGenBase
{
	
	private NoiseGeneratorOctaves noiseGens[] = new NoiseGeneratorOctaves[2];
	private NoiseGeneratorTriangle noiseGeneratorTriangle;
	
	public DefaultTerrainGen(ChunkProviderLands chunkProvider, Random rand)
	{
		super(chunkProvider);
		this.noiseGens[0] = new NoiseGeneratorOctaves(rand, 7);
		this.noiseGens[1] = new NoiseGeneratorOctaves(rand, 1);
		noiseGeneratorTriangle = new NoiseGeneratorTriangle(rand);
	}
	
	@Override
	public ChunkPrimer createChunk(int chunkX, int chunkZ)
	{
		ChunkPrimer primer = new ChunkPrimer();
		double[] heightMap = new double[256];
		double[] heightMapTriangles = new double[256];
		double[] riverHeightMap = new double[256];
		int[] topBlock = new int[256];
		int[] topRiverBlock = new int[256];
		
		heightMap = this.noiseGens[0].generateNoiseOctaves(heightMap, chunkX * 16, 10, chunkZ * 16, 16, 1, 16, .1, 0, .1);
		riverHeightMap = this.noiseGens[1].generateNoiseOctaves(riverHeightMap, chunkX * 16, 1, chunkZ * 16, 16, 1, 16, .003, 0, .003);
		heightMapTriangles = noiseGeneratorTriangle.generateNoiseTriangle(heightMap, chunkX * 16, chunkZ * 16, 16, 16);
		
		for(int i = 0; i < 256; i++)
		{
			topRiverBlock[i] = (int) (.025 / ((5 * riverHeightMap[i]) * (5 * riverHeightMap[i]) + 0.005));
		}
		
		for(int i = 0; i < 256; i++)
		{
			int y = (int) (96 + heightMap[i] + heightMapTriangles[i]);
			topBlock[i] = ((y & 511) <= 255  ? y & 255 : 255 - y & 255) - topRiverBlock[i];
		}
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				primer.setBlockState(x, 0, z, Blocks.bedrock.getDefaultState());
				int y;
				int yMax = topBlock[x << 4 | z] - 2 - topRiverBlock[x << 4 | z];
				for(y = 1; y < yMax; y++)
				{
					//currentBlockOffset = (int) Math.abs(generated1[x + z << 8 + y * 16]);
					primer.setBlockState(x, y, z, provider.upperBlock);
				}
				
				//location copied from the chunk constructor: x * chunkBlocks.length/256 * 16 | z * blockSize/256 | y
				for(; y < yMax + 2; y++)
				{
					primer.setBlockState(x, y, z, provider.surfaceBlock);
				}
				
				for(int i = y + topRiverBlock[x << 4 | z]; y < i; y++)
					primer.setBlockState(x, y, z, provider.riverBlock);
				
				for(; y < 63; y++)
					primer.setBlockState(x, y, z, provider.oceanBlock);
				
			}
		
		return primer;
	}
	
}
