package com.mraof.minestuck.world.gen.lands.decorator;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public abstract class PostDecorator implements ILandDecorator	//TODO Bug: sometimes there's chunks that doesn't get decorated
{
	
	@Override
	public final void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		checkAndGenerate(world, chunkX, chunkZ, provider);
		checkAndGenerate(world, chunkX + 1, chunkZ, provider);
		checkAndGenerate(world, chunkX, chunkZ + 1, provider);
		checkAndGenerate(world, chunkX + 1, chunkZ + 1, provider);
	}
	
	private final void checkAndGenerate(World world, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		if(world.isAreaLoaded(new BlockPos((chunkX - 1) << 4, 0, (chunkZ - 1) << 4), new BlockPos(chunkX << 4, 0, chunkZ << 4))
				&& world.getChunkFromChunkCoords(chunkX, chunkZ).isTerrainPopulated()
				&& world.getChunkFromChunkCoords(chunkX - 1, chunkZ).isTerrainPopulated()
				&& world.getChunkFromChunkCoords(chunkX, chunkZ - 1).isTerrainPopulated()
				&& world.getChunkFromChunkCoords(chunkX - 1, chunkZ - 1).isTerrainPopulated())
			generateAtChunk(world, new Random(provider.getSeedFor(chunkX, chunkZ)), chunkX, chunkZ, provider);	//TODO change the seed to not match what the other decorators are generated with
	}
	
	public abstract void generateAtChunk(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider);
	
}
