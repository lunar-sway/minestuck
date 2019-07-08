package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;

public class BlockDoubleLog extends FlammableLogBlock
{
	public static final EnumProperty<EnumFacing.Axis> AXIS_2 = MinestuckProperties.AXIS_2;
	
	public BlockDoubleLog(MaterialColor axisColor, Properties properties)
	{
		super(axisColor, properties);
	}
	
	public BlockDoubleLog(MaterialColor axisColor, int flammability, int encouragement, Properties properties)
	{
		super(axisColor, flammability, encouragement, properties);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(AXIS_2);
	}
	
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		return super.getStateForPlacement(context).with(AXIS_2, context.getNearestLookingDirection().getAxis());
	}
}
