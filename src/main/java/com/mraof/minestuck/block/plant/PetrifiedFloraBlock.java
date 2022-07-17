package com.mraof.minestuck.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PetrifiedFloraBlock extends BushBlock
{
	public PetrifiedFloraBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
        return state.is(Blocks.STONE) || state.is(Blocks.GRAVEL) || state.is(Blocks.COBBLESTONE);	//TODO define a block tag instead
    }
}