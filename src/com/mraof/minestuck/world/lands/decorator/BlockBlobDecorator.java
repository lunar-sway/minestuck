package com.mraof.minestuck.world.lands.decorator;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class BlockBlobDecorator extends BiomeSpecificDecorator	//A version of WorldGenBlockBlob slightly adapted to this usage area
{
	
	private IBlockState block;
	private int size;
	private int count;
	
	public BlockBlobDecorator(IBlockState block, int size, int count, Biome... biomes)
	{
		super(biomes);
		this.block = block;
		this.count = count;
		this.size = size;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(pos);
		
		for (int i1 = 0; i1 < 3; ++i1)
		{
			int xSize = size + random.nextInt(2);
			int ySize = size + random.nextInt(2);
			int zSize = size + random.nextInt(2);
			float f = (float)(xSize + ySize + zSize) * 0.333F + 0.5F;
			Iterator iterator = BlockPos.getAllInBox(pos.add(-xSize, -ySize, -zSize), pos.add(xSize, ySize, zSize)).iterator();
			
			while (iterator.hasNext())
			{
				BlockPos blockpos1 = (BlockPos)iterator.next();
				
				if (blockpos1.distanceSq(pos) <= (double)(f * f))
					world.setBlockState(blockpos1, block, 2);
			}
			
			pos = pos.add(-(size + 1) + random.nextInt(2 + size * 2), 0 - random.nextInt(2), -(size + 1) + random.nextInt(2 + size * 2));
		}
		
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		int i = 0;
		while(i < random.nextInt(count))
			i++;
		return i;
	}
	
	@Override
	public float getPriority()
	{
		return 0.7F;
	}
}