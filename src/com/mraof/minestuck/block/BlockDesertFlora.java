package com.mraof.minestuck.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockDesertFlora extends BlockBush
{
	
	public BlockDesertFlora(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return BlockTags.SAND.contains(state.getBlock());
	}
}