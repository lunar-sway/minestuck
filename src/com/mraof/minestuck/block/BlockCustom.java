package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockCustom extends Block
{
	protected final ToolType harvestTool;
	protected final int harvestLevel;
	protected final int flammability, fireSpread;
	
	public BlockCustom(int flammability, int fireSpread, Properties properties)
	{
		this(flammability, fireSpread, null, 0, properties);
	}
	public BlockCustom(ToolType harvestTool, int harvestLevel, Properties properties)
	{
		this(0, 0, harvestTool, harvestLevel, properties);
	}
	
	public BlockCustom(int flammability, int fireSpread, ToolType harvestTool, int harvestLevel, Properties properties)
	{
		super(properties);
		this.harvestTool = harvestTool;
		this.harvestLevel = harvestLevel;
		this.flammability = flammability;
		this.fireSpread = fireSpread;
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(IBlockState state)
	{
		return harvestTool;
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return harvestLevel;
	}
	
	@Override
	public int getFlammability(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return flammability;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return fireSpread;
	}
}