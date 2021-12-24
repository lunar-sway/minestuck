package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class VariableSolidSwitchBlock extends Block
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	
	public VariableSolidSwitchBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWER, 0));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isCrouching())
		{
			if(state.getValue(POWER) != 15)
			{
				worldIn.setBlock(pos, state.setValue(POWER, state.getValue(POWER) + 1), 2);
				worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			} else
			{
				worldIn.setBlock(pos, state.setValue(POWER, 0), 2);
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			}
			
			return ActionResultType.SUCCESS;
		} else if(player.isCrouching() && player.getItemInHand(hand).isEmpty())
		{
			if(state.getValue(POWER) != 0)
			{
				worldIn.setBlock(pos, state.setValue(POWER, state.getValue(POWER) - 1), 2);
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			} else
			{
				worldIn.setBlock(pos, state.setValue(POWER, 15), 2);
				worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			}
			
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWER) > 0;
	}
	
	/*@Override
	public boolean canProvidePower(BlockState state)
	{
		return state.get(POWERED);
	}*/
	
	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWER);
	}
	
	/*@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.get(POWER);
	}*/
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
	{
		return state.getValue(POWER);
	}
	
	/*@Override
	public int getLightValue(BlockState state)
	{
		return state.get(POWER);
	}*/
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWER) > 0)
		{
			if(rand.nextInt(16 - stateIn.getValue(POWER)) == 0)
				ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
	}
	
	/*@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWER);
	}*/
}