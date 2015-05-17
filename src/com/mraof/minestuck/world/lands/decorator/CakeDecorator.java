package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.BlockCake;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class CakeDecorator implements ILandDecorator
{
	
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		if(random.nextDouble() < 0.2)
		{
			for(int i = 0; i < 10; i++)
			{
				if(random.nextBoolean())
					continue;
				
				int x = random.nextInt(16) + (chunkX << 4) + 8;
				int z = random.nextInt(16) + (chunkZ << 4) + 8;
				BlockPos pos = world.getHorizon(new BlockPos(x, 0, z));
				int bits = Math.max(0, (int) (random.nextDouble()*10) - 6);
				if(provider.isPositionInSpawn(x, z))
					continue;
				
				if(Blocks.cake.canPlaceBlockAt(world, pos) && !world.getBlockState(pos).getBlock().getMaterial().isLiquid() && world.getBlockState(pos).getBlock().isReplaceable(world, pos))
					world.setBlockState(pos, Blocks.cake.getDefaultState().withProperty(BlockCake.BITES, bits), 2);
			}
		}
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}