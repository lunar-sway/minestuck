package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public abstract class SingleBlockDecorator extends BiomeSpecificDecorator
{
	
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(pos);
		
		if(canPlace(pos, world))
			world.setBlockState(pos, pickBlock(random), 2);
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
	public abstract IBlockState pickBlock(Random random);
	
	public abstract boolean canPlace(BlockPos pos, World world);
}