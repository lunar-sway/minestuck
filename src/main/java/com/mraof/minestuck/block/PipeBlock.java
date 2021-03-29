package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class PipeBlock extends MSDirectionalBlock
{
	public final ImmutableMap<Direction, VoxelShape> shape;
	
	public PipeBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties);
		this.shape = shape.createRotatedShapesAllDirections();
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.DOWN));
	}
	
	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
	{
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
}