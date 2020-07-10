package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

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
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return fireSpread;
	}
}