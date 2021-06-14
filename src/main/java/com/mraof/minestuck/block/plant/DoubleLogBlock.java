package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class DoubleLogBlock extends FlammableLogBlock
{
	public static final EnumProperty<Direction.Axis> AXIS_2 = MSProperties.AXIS_2;
	
	public DoubleLogBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(AXIS, Direction.Axis.Y).setValue(AXIS_2, Direction.Axis.Y));
	}
	
	public DoubleLogBlock(int flammability, int encouragement, Properties properties)
	{
		super(flammability, encouragement, properties);
		registerDefaultState(getStateDefinition().any().setValue(AXIS, Direction.Axis.Y).setValue(AXIS_2, Direction.Axis.Y));
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(AXIS_2);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return super.getStateForPlacement(context).setValue(AXIS_2, context.getNearestLookingDirection().getAxis());
	}
}