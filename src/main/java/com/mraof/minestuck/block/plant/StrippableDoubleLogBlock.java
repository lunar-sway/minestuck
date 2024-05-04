package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StrippableDoubleLogBlock extends StrippableFlammableLogBlock
{
	public static final EnumProperty<Direction.Axis> AXIS_2 = MSProperties.AXIS_2;
	
	public StrippableDoubleLogBlock(Properties pProperties, Supplier<BlockState> strippedState)
	{
		super(pProperties, strippedState);
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
	
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate)
	{
		return super.getToolModifiedState(state, context, toolAction, simulate);
	}
}