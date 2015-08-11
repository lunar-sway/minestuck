package com.mraof.minestuck.world.lands.gen;

import com.mraof.minestuck.world.biome.BiomeGenMinestuck;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class LandTerrainGenBase
{
	
	protected ChunkProviderLands provider;
	protected BiomeGenBase[] biomesForGeneration;
	
	public LandTerrainGenBase(ChunkProviderLands chunkProvider)
	{
		this.provider = chunkProvider;
		this.biomesForGeneration = new BiomeGenBase[16];
	}
	
	public ChunkPrimer createChunk(int chunkX, int chunkZ)
	{
		ChunkPrimer primer = new ChunkPrimer();
		/*int[] topBlock = getHeightMap(chunkX, chunkZ);
		int[] topRiverBlock = getRiverHeightMap(chunkX, chunkZ);
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				primer.setBlockState(x, 0, z, Blocks.bedrock.getDefaultState());
				int riverHeight = Math.max(0, topRiverBlock[x << 4 | z] - Math.max(0, 62 - topBlock[x << 4 | z]));
				int y;
				int yMax = topBlock[x << 4 | z] - 3 - riverHeight;
				for(y = 1; y < yMax; y++)
				{
					primer.setBlockState(x, y, z, provider.upperBlock);
				}
				
				for(; y < yMax + (riverHeight == 0 ? 3 : 2); y++)
					primer.setBlockState(x, y, z, provider.surfaceBlock);
				
				for(int i = y + riverHeight; y < i; y++)
					primer.setBlockState(x, y, z, provider.riverBlock);
				
				for(; y < 63; y++)
					primer.setBlockState(x, y, z, provider.oceanBlock);
			}*/
		provider.landWorld.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4, chunkZ * 4, 4, 4);
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				primer.setBlockState(x, 0, z, Blocks.bedrock.getDefaultState());
				BiomeGenBase biome = biomesForGeneration[x/4 + z - (z%4)];
				int height = biome == BiomeGenMinestuck.mediumNormal ? 70 : 60;
				for(int y = 1; y < height; y++)
					primer.setBlockState(x, y, z, provider.surfaceBlock);
			}
		
		return primer;
	}
	
	public abstract int[] getHeightMap(int chunkX, int chunkZ);
	
	public abstract int[] getRiverHeightMap(int chunkX, int chunkZ);
	
}
