package com.mraof.minestuck.world.lands.decorator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

/**
 * Randomly selects a single block in the central 16x16 blocks amongst 2x2 chunks, and, if that block is in an accepted biome, runs <code>generate()</code> on that block.
 * This process is repeated <code>getCount()</code> times.
 * <code>BiomeSpecificDecorator</code> shouldn't be used for decorators that iterate through every block in a chunk.
 */
public abstract class BiomeSpecificDecorator implements ILandDecorator
{
	
	protected final Set<Biome> biomes;
	
	protected BiomeSpecificDecorator(Biome... biomes)
	{
		this.biomes = new HashSet<Biome>(Arrays.asList(biomes));
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		BlockPos pos = null;
		
		int count = getCount(random);
		
		for(int i = 0; i < count; i++)
		{
			BlockPos genPos = new BlockPos((chunkX << 4) + 8 + random.nextInt(16), random.nextInt(256), (chunkZ << 4) + 8 + random.nextInt(16));
			
			if(!biomes.isEmpty() && !biomes.contains(world.getBiomeBody(genPos)))
				continue;
			
			BlockPos tempPos = generate(world, random, genPos, provider);
			if(tempPos != null)
				pos = tempPos;
		}
		
		return pos;
	}
	
	public abstract BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider);
	
	public abstract int getCount(Random random);
	
}