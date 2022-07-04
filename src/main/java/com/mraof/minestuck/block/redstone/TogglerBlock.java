package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		worldIn.setBlock(pos, state.cycle(DISCHARGE), Constants.BlockFlags.DEFAULT);
		if(state.getValue(DISCHARGE))
			worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
		else
			worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
		return ActionResultType.sidedSuccess(worldIn.isClientSide);
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
	
	public void updatePower(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			BlockState state = worldIn.getBlockState(pos);
			Direction stateFacing = state.getValue(FACING);
			boolean hasPower = BlockUtil.hasSignalNotFromFacing(worldIn, pos, stateFacing);
			
			boolean isPoweredBeforeUpdate = state.getValue(POWERED);
			
			if(state.getValue(POWERED) != hasPower)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, hasPower));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
			
			if(!isPoweredBeforeUpdate && hasPower)
			{
				BlockPos facingPos = pos.relative(state.getValue(FACING));
				BlockState facingState = worldIn.getBlockState(facingPos);
				
				if(state.getValue(DISCHARGE))
				{
					discharge(worldIn, pos, state, facingPos, facingState);
				} else
				{
					toggle(worldIn, pos, state, facingPos, facingState);
				}
			}
		}
	}
	
	private void toggle(World worldIn, BlockPos pos, BlockState state, BlockPos facingPos, BlockState facingState)
	{
		if(facingState.hasProperty(MSProperties.MACHINE_TOGGLE))
		{
			worldIn.setBlock(facingPos, facingState.cycle(MSProperties.MACHINE_TOGGLE), Constants.BlockFlags.DEFAULT);
			
			if(!worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getBlock().asItem().is(ItemTags.WOOL)) //wont make a toggle sound if the toggler is "muted" by a wool block
			{
				worldIn.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, facingState.getValue(MSProperties.MACHINE_TOGGLE) ? 1.5F : 0.5F);
			}
		}
	}
	
	private void discharge(World worldIn, BlockPos pos, BlockState state, BlockPos facingPos, BlockState facingState)
	{
		boolean updated = false;
		
		//removing redstone power from powered blocks
		if(facingState.isSignalSource())
		{
			if(facingState.hasProperty(BlockStateProperties.POWERED))
			{
				worldIn.setBlock(facingPos, facingState.setValue(BlockStateProperties.POWERED, false), Constants.BlockFlags.DEFAULT);
				updated = true;
			}
			if(facingState.hasProperty(BlockStateProperties.POWER))
			{
				worldIn.setBlock(facingPos, facingState.setValue(BlockStateProperties.POWER, 0), Constants.BlockFlags.DEFAULT);
				updated = true;
			}
		}
		
		if(updated && !worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getBlock().asItem().is(ItemTags.WOOL)) //wont make a toggle sound if the toggler is "muted" by a wool block
		{
			worldIn.playSound(null, facingPos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 0.5F);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(DISCHARGE);
	}
}