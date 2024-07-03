package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * For any block models that need to face any direction instead of just horizontally
 */
public class DirectionalCustomShapeBlock extends MSDirectionalBlock implements SimpleWaterloggedBlock
{
	public final ImmutableMap<Direction, VoxelShape> shape;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public DirectionalCustomShapeBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties);
		this.shape = shape.createRotatedShapesAllDirections();
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(WATERLOGGED, false));
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		FluidState iFluidState = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(WATERLOGGED, iFluidState.getType() == Fluids.WATER);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
	{
		if(stateIn.getValue(WATERLOGGED))
		{
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		
		return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}
	
	@Override
	public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction)
	{
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}
