package com.mraof.minestuck.world.lands.decorator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public abstract class BiomeSpecificDecorator implements ILandDecorator
{
	
	protected final List<Biome> biomes;
	
	protected BiomeSpecificDecorator(Biome... biomes)
	{
		this.biomes = Arrays.asList(biomes);
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		BlockPos pos = null;
		
		int count = getCount(random);
		
		for(int i = 0; i < count; i++)
		{
			BlockPos genPos = new BlockPos((chunkX << 4) + 8 + random.nextInt(16), random.nextInt(256), (chunkZ << 4) + 8 + random.nextInt(16));
			
			if(!biomes.isEmpty() && !biomes.contains(world.getBiomeForCoordsBody(genPos)))
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