package com.mraof.minestuck.block;

import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockMinestuckLog extends BlockLog
{
	private final int flammability, encouragement;
	
	public BlockMinestuckLog(MaterialColor axisColor, Properties properties)
	{
		this(axisColor, 5, 5, properties);
	}
	
	public BlockMinestuckLog(MaterialColor axisColor, int flammability, int encouragement, Properties properties)
	{
		super(axisColor, properties);
		this.flammability = flammability;
		this.encouragement = encouragement;
	}
	
	@Override
	public int getFlammability(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return encouragement;
	}
}