package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

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
		this(axisColor, properties, 5, 5);
	}
	
	public BlockMinestuckLog(MaterialColor axisColor, Properties properties, int flammability, int encouragement)
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