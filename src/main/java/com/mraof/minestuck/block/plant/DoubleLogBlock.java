package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(AXIS_2);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return super.getStateForPlacement(context).setValue(AXIS_2, context.getNearestLookingDirection().getAxis());
	}
}