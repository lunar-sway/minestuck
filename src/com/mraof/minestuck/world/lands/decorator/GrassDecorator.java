package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class GrassDecorator implements ILandDecorator
{
	
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(chunkX << 4, 0, chunkZ << 4).add(x + 8, 0, z + 8));
				if(world.getBlockState(pos).getBlock().getMaterial().isLiquid() || world.getBlockState(pos).getBlock().getMaterial().isSolid())
					continue;
				else pos = pos.down();
				
				if(world.getBlockState(pos).equals(Blocks.dirt.getDefaultState()))
					world.setBlockState(pos, Blocks.grass.getDefaultState(), 2);
			}
	}
	
	@Override
	public float getPriority()
	{
		return 0.3F;
	}
	
}
