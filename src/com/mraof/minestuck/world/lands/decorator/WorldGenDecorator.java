package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class WorldGenDecorator implements ILandDecorator
{
	
	protected WorldGenerator worldgen;
	protected float priority;
	protected int tries;
	
	public WorldGenDecorator(WorldGenerator worldGen, int tries, float priority)
	{
		this.worldgen = worldGen;
		this.tries = tries;
		this.priority = priority;
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int i = 0; i < tries; i++)
		{
			BlockPos pos = new BlockPos((chunkX << 4) + 8 + random.nextInt(16), random.nextInt(256), (chunkZ << 4) + 8 + random.nextInt(16));
			worldgen.generate(world, random, pos);
		}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return priority;
	}
}