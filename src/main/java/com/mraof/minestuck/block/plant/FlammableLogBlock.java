package com.mraof.minestuck.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

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
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return encouragement;
	}
}