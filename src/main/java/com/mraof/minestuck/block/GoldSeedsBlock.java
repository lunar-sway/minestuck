package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class GoldSeedsBlock extends Block
{
	public static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 4, 16);
	
	public GoldSeedsBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if(worldIn.getBlockState(pos.down()).getBlock() != Blocks.FARMLAND)
		{
			spawnDrops(state, worldIn, pos);
			worldIn.removeBlock(pos, false);
		}
	}
}