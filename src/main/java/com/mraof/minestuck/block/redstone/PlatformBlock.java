package com.mraof.minestuck.block.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class PlatformBlock extends Block
{
	//public static final BooleanProperty PLAYER_PERMEABLE = BlockStateProperties.ENABLED;
	
	public PlatformBlock(Properties properties)
	{
		super(properties);
		//this.setDefaultState(this.stateContainer.getBaseState().with(PLAYER_PERMEABLE, false));
	}
	
	/*@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		return !state.get(PLAYER_PERMEABLE); //TODO Does not work
	}*/
	
	@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		super.tick(state, worldIn, pos, rand);
		
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos), this, 10);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		//builder.add(PLAYER_PERMEABLE);
	}
}