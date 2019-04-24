package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockGoldSeeds extends Block
{
	public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 4, 16);
	
	public BlockGoldSeeds(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if(worldIn.getBlockState(pos.down()).getBlock() != Blocks.FARMLAND)
		{
			state.dropBlockAsItem(worldIn, pos, 0);
			worldIn.removeBlock(pos);
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}