package com.mraof.minestuck.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class FlammableLogBlock extends RotatedPillarBlock
{
	private final int flammability, encouragement;
	
	public FlammableLogBlock(Properties properties)
	{
		this(5, 5, properties);
	}
	
	public FlammableLogBlock(int flammability, int encouragement, Properties properties)
	{
		super(properties);
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