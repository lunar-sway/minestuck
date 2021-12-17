package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.tileentity.redstone.RedstoneClockTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class RedstoneClockBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public RedstoneClockBlock(Properties properties)
	{
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(POWERED, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof RedstoneClockTileEntity)
		{
			RedstoneClockTileEntity te = (RedstoneClockTileEntity) tileEntity;
			
			if(!player.isSneaking())
			{
				te.incrementClockSpeed(player);
				
				return ActionResultType.SUCCESS;
			} else if(player.isSneaking() && player.getHeldItem(hand).isEmpty())
			{
				te.decrementClockSpeed(player);
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		super.tick(state, worldIn, pos, rand);
		
		if(state.get(POWERED))
		{
			worldIn.setBlockState(pos, state.with(POWERED, false));
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof RedstoneClockTileEntity)
		{
			RedstoneClockTileEntity te = (RedstoneClockTileEntity) tileEntity;
			
			if(te.getClockSpeed() == 0)
				te.setClockSpeed(60);
		}
	}
	
	@Override
	public boolean canProvidePower(BlockState state)
	{
		return state.get(POWERED);
	}
	
	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.get(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new RedstoneClockTileEntity();
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.get(POWERED))
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE_DUST);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWERED);
	}
}
