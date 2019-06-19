
package com.mraof.minestuck.world.lands.decorator;

import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class SurfaceDecoratorRegistryVein extends BiomeSpecificDecorator
{
	
	public int amount;
	public String block;
	public int size;
	public float priority;
	
	public SurfaceDecoratorRegistryVein(String block, int amount, int size, Biome... biomes)
	{
		this(block, amount, size, 0.3F, biomes);
	}
	
	public SurfaceDecoratorRegistryVein(String block, int amount, int size, float priority, Biome... biomes)
	{
		super(biomes);
		this.amount = amount;
		this.block = block;
		this.size = size;
		this.priority = priority;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		//(new WorldGenMinable(provider.blockRegistry.getBlockState(block), size, new OreHandler.BlockStatePredicate(provider.getSurfaceBlock(), provider.getUpperBlock()))).generate(world, random, pos.add(-8, 0, -8));
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		return amount;
	}
	
	@Override
	public float getPriority()
	{
		return priority;
	}
	
}
