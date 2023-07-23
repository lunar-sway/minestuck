package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Toggles the state of the block it is facing upon receiving a new redstone signal if that block has the MACHINE_TOGGLE property from MSProperties,
 * or returns redstone power in the form of the POWERED/POWER properties to false/0 if DISCHARGE is set to true
 */
public class TogglerBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty DISCHARGE = MSProperties.MACHINE_TOGGLE;
	
	public TogglerBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(DISCHARGE, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		level.setBlock(pos, state.cycle(DISCHARGE), Block.UPDATE_ALL);
		if(state.getValue(DISCHARGE))
			level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
		else
			level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
		return InteractionResult.sidedSuccess(level.isClientSide);
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
			else level.sendBlockUpdated(pos, state, state, 2);
			
			if(!isPoweredBeforeUpdate && hasPower)
			{
				BlockPos facingPos = pos.relative(state.getValue(FACING));
				BlockState facingState = level.getBlockState(facingPos);
				
				if(state.getValue(DISCHARGE))
					discharge(level, pos, state, facingPos, facingState);
				else
					toggle(level, pos, state, facingPos, facingState);
			}
		}
	}
	
	private void toggle(Level level, BlockPos pos, BlockState state, BlockPos facingPos, BlockState facingState)
	{
		//TODO consider allowing the active type of RemoteObserverBlockEntity and StatStorerBlockEntity to be iterated while in toggle mode
		if(facingState.hasProperty(MSProperties.MACHINE_TOGGLE))
		{
			level.setBlock(facingPos, facingState.cycle(MSProperties.MACHINE_TOGGLE), Block.UPDATE_ALL);
			
			if(!level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).is(BlockTags.WOOL)) //wont make a toggle sound if the toggler is "muted" by a wool block
				level.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, facingState.getValue(MSProperties.MACHINE_TOGGLE) ? 1.5F : 0.5F);
		}
	}
	
	private void discharge(Level level, BlockPos pos, BlockState state, BlockPos facingPos, BlockState facingState)
	{
		boolean updated = false;
		
		//removing redstone power from powered blocks
		if(facingState.isSignalSource())
		{
			if(facingState.hasProperty(BlockStateProperties.POWERED))
			{
				level.setBlock(facingPos, facingState.setValue(BlockStateProperties.POWERED, false), Block.UPDATE_ALL);
				updated = true;
			}
			if(facingState.hasProperty(BlockStateProperties.POWER))
			{
				level.setBlock(facingPos, facingState.setValue(BlockStateProperties.POWER, 0), Block.UPDATE_ALL);
				updated = true;
			}
		}
		
		//setting the active stat type for stat storers to 0
		if(level.getBlockEntity(facingPos) instanceof StatStorerBlockEntity statStorer)
		{
			statStorer.setActiveStoredStatValue(0);
		}
		
		if(updated && !level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).is(BlockTags.WOOL)) //wont make a toggle sound if the toggler is "muted" by a wool block
			level.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, 0.5F);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(DISCHARGE);
	}
}