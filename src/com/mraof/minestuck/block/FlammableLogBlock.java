package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class FlammableLogBlock extends LogBlock
{
	private final int flammability, encouragement;
	
	public FlammableLogBlock(MaterialColor axisColor, Properties properties)
	{
		this(axisColor, 5, 5, properties);
	}
	
	public FlammableLogBlock(MaterialColor axisColor, int flammability, int encouragement, Properties properties)
	{
		super(axisColor, properties);
		this.flammability = flammability;
		this.encouragement = encouragement;
	}
	
	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return encouragement;
	}
}