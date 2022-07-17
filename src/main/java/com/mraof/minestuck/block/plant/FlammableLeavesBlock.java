package com.mraof.minestuck.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableLeavesBlock extends LeavesBlock
{
	public FlammableLeavesBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return 5;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return 5;
	}
}