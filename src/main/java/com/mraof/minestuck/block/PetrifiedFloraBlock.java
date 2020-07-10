package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class PetrifiedFloraBlock extends BushBlock
{
	public PetrifiedFloraBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
        return state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.GRAVEL || state.getBlock() == Blocks.COBBLESTONE;
    }
}