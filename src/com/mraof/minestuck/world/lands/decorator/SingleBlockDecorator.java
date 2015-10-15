package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public abstract class SingleBlockDecorator implements ILandDecorator
{
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		int blocks = getBlocksForChunk(chunkX, chunkZ, random);
		for(int i = 0; i < blocks; i++)
		{
			int x = random.nextInt(16) + (chunkX << 4) + 8;
			int z = random.nextInt(16) + (chunkZ << 4) + 8;
			BlockPos pos = world.getHeight(new BlockPos(x, 0, z));
			
			if(canPlace(pos, world))
				world.setBlockState(pos, pickBlock(random), 2);
		}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
	public abstract IBlockState pickBlock(Random random);
	
	public abstract int getBlocksForChunk(int chunkX, int chunkZ, Random random);
	
	public abstract boolean canPlace(BlockPos pos, World world);
}