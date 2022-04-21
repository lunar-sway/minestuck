package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Rotates(horizontally) the block it is facing when it encounters a new redstone signal, assuming that block it is facing is already horizontally oriented and would not break rules mostly comparable to piston movement.
 * Whether it rotates clockwise or counterclockwise depends on the ROTATION_FLIPPED property which can be cycled by right clicking
 */
public class RotatorBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ROTATION_FLIPPED = BlockStateProperties.INVERTED;
	
	public RotatorBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(ROTATION_FLIPPED, false));
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		if(fromPos != pos.relative(state.getValue(FACING)))
			updatePower(worldIn, pos);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		updatePower(worldIn, pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		worldIn.setBlock(pos, state.cycle(ROTATION_FLIPPED), Constants.BlockFlags.DEFAULT);
		if(state.getValue(ROTATION_FLIPPED))
			worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
		else
			worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
		return ActionResultType.SUCCESS;
	}
	
	public void updatePower(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			BlockState state = worldIn.getBlockState(pos);
			boolean hasPower = worldIn.hasNeighborSignal(pos);
			
			boolean isPoweredBeforeUpdate = state.getValue(POWERED);
			
			if(state.getValue(POWERED) != hasPower)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, hasPower));
			
			if(!isPoweredBeforeUpdate && hasPower)
			{
				BlockPos facingPos = pos.relative(state.getValue(FACING));
				BlockState facingState = worldIn.getBlockState(facingPos);
				BlockState rotatedFacingState;
				if(state.getValue(ROTATION_FLIPPED))
					rotatedFacingState = facingState.rotate(worldIn, facingPos, Rotation.COUNTERCLOCKWISE_90);
				else
					rotatedFacingState = facingState.rotate(worldIn, facingPos, Rotation.CLOCKWISE_90);
				
				if((rotatedFacingState.canSurvive(worldIn, pos) && !rotatedFacingState.hasLargeCollisionShape() && (PistonBlock.isPushable(rotatedFacingState, worldIn, facingPos, null, false, null)) ||
						(rotatedFacingState.getBlock() instanceof MSDirectionalBlock || MSTags.Blocks.RULE_EXEMPT_ROTATABLE.contains(rotatedFacingState.getBlock()))))
					worldIn.setBlockAndUpdate(facingPos, rotatedFacingState);
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(ROTATION_FLIPPED);
	}
}