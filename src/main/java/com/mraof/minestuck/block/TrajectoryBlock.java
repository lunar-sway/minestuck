package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.ComputerBlock;
import com.mraof.minestuck.tileentity.WirelessRedstoneTransmitterTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ComparatorMode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

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
		updatePower(worldIn, pos, blockState);
		double powerMod = blockState.get(POWER) / 16D + 1;
		if(entityIn instanceof PlayerEntity)
			Debug.debugf("blockState.get(POWER) = %s", blockState.get(POWER));
		//entityIn.setMotion(blockState.get(FACING).getXOffset() * powerMod, blockState.get(FACING).getYOffset() * powerMod, blockState.get(FACING).getZOffset() * powerMod);
		entityIn.setMotion(entityIn.getMotion().x / 1.2 + blockState.get(FACING).getXOffset() * powerMod, entityIn.getMotion().y / 1.2 + blockState.get(FACING).getYOffset() * powerMod, entityIn.getMotion().z / 1.2 + blockState.get(FACING).getZOffset() * powerMod);
		//entityIn.setMotion(state.get(FACING).getDirectionVec().offset(state.get(FACING), 5/*POWER*/));
		//Debug.debugf("motion AFTER = %s", entityIn.getMotion());
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updatePower(worldIn, pos, state);
	}
	
	public void updatePower(World worldIn, BlockPos pos, BlockState state)
	{
		int powerInt = worldIn.getWorld().getRedstonePowerFromNeighbors(pos);
		worldIn.setBlockState(pos, state.with(POWER, powerInt), Constants.BlockFlags.BLOCK_UPDATE);
		//worldIn.notifyBlockUpdate(pos, state, state.with(POWER, powerInt), 3);
		//Debug.debugf("blockState.get(POWER) = %s, power from neighbors = %s", state.get(POWER), powerInt);
	}
	
	/*@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return super.hasComparatorInputOverride(state);
	}
	*/
	/*private int calculateInputStrength(World worldIn, BlockPos pos, BlockState state)
	{
		int i = 0;
		//int i = super.calculateInputStrength(worldIn, pos, state);
		//Direction direction = state.get(HORIZONTAL_FACING);
		for(int directionInt = 0; directionInt < 6; directionInt++)
		{
			BlockPos blockpos = pos.offset(Direction.byIndex(directionInt));
			BlockState blockstate = worldIn.getBlockState(blockpos);
			
			blockstate = worldIn.getBlockState(blockpos);
			if(blockstate.hasComparatorInputOverride())
			{
				i = blockstate.getComparatorInputOverride(worldIn, blockpos);
			}
			/*if(blockstate.hasComparatorInputOverride())
			{
				i = blockstate.getComparatorInputOverride(worldIn, blockpos);
			} else if(i < 15 && blockstate.isNormalCube(worldIn, blockpos))
			{
				blockpos = blockpos.offset(direction);
				blockstate = worldIn.getBlockState(blockpos);
				if(blockstate.hasComparatorInputOverride())
				{
					i = blockstate.getComparatorInputOverride(worldIn, blockpos);
				} else if(blockstate.isAir(worldIn, blockpos))
				{
					ItemFrameEntity itemframeentity = this.findItemFrame(worldIn, direction, blockpos);
					if(itemframeentity != null)
					{
						i = itemframeentity.getAnalogOutput();
					}
				}
			}*//*
		}
		
		
		return i;
	}*/
	/*
	private int calculateOutput(World worldIn, BlockPos pos, BlockState state) {
		return this.calculateInputStrength(worldIn, pos, state);
	}
	
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		if(!this.isLocked(worldIn, pos, state))
		{
			boolean flag = state.get(POWERED);
			boolean flag1 = this.shouldBePowered(worldIn, pos, state);
			if(flag && !flag1)
			{
				worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(false)), 2);
			} else if(!flag)
			{
				worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(true)), 2);
				if(!flag1)
				{
					worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
				}
			}
			
		}
	}
	
	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return super.getStrongPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		if(!blockState.get(POWERED))
		{
			return 0;
		} else
		{
			return blockState.get(FACING) == side ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
		}
	}
	
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if(state.isValidPosition(worldIn, pos))
		{
			this.updateState(worldIn, pos, state);
		} else
		{
			TileEntity tileentity = state.hasTileEntity() ? worldIn.getTileEntity(pos) : null;
			spawnDrops(state, worldIn, pos, tileentity);
			worldIn.removeBlock(pos, false);
			
			for(Direction direction : Direction.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}
			
		}
	}
	
	protected void updateState(World worldIn, BlockPos pos, BlockState state)
	{
		boolean flag = state.get(POWERED);
		boolean flag1 = this.shouldBePowered(worldIn, pos, state);
		if(flag != flag1 && !worldIn.getPendingBlockTicks().isTickPending(pos, this))
		{
			TickPriority tickpriority = TickPriority.HIGH;
			if(this.isFacingTowardsRepeater(worldIn, pos, state))
			{
				tickpriority = TickPriority.EXTREMELY_HIGH;
			} else if(flag)
			{
				tickpriority = TickPriority.VERY_HIGH;
			}
			
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, state.get(), tickpriority);
		}
	}
	
	protected boolean shouldBePowered(World worldIn, BlockPos pos, BlockState state)
	{
		return this.calculateInputStrength(worldIn, pos, state) > 0;
	}
	
	protected int calculateInputStrength(World worldIn, BlockPos pos, BlockState state)
	{
		Direction direction = state.get(FACING);
		BlockPos blockpos = pos.offset(direction);
		int i = worldIn.getRedstonePower(blockpos, direction);
		if(i >= 15)
		{
			return i;
		} else
		{
			BlockState blockstate = worldIn.getBlockState(blockpos);
			return Math.max(i, blockstate.getBlock() == Blocks.REDSTONE_WIRE ? blockstate.get(RedstoneWireBlock.POWER) : 0);
		}
	}
	
	public boolean isFacingTowardsRepeater(IBlockReader worldIn, BlockPos pos, BlockState state)
	{
		Direction direction = state.get(FACING).getOpposite();
		BlockState blockstate = worldIn.getBlockState(pos.offset(direction));
		return isTrajectoryBlock(blockstate) && blockstate.get(FACING) != direction;
	}
	
	public static boolean isTrajectoryBlock(BlockState state)
	{
		return state.getBlock() instanceof RedstoneDiodeBlock;
	}
	
	protected int getActiveSignal(IBlockReader worldIn, BlockPos pos, BlockState state)
	{
		return 15;
	}
	
	//protected abstract int getDelay(BlockState state);
	
	@Override
	public void onNeighborChange(BlockState stateIn, IWorldReader world, BlockPos posIn, BlockPos neighborPos)
	{
		super.onNeighborChange(stateIn, world, posIn, neighborPos);
		
		if(world.getDimension().getWorld().getBlockState(neighborPos).getValues().get(POWER) != null) //Cannot get property IntegerProperty{name=power, clazz=class java.lang.Integer, values=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]} as it does not exist in Block{minecraft:air}
		{
			Debug.debugf("POWER = %s, neighbor get power = %s", POWER, world.getDimension().getWorld().getBlockState(neighborPos).get(POWER));
			world.getDimension().getWorld().notifyBlockUpdate(posIn, stateIn, stateIn.with(POWER, world.getDimension().getWorld().getBlockState(neighborPos).get(POWER)), 3);
		}
		
		//updatePostPlacement(stateIn, stateIn.get(FACING), world.getDimension().getWorld().getBlockState(neighborPos), world.getDimension().getWorld(), posIn, neighborPos);
		
		BlockState thisState = world.getDimension().getWorld().getBlockState(posIn);
		if(stateIn.canProvidePower())
		{
			//thisState = thisState.with(POWER, state.getComparatorInputOverride(world.getDimension().getWorld(), neighbor));
			//POWER = state.getComparatorInputOverride(world.getDimension().getWorld(), neighbor);
		}
		//state;
		//world.getRedstonePowerFromNeighbors;
		//POWER = IntegerProperty.create(POWER.getName(), )world.getBlockState(neighbor).get(POWER).;
	}
	*/
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