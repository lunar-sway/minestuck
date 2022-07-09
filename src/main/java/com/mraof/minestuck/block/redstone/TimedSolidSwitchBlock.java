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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		worldIn.setBlock(pos, state.setValue(POWER, state.getValue(POWER) == 0 ? 15 : 0), Constants.BlockFlags.DEFAULT);
		if(state.getValue(POWER) > 0)
		{
			worldIn.playSound(player, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, tickRate);
		} else
		{
			worldIn.playSound(player, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
		}
		
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		super.tick(state, worldIn, pos, rand);
		
		int power = state.getValue(POWER);
		
		if(power >= 1)
		{
			worldIn.setBlockAndUpdate(pos, state.setValue(POWER, power - 1));
			worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, tickRate);
			
			if(power - 1 == 0)
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			else
				worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1.2F);
		}
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, tickRate);
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWER) > 0;
	}
	
	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWER);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(15) < stateIn.getValue(POWER))
		{
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
	}
}