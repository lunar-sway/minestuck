package com.mraof.minestuck.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
		if(entityIn.isSuppressingBounce())
		{
			super.onLanded(worldIn, entityIn);
		} else
		{
			Vec3d entityMotion = entityIn.getMotion();
			
			if(entityMotion.y < 0.0D)
			{
				entityIn.setMotion(entityMotion.x, -entityMotion.y * 0.1D, entityMotion.z);
			}
		}
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityWalk(worldIn, pos, entityIn);
		BlockState blockState = worldIn.getBlockState(pos);
		//entityIn.onGround = false;
		updatePower(worldIn, pos, blockState);
		
		if(blockState.get(POWER) != 0)
		{
			double powerMod = blockState.get(POWER) / 16D;
			entityIn.setMotion(entityIn.getMotion().x + blockState.get(FACING).getXOffset() * powerMod, entityIn.getMotion().y + blockState.get(FACING).getYOffset() * powerMod, entityIn.getMotion().z + blockState.get(FACING).getZOffset() * powerMod);
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updatePower(worldIn, pos, state);
	}
	
	public void updatePower(World worldIn, BlockPos pos, BlockState state)
	{
		if(!worldIn.isRemote)
		{
			int powerInt = worldIn.getWorld().getRedstonePowerFromNeighbors(pos);
			worldIn.setBlockState(pos, state.with(POWER, powerInt), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWER);
	}
}