package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableBlock extends Block
{
	protected final int flammability, fireSpread;
	
	public FlammableBlock(int flammability, int fireSpread, Properties properties)
	{
		super(properties);
		this.flammability = flammability;
		this.fireSpread = fireSpread;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
	{
		return fireSpread;
	}
}