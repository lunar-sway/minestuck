package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class DecorBlock extends Block
{
	public static final VoxelShape CHESSBOARD_SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 1, 16);
	public static final VoxelShape FROG_STATUE_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 15, 15);
	public static final VoxelShape BLENDER_SHAPE = Block.makeCuboidShape(3, 0, 3, 13, 16, 13);
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public final VoxelShape shape;
	
	public DecorBlock(Properties properties, VoxelShape shape)
	{
		super(properties);
		this.shape = shape;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	/*TODO
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
        return worldIn.getBlockState(pos.down()).isFullCube();
    }*/
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape;
	}
}