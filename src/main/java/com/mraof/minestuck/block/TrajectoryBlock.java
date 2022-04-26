package com.mraof.minestuck.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

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
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		BlockState trajectoryState = worldIn.getBlockState(pos);
		if(trajectoryState.getValue(FACING) == Direction.UP && trajectoryState.getValue(POWER) >= UPWARDS_POWER_MIN)
		{
			entityIn.causeFallDamage(fallDistance, 0.0F); //reduces damage if the trajectory block faces up and is powered a reasonable amount
		}
		else
			super.fallOn(worldIn, pos, entityIn, fallDistance);
	}
	
	@Override
	public void stepOn(World worldIn, BlockPos pos, Entity entityIn)
	{
		super.stepOn(worldIn, pos, entityIn);
		BlockState blockState = worldIn.getBlockState(pos);
		updatePower(worldIn, pos);
		
		int power = blockState.getValue(POWER);
		double powerMod = power / 16D;
		
		if(power != 0 && !(blockState.getValue(FACING) == Direction.UP && power < UPWARDS_POWER_MIN))
		{
			if(entityIn.isOnGround())
				entityIn.setOnGround(false);
			entityIn.setDeltaMovement(entityIn.getDeltaMovement().x * 0.8 + blockState.getValue(FACING).getStepX() * powerMod, entityIn.getDeltaMovement().y * 0.8 + blockState.getValue(FACING).getStepY() * powerMod, entityIn.getDeltaMovement().z * 0.8 + blockState.getValue(FACING).getStepZ() * powerMod);
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
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
			int powerInt = worldIn.getBestNeighborSignal(pos);
			if(state.getValue(POWER) != powerInt)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWER, powerInt));
			
			if(state.getValue(POWERED) != powerInt > 0)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, powerInt > 0));
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWER) != 0 && rand.nextInt(10 - (stateIn.getValue(POWER) + 1) / 4) == 0) //at max power nextInt(6) == 0, at 1 to 4 power nextInt(9) == 0. More frequent at higher power
		{
			double powerMod = stateIn.getValue(POWER) / 120D + 0.075;
			worldIn.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.above().getY() + 0.25, pos.getZ() + 0.5, stateIn.getValue(FACING).getStepX() * powerMod, stateIn.getValue(FACING).getStepY() * powerMod, stateIn.getValue(FACING).getStepZ() * powerMod);
		}
	}
	
	/**
	 * Helps entities avoid these blocks if possible should the blocks
	 */
	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity)
	{
		if((state.getValue(POWER) >= UPWARDS_POWER_MIN && state.getValue(FACING) == Direction.UP) || (state.getValue(POWER) > 0 && state.getValue(FACING) != Direction.UP))
			return PathNodeType.DANGER_OTHER;
		else
			return PathNodeType.WALKABLE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
		builder.add(POWERED);
	}
}