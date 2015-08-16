package com.mraof.minestuck.world.lands.gen;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class LandTerrainGenBase implements ILandTerrainGen
{
	
	public int seaHeight = 62;
	protected ChunkProviderLands provider;
	
	public LandTerrainGenBase(ChunkProviderLands chunkProvider)
	{
		this.provider = chunkProvider;
	}
	
	public ChunkPrimer createChunk(int chunkX, int chunkZ)
	{
		ChunkPrimer primer = new ChunkPrimer();
		
		int[] topBlock = getHeightMap(chunkX, chunkZ);	//original code
		int[] topRiverBlock = getRiverHeightMap(chunkX, chunkZ);
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				primer.setBlockState(x, 0, z, Blocks.bedrock.getDefaultState());
				int riverHeight = Math.max(0, topRiverBlock[x << 4 | z] - Math.max(0, seaHeight - topBlock[x << 4 | z]));
				int y;
				int yMax = topBlock[x << 4 | z] - 3 - riverHeight;
				for(y = 1; y < yMax; y++)
				{
					primer.setBlockState(x, y, z, provider.groundBlock);
				}
				
				int upperBlockHeight = (riverHeight > 0 || yMax + 3 >= seaHeight) ? 2 : 3;
				for(; y < yMax + upperBlockHeight; y++)
					primer.setBlockState(x, y, z, provider.upperBlock);
				
				if(y >= seaHeight && riverHeight == 0)
					primer.setBlockState(x, y, z, provider.surfaceBlock);
				else
				{
					for(int i = y + riverHeight; y < i; y++)
						primer.setBlockState(x, y, z, provider.riverBlock);
					
					for(; y <= seaHeight; y++)
						primer.setBlockState(x, y, z, provider.oceanBlock);
				}
			}
		
		/*int[] topBlockOrig = getHeightMap(chunkX, chunkZ, false);	Learning what different noise input does
		int[] topBlockTest = getHeightMap(chunkX, chunkZ, true);
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				int heightOrig = topBlockOrig[x << 4 | z];
				int heightTest = topBlockTest[x << 4 | z];
				int y;
				for(y = 0; y < Math.min(heightOrig, heightTest); y++)
					primer.setBlockState(x, y, z, Blocks.stone.getDefaultState());
				for(; y < heightOrig; y++)
					primer.setBlockState(x, y, z, Blocks.dirt.getDefaultState());
				for(; y < heightTest; y++)
					primer.setBlockState(x, y, z, Blocks.gravel.getDefaultState());
			}*/
		
		/*for(int x = 0; x < 16; x++)	Biome test
			for(int z = 0; z < 16; z++)
			{
				primer.setBlockState(x, 0, z, Blocks.bedrock.getDefaultState());
				BiomeGenBase biome = biomesForGeneration[x/4 + z - (z%4)];
				for(int y = 1; y < 70; y++)
					primer.setBlockState(x, y, z, provider.groundBlock);
				if(biome == BiomeGenMinestuck.mediumNormal)
					primer.setBlockState(x, 70, z, provider.upperBlock);
			}*/
		
		return primer;
	}
	
	protected abstract int[] getHeightMap(int chunkX, int chunkZ);
	
	protected abstract int[] getRiverHeightMap(int chunkX, int chunkZ);
	
}
