package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class BlockLargeMachine extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	public BlockLargeMachine(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(FACING);
	}
	
	//keeps the blocks from dropping something
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
	{}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public EnumPushReaction getPushReaction(IBlockState state)
	{
		return EnumPushReaction.BLOCK;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	//keep the game from crashing when a machine is blown up
	
	@Override
	public void dropBlockAsItemWithChance(IBlockState state, World worldIn, BlockPos pos, float chancePerItem, int fortune)
	{}
	
	public static Rotation rotationFromFacing(EnumFacing facing)
	{
		Rotation rotation;
		switch(facing)
		{
			case EAST:
				rotation = Rotation.CLOCKWISE_90;
				break;
			case SOUTH:
				rotation = Rotation.CLOCKWISE_180;
				break;
			case WEST:
				rotation = Rotation.COUNTERCLOCKWISE_90;
				break;
			default:
				rotation = Rotation.NONE;
				break;
		}
		return rotation;
	}
}