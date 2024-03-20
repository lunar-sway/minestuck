package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;

/**
 * When not crouching, players are subjected to movement by the block when walking on top of them. In addition, fall damage is cancelled when entities land on a upward facing trajectory block of more than power 6.
 * While players can avoid the effects by crouching, while they are subject to them, they have limited movement of their own and cannot jump
 */
public class TrajectoryBlock extends MSDirectionalBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED; //used for texture purposes
	
	private static final int UPWARDS_POWER_MIN = 7;
	
	protected TrajectoryBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWER, 0).setValue(POWERED, false));
	}
	
	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance)
	{
		BlockState trajectoryState = level.getBlockState(pos);
		if(trajectoryState.getValue(FACING) == Direction.UP && trajectoryState.getValue(POWER) >= UPWARDS_POWER_MIN)
		{
			entityIn.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall()); //reduces damage if the trajectory block faces up and is powered a reasonable amount
		}
		else
			super.fallOn(level, state, pos, entityIn, fallDistance);
	}
	
	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entityIn)
	{
		super.stepOn(level, pos, state, entityIn);
		BlockState blockState = level.getBlockState(pos);
		updatePower(level, pos);
		
		int power = blockState.getValue(POWER);
		double powerMod = power / 16D;
		
		if(power != 0 && !(blockState.getValue(FACING) == Direction.UP && power < UPWARDS_POWER_MIN) && !entityIn.isShiftKeyDown())
		{
			if(entityIn.onGround())
				entityIn.setOnGround(false);
			entityIn.setDeltaMovement(entityIn.getDeltaMovement().x * 0.8 + blockState.getValue(FACING).getStepX() * powerMod, entityIn.getDeltaMovement().y * 0.8 + blockState.getValue(FACING).getStepY() * powerMod, entityIn.getDeltaMovement().z * 0.8 + blockState.getValue(FACING).getStepZ() * powerMod);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
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
			BlockState oldState = level.getBlockState(pos);
			int newPower = level.getBestNeighborSignal(pos);
			
			BlockState newState = setPower(oldState, newPower);
			if(oldState != newState)
				level.setBlockAndUpdate(pos, newState);
		}
	}
	
	public static BlockState setPower(BlockState state, int newPower)
	{
		return state.setValue(POWER, newPower).setValue(POWERED, newPower > 0);
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(stateIn.getValue(POWER) != 0 && rand.nextInt(10 - (stateIn.getValue(POWER) + 1) / 4) == 0) //at max power nextInt(6) == 0, at 1 to 4 power nextInt(9) == 0. More frequent at higher power
		{
			double powerMod = stateIn.getValue(POWER) / 120D + 0.075;
			level.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.above().getY() + 0.25, pos.getZ() + 0.5, stateIn.getValue(FACING).getStepX() * powerMod, stateIn.getValue(FACING).getStepY() * powerMod, stateIn.getValue(FACING).getStepZ() * powerMod);
		}
	}
	
	/**
	 * Helps entities avoid these blocks if possible should the blocks
	 */
	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity)
	{
		if((state.getValue(POWER) >= UPWARDS_POWER_MIN && state.getValue(FACING) == Direction.UP) || (state.getValue(POWER) > 0 && state.getValue(FACING) != Direction.UP))
			return BlockPathTypes.DANGER_OTHER;
		else
			return BlockPathTypes.WALKABLE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
		builder.add(POWERED);
	}
}