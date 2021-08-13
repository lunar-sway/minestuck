package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class TrajectoryBlock extends MSDirectionalBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
	
	protected TrajectoryBlock(Properties properties)
	{
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP).with(POWER, 0));
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		if(entityIn.isSuppressingBounce())
		{
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		} else
		{
			entityIn.onLivingFall(fallDistance, 0.0F);
		}
	}
	
	@Override
	public void onLanded(IBlockReader worldIn, Entity entityIn)
	{
		super.onLanded(worldIn, entityIn);
		if(entityIn.isSuppressingBounce())
		{
			super.onLanded(worldIn, entityIn);
		} else
		{
			Vec3d entityMotion = entityIn.getMotion();
			entityIn.setMotion(entityMotion.x, 0, entityMotion.z);
		}
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityWalk(worldIn, pos, entityIn);
		BlockState blockState = worldIn.getBlockState(pos);
		//Debug.debugf("motion BEFORE = %s", entityIn.getMotion());
		entityIn.onGround = false;
		double powerMod = blockState.get(POWER) / 4D + 1;
		entityIn.setMotion(entityIn.getMotion().x + blockState.get(FACING).getXOffset() * powerMod, entityIn.getMotion().y + blockState.get(FACING).getYOffset() * powerMod, entityIn.getMotion().z + blockState.get(FACING).getZOffset() * powerMod);
		//entityIn.setMotion(state.get(FACING).getDirectionVec().offset(state.get(FACING), 5/*POWER*/));
		//Debug.debugf("motion AFTER = %s", entityIn.getMotion());
	}
	
	@Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(state, world, pos, neighbor);
		//state;
		//world.getRedstonePowerFromNeighbors;
		//POWER = IntegerProperty.create(POWER.getName(), )world.getBlockState(neighbor).get(POWER).;
	}
	
	/*@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityCollision(state, worldIn, pos, entityIn);
		Debug.debugf("motion BEFORE = %s", entityIn.getMotion());
		entityIn.setMotion(state.get(FACING).getXOffset(), state.get(FACING).getYOffset(), state.get(FACING).getZOffset());
		//entityIn.setMotion(state.get(FACING).getDirectionVec().offset(state.get(FACING), 5));
		Debug.debugf("motion AFTER = %s", entityIn.getMotion());
	}*/
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWER);
	}
}