package com.mraof.minestuck.block;

import com.mraof.minestuck.block.redstone.LogicGateBlock;
import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Rotates(horizontally) the block it is facing when it encounters a new redstone signal, assuming that block it is facing is already horizontally oriented and would not break rules mostly comparable to piston movement
 */
public class RotatorBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	protected RotatorBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false));
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		if(fromPos != pos.relative(state.getValue(FACING)))
			updatePower(worldIn, pos);
	}
	
	public void updatePower(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			BlockState state = worldIn.getBlockState(pos);
			int powerInt = worldIn.getBestNeighborSignal(pos);
			
			boolean isPoweredBeforeUpdate = state.getValue(POWERED); //
			
			if(state.getValue(POWERED) != powerInt > 0)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, powerInt > 0));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
			
			if(!isPoweredBeforeUpdate && powerInt > 0)
			{
				BlockPos facingPos = pos.relative(state.getValue(FACING));
				BlockState facingState = worldIn.getBlockState(facingPos);
				BlockState rotatedFacingState = facingState.rotate(worldIn, facingPos, Rotation.CLOCKWISE_90);
				if((rotatedFacingState.canSurvive(worldIn, pos) && !rotatedFacingState.hasLargeCollisionShape() && (PistonBlock.isPushable(rotatedFacingState, worldIn, facingPos, null, false, null)) ||
						(rotatedFacingState.getBlock() instanceof MSDirectionalBlock || rotatedFacingState.getBlock() instanceof LogicGateBlock)))
					worldIn.setBlockAndUpdate(facingPos, rotatedFacingState);
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
	}
}