package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockDecor extends Block
{
	public static final VoxelShape CHESSBOARD_SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 1, 16);
	public static final VoxelShape FROG_STATUE_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 15, 15);
	public static final VoxelShape BLENDER_SHAPE = Block.makeCuboidShape(3, 0, 3, 13, 16, 13);
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public final VoxelShape shape;
	public final Map<EnumFacing, BlockFaceShape> faceShapes;
	
	public BlockDecor(Properties properties, VoxelShape shape, Map<EnumFacing, BlockFaceShape> faceShapes)
	{
		super(properties);
		this.shape = shape;
		this.faceShapes = faceShapes;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		Rotation rotation = BlockMachine.rotationFromFacing(face);
		return faceShapes.get(rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos)
	{
        return worldIn.getBlockState(pos.down()).isFullCube();
    }
	
	@Nullable
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return shape;
	}
}