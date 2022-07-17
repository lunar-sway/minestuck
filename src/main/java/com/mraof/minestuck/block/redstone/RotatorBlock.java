package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Rotates(horizontally) the block it is facing when it encounters a new redstone signal, assuming that block it is facing is already horizontally oriented and would not break rules mostly comparable to piston movement.
 * Whether it rotates clockwise or counterclockwise depends on the ROTATION_FLIPPED property which can be cycled by right clicking
 */
public class RotatorBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ROTATION_FLIPPED = MSProperties.MACHINE_TOGGLE;
	
	public RotatorBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(ROTATION_FLIPPED, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		if(fromPos != pos.relative(state.getValue(FACING)))
			updatePower(level, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		updatePower(level, pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		level.setBlock(pos, state.cycle(ROTATION_FLIPPED), Block.UPDATE_ALL);
		if(state.getValue(ROTATION_FLIPPED))
			level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
		else
			level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
		return InteractionResult.SUCCESS;
	}
	
	public void updatePower(Level level, BlockPos pos)
	{
		if(!level.isClientSide)
		{
			BlockState state = level.getBlockState(pos);
			Direction stateFacing = state.getValue(FACING);
			boolean hasPower = BlockUtil.hasSignalNotFromFacing(level, pos, stateFacing);
			
			boolean isPoweredBeforeUpdate = state.getValue(POWERED);
			
			if(state.getValue(POWERED) != hasPower)
				level.setBlockAndUpdate(pos, state.setValue(POWERED, hasPower));
			
			if(!isPoweredBeforeUpdate && hasPower)
			{
				BlockPos facingPos = pos.relative(state.getValue(FACING));
				BlockState facingState = level.getBlockState(facingPos);
				BlockState rotatedFacingState;
				if(state.getValue(ROTATION_FLIPPED))
					rotatedFacingState = facingState.rotate(level, facingPos, Rotation.COUNTERCLOCKWISE_90);
				else
					rotatedFacingState = facingState.rotate(level, facingPos, Rotation.CLOCKWISE_90);
				
				if((rotatedFacingState.canSurvive(level, pos) && !rotatedFacingState.hasLargeCollisionShape() && (PistonBaseBlock.isPushable(rotatedFacingState, level, facingPos, null, false, null)) ||
						rotatedFacingState.is(MSTags.Blocks.ROTATOR_WHITELISTED)))
					level.setBlockAndUpdate(facingPos, rotatedFacingState);
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(ROTATION_FLIPPED);
	}
}