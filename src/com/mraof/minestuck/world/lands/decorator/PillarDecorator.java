package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class PillarDecorator extends BiomeSpecificDecorator
{
	public String blockType;
	public float count;
	public boolean large;
	
	public PillarDecorator(String blockType, float count, boolean large, Biome... biomes)
	{
		super(biomes);
		this.blockType = blockType;
		this.large = large;
		this.count = count;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		IBlockState state = provider.blockRegistry.getBlockState(blockType);
		pos = world.getPrecipitationHeight(pos);
		while(world.getBlockState(pos.down()).getMaterial().isLiquid())
			pos = pos.down();
		
		int height = 4 + random.nextInt(4);
		
		if(world.getBlockState(pos.up(height - 1)).getMaterial().isLiquid())
			return null;
		
		boolean size = large ? random.nextFloat() < 0.4 : false;
		
		if(size)
		{
			for(int i = 0; i < height + 3; i++)
			{
				world.setBlockState(pos.add(0, i, 0), state);
				world.setBlockState(pos.add(1, i, 0), state);
				world.setBlockState(pos.add(1, i, 1), state);
				world.setBlockState(pos.add(0, i, 1), state);
			}
		} else
		{
			for(int i = 0; i < height; i++)
				world.setBlockState(pos.up(i), state);
		}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.8F;
	}
	
	@Override
	public int getCount(Random random)
	{
		int i = (int) count;
		return i + (random.nextFloat() < (count - i) ? 1 : 0);
	}
}