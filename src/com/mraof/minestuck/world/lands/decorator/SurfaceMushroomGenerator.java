package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class SurfaceMushroomGenerator extends BiomeSpecificDecorator
{
	private int tries;
	private int count;
	
	public SurfaceMushroomGenerator(int tries, int count, Biome... biomes)
	{
		super(biomes);
		this.tries = tries;
		this.count = count;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		for (int i = 0; i < tries; ++i)
		{
			BlockPos pos1 = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			if (world.isAirBlock(pos1) && MinestuckBlocks.glowingMushroom.canSpread(world, pos1, MinestuckBlocks.glowingMushroom.getDefaultState()))
				world.setBlockState(pos1, MinestuckBlocks.glowingMushroom.getDefaultState(), 2);
		}
		
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		return count;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
}