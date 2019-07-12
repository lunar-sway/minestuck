package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LayeredBlock extends SnowBlock
{
	public LayeredBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{}
}