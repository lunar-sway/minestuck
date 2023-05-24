package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class EndLeavesBlock extends FlammableLeavesBlock
{
	public static final int LEAF_SUSTAIN_DISTANCE = 5;
	
	public EndLeavesBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return state.getValue(DISTANCE) > LEAF_SUSTAIN_DISTANCE && !state.getValue(PERSISTENT);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if(!state.getValue(PERSISTENT) && state.getValue(DISTANCE) > LEAF_SUSTAIN_DISTANCE)
		{
			dropResources(state, level, pos);
			level.removeBlock(pos, false);
		}
	}
	
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
	{
		int i = getDistance(facingState) + 1;
		if(i != 1 || stateIn.getValue(DISTANCE) != i)
			level.scheduleTick(currentPos, this, 1);
		
		return stateIn;
	}
	
	protected BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos)
	{
		int i = 7;
		
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		
		for(Direction facing : Direction.values())
		{
			mutablePos.set(pos).move(facing);
			int axisDecrease = facing.getAxis() == Direction.Axis.X ? 2 : 1;
			i = Math.min(i, getDistance(level.getBlockState(mutablePos)) + axisDecrease);
			if(i == 1)
				break;
		}
		
		
		return state.setValue(DISTANCE, i);
	}
	
	protected int getDistance(BlockState neighbor)
	{
		if(neighbor.is(MSBlocks.END_LOG.get()))
		{
			return 0;
		} else
		{
			return neighbor.is(this) ? neighbor.getValue(DISTANCE) : 7;
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return updateDistance(this.defaultBlockState().setValue(PERSISTENT, true), context.getLevel(), context.getClickedPos());
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return 2;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face)
	{
		return 50;
	}
}