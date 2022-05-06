package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class CruxtruderLidBlock extends Block
{
	public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 5, 14);
	
	public CruxtruderLidBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
}
