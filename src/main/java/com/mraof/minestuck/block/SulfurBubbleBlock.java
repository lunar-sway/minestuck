package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SulfurBubbleBlock extends TallGrassBlock
{
	public SulfurBubbleBlock(BlockBehaviour.Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(MSBlocks.NATIVE_SULFUR.get());
	}
}
