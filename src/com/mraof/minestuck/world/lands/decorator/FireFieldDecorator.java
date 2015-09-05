package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class FireFieldDecorator implements ILandDecorator
{
	private static final int tries = 8;
	private static final int blocks = 96;
	private static final float fireChance = 0.5F;
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		BlockPos chunkPos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
		for(int i1 = 0; i1 < tries; i1++)
		{
			BlockPos pos0 = chunkPos.add(random.nextInt(16), random.nextInt(256), random.nextInt(16));
			for(int i2 = 0; i2 < blocks; i2++)
			{
				BlockPos pos1 = pos0.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
				IBlockState block = world.getBlockState(pos1);
				if(block != provider.groundBlock && (block == provider.surfaceBlock || block == provider.upperBlock))
				{
					world.setBlockState(pos1, Blocks.netherrack.getDefaultState(), 2);
					if(world.isAirBlock(pos1.up()) && random.nextFloat() < fireChance)
						world.setBlockState(pos1.up(), Blocks.fire.getDefaultState(), 2);
				}
			}
		}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}