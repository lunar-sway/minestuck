package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class IceDecorator implements ILandDecorator
{
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int x = (chunkX << 4) + 8; x < (chunkX+1 << 4) + 8; x++)
			for(int z = (chunkZ << 4) + 8; z < (chunkZ+1 << 4) + 8; z++)
			{
				BlockPos pos = world.getPrecipitationHeight(new BlockPos(x, 0, z)).down();
				Block block1 = world.getBlockState(pos).getBlock();
				if(world.func_175675_v(pos))
					world.setBlockState(pos, Blocks.ice.getDefaultState(), 2);
			}
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.1F;
	}
	
}
