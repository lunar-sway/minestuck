package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
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
 * When right clicked without crouching(and is unpowered), the block turns to full power and brightness and then loses one value of power and light at a set rate of ticks determined by the "tickrate" of the block
 */
public class TimedSolidSwitchBlock extends Block
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	
	private final int tickRate;
	
	public TimedSolidSwitchBlock(Properties properties, int tickRate)
	{
		super(properties);
		this.tickRate = tickRate;
		registerDefaultState(stateDefinition.any().setValue(POWER, 0));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		level.setBlock(pos, state.setValue(POWER, state.getValue(POWER) == 0 ? 15 : 0), Block.UPDATE_ALL);
		if(state.getValue(POWER) > 0)
		{
			level.playSound(player, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			level.scheduleTick(new BlockPos(pos), this, tickRate);
		} else
		{
			level.playSound(player, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
		}
		
		return InteractionResult.SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		super.tick(state, level, pos, rand);
		
		int power = state.getValue(POWER);
		
		if(power >= 1)
		{
			level.setBlockAndUpdate(pos, state.setValue(POWER, power - 1));
			level.scheduleTick(new BlockPos(pos), this, tickRate);
			
			if(power - 1 == 0)
				level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			else
				level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, 1.2F);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		level.scheduleTick(new BlockPos(pos), this, tickRate);
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWER) > 0;
	}
	
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