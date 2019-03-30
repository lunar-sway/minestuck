package com.mraof.minestuck.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockPetrifiedFlora extends BlockBush
{
	public BlockPetrifiedFlora(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
        return state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.GRAVEL || state.getBlock() == Blocks.COBBLESTONE;
    }
}