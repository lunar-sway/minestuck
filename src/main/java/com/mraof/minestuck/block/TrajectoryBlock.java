package com.mraof.minestuck.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TrajectoryBlock extends MSDirectionalBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	
	protected TrajectoryBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWER, 0));
	}
	
	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		BlockState trajectoryState = worldIn.getBlockState(pos);
		if(entityIn.isSuppressingBounce())
		{
			super.fallOn(worldIn, pos, entityIn, fallDistance);
		} else if(trajectoryState.getValue(FACING) == Direction.UP && trajectoryState.getValue(POWER) > 6)
		{
			entityIn.causeFallDamage(fallDistance, 0.0F); //reduces damage if the trajectory block faces up and is powered a reasonable amount
		}
	}
	
	@Override
	public void updateEntityAfterFallOn(IBlockReader worldIn, Entity entityIn)
	{
		if(entityIn.isSuppressingBounce())
		{
			super.updateEntityAfterFallOn(worldIn, entityIn);
		} else
		{
			Vector3d entityMotion = entityIn.getDeltaMovement();
			
			if(entityMotion.y < 0.0D)
			{
				entityIn.setDeltaMovement(entityMotion.x, -entityMotion.y * 0.1D, entityMotion.z); //intended to reset player's falling momentum
			}
		}
	}
	
	/*@Override
	public void onLanded(IBlockReader worldIn, Entity entityIn)
	{
		if(entityIn.isSuppressingBounce())
		{
			super.onLanded(worldIn, entityIn);
		} else
		{
			Vec3d entityMotion = entityIn.getMotion();
			
			if(entityMotion.y < 0.0D)
			{
				entityIn.setMotion(entityMotion.x, -entityMotion.y * 0.1D, entityMotion.z); //intended to reset player's falling momentum
			}
		}
	}*/
	
	@Override
	public void stepOn(World worldIn, BlockPos pos, Entity entityIn)
	{
		super.stepOn(worldIn, pos, entityIn);
		BlockState blockState = worldIn.getBlockState(pos);
		updatePower(worldIn, pos, blockState);
		
		if(blockState.getValue(POWER) != 0)
		{
			int power = blockState.getValue(POWER);
			double powerMod = power / 16D;
			if(!(blockState.getValue(FACING) == Direction.UP && blockState.getValue(POWER) < 7))
			{
				if(entityIn.isOnGround())
					entityIn.setOnGround(false);
				entityIn.setDeltaMovement(entityIn.getDeltaMovement().x * 0.8 + blockState.getValue(FACING).getStepX() * powerMod, entityIn.getDeltaMovement().y * 0.8 + blockState.getValue(FACING).getStepY() * powerMod, entityIn.getDeltaMovement().z * 0.8 + blockState.getValue(FACING).getStepZ() * powerMod);
			}
		}
	}
	
	/*@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityWalk(worldIn, pos, entityIn);
		BlockState blockState = worldIn.getBlockState(pos);
		updatePower(worldIn, pos, blockState);
		
		if(blockState.get(POWER) != 0)
		{
			int power = blockState.get(POWER);
			double powerMod = power / 16D;
			if(!(blockState.get(FACING) == Direction.UP && blockState.get(POWER) < 7))
			{
				if(entityIn.onGround)
					entityIn.onGround = false;
				entityIn.setMotion(entityIn.getMotion().x * 0.8 + blockState.get(FACING).getXOffset() * powerMod, entityIn.getMotion().y * 0.8 + blockState.get(FACING).getYOffset() * powerMod, entityIn.getMotion().z * 0.8 + blockState.get(FACING).getZOffset() * powerMod);
			}
		}
	}*/
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updatePower(worldIn, pos, state);
	}
	
	public void updatePower(World worldIn, BlockPos pos, BlockState state)
	{
		if(!worldIn.isClientSide)
		{
			int powerInt = worldIn.getBestNeighborSignal(pos);
			worldIn.setBlock(pos, state.setValue(POWER, powerInt), Constants.BlockFlags.NOTIFY_NEIGHBORS);
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(8) == 0 && stateIn.getValue(POWER) != 0)
		{
			double powerMod = stateIn.getValue(POWER) / 120D + 0.075;
			worldIn.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.above().getY() + 0.25, pos.getZ() + 0.5, stateIn.getValue(FACING).getStepX() * powerMod, stateIn.getValue(FACING).getStepY() * powerMod, stateIn.getValue(FACING).getStepZ() * powerMod);
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