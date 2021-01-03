package com.mraof.minestuck.block;

import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.Map;

public class FrogBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public final Map<Direction, VoxelShape> shape;
	
	public FrogBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties);
		this.shape = shape.createRotatedShapes();
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
}