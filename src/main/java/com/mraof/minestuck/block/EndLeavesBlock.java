package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EndLeavesBlock extends FlammableLeavesBlock
{
	public static final int LEAF_SUSTAIN_DISTANCE = 5;
	
	public EndLeavesBlock(Properties properties)
	{
		super(properties.notSolid());
	}
	
	@Override
	public boolean ticksRandomly(BlockState state)
	{
		return state.get(DISTANCE) >= LEAF_SUSTAIN_DISTANCE && !state.get(PERSISTENT);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(!state.get(PERSISTENT) && state.get(DISTANCE) >= LEAF_SUSTAIN_DISTANCE)
		{
			spawnDrops(state, worldIn, pos);
			worldIn.removeBlock(pos, false);
		}
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		int i = getDistance(facingState) + 1;
		if(i != 1 || stateIn.get(DISTANCE) != i)
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		
		return stateIn;
	}
	
	protected BlockState updateDistance(BlockState state, IWorld world, BlockPos pos) {
		int i = 7;
		
		try (BlockPos.PooledMutable mutablePos = BlockPos.PooledMutable.retain())
		{
			for(Direction facing : Direction.values())
			{
				mutablePos.setPos(pos).move(facing);
				int axisDecrease = facing.getAxis() == Direction.Axis.X ? 2 : 1;
				i = Math.min(i, getDistance(world.getBlockState(mutablePos)) + axisDecrease);
				if(i == 1)
					break;
			}
		}
		
		return state.with(DISTANCE, i);
	}
	
	protected int getDistance(BlockState neighbor)
	{
		if(neighbor.getBlock() == MSBlocks.END_LOG)
		{
			return 0;
		} else
		{
			return neighbor.getBlock() == this ? neighbor.get(DISTANCE) : 7;
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return updateDistance(this.getDefaultState().with(PERSISTENT, true), context.getWorld(), context.getPos());
	}
	
	@Override
	public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return 1;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return 250;
	}
}