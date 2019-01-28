package com.mraof.minestuck.world.lands.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class LandTerrainGenBase implements ILandTerrainGen
{
	
	public int seaHeight = 62;
	protected ChunkProviderLands provider;
	
	public LandTerrainGenBase(ChunkProviderLands chunkProvider)
	{
		this.provider = chunkProvider;
	}
	
	public void setSeaHeight(int sea)
	{
		this.seaHeight = sea;
		provider.landWorld.setSeaLevel(this.seaHeight + 1);		//We add 1 to maintain the one-block discrepancy already present, which is required to ensure proper generation of villages.
	}
	
	@Override
	public ChunkPrimer createChunk(int chunkX, int chunkZ)
	{
		IBlockState ground = provider.getGroundBlock();
		IBlockState upper = provider.getUpperBlock();
		IBlockState surface = provider.getSurfaceBlock();
		IBlockState river = provider.blockRegistry.getBlockState("river");
		IBlockState ocean = provider.blockRegistry.getBlockState("ocean");
		
		ChunkPrimer primer = new ChunkPrimer();
		
		int[] topBlock = getHeightMap(chunkX, chunkZ);	//original code
		int[] topRiverBlock = getRiverHeightMap(chunkX, chunkZ);
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				primer.setBlockState(x, 0, z, Blocks.BEDROCK.getDefaultState());
				int riverHeight = Math.max(0, topRiverBlock[x << 4 | z] - Math.max(0, seaHeight - topBlock[x << 4 | z]));
				int y;
				int yMax = topBlock[x << 4 | z] - 3 - riverHeight;
				for(y = 1; y < yMax; y++)
				{
					primer.setBlockState(x, y, z, ground);
				}
				
				int upperBlockHeight = (riverHeight > 0 || yMax + 3 >= seaHeight) ? 2 : 3;
				for(; y < yMax + upperBlockHeight; y++)
					primer.setBlockState(x, y, z, upper);
				
				if(y >= seaHeight && riverHeight == 0)
					primer.setBlockState(x, y, z, surface);
				else
				{
					for(int i = y + riverHeight; y < i; y++)
						primer.setBlockState(x, y, z, river);
					
					for(; y <= seaHeight; y++)
						primer.setBlockState(x, y, z, ocean);
				}
			}
		
		return primer;
	}
	
	protected abstract int[] getHeightMap(int chunkX, int chunkZ);
	
	protected abstract int[] getRiverHeightMap(int chunkX, int chunkZ);
	
}
