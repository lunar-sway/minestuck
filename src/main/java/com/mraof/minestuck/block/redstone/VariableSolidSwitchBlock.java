package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Can be right clicked without crouching to increase the power/light values the block gives off by one or right clicked with an empty hand to reduce the power/light values by one.
 * Reaching the highest or lowest value will cause it to cycle back to the opposite extreme
 */
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
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!player.isCrouching())
		{
			if(state.getValue(POWER) < 15)
			{
				level.setBlock(pos, state.setValue(POWER, state.getValue(POWER) + 1), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			} else
			{
				level.setBlock(pos, state.setValue(POWER, 0), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			}
			
			return InteractionResult.SUCCESS;
		} else if(player.isCrouching() && player.getItemInHand(hand).isEmpty())
		{
			if(state.getValue(POWER) != 0)
			{
				level.setBlock(pos, state.setValue(POWER, state.getValue(POWER) - 1), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			} else
			{
				level.setBlock(pos, state.setValue(POWER, 15), Block.UPDATE_ALL);
				level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			}
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWER) > 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWER);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(rand.nextInt(15) < stateIn.getValue(POWER))
		{
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
	}
}