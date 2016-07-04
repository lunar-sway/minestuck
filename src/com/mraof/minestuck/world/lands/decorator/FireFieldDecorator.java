package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class FireFieldDecorator extends BiomeSpecificDecorator
{
	private final int tries;
	private static final int blocks = 96;
	private static final float fireChance = 0.5F;
	
	public FireFieldDecorator(int tries, Biome... biomes)
	{
		super(biomes);
		this.tries = tries;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		for(int i2 = 0; i2 < blocks; i2++)
		{
			BlockPos pos1 = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			IBlockState block = world.getBlockState(pos1);
			if(block != provider.groundBlock && (block == provider.surfaceBlock || block == provider.upperBlock))
			{
				world.setBlockState(pos1, Blocks.NETHERRACK.getDefaultState(), 2);
				if(world.isAirBlock(pos1.up()) && random.nextFloat() < fireChance)
					world.setBlockState(pos1.up(), Blocks.FIRE.getDefaultState(), 2);
			}
		}
		
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
		return 0.5F;
	}
	
}