package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class WorldGenDecorator extends BiomeSpecificDecorator
{
	
	protected WorldGenerator worldgen;
	protected float priority;
	protected int tries;
	
	public WorldGenDecorator(WorldGenerator worldGen, int tries, float priority, Biome... biomes)
	{
		super(biomes);
		this.worldgen = worldGen;
		this.tries = tries;
		this.priority = priority;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		worldgen.generate(world, random, pos);
		
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		return tries;
	}
	
	@Override
	public float getPriority()
	{
		return priority;
	}
}