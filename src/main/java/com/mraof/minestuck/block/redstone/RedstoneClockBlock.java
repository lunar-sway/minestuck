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
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Generates a redstone pulse through its tile entity at a modifiable increment. Used as a compact means of creating redstone clocks
 */
public class RedstoneClockBlock extends MSDirectionalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public RedstoneClockBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		if(tileEntity instanceof RedstoneClockTileEntity) //does not check for creative shock to anticipate use cases in timing puzzle designs
		{
			RedstoneClockTileEntity te = (RedstoneClockTileEntity) tileEntity;
			
			if(!player.isCrouching())
			{
				te.incrementClockSpeed(player);
				
				return ActionResultType.SUCCESS;
			} else if(player.isCrouching() && player.getItemInHand(hand).isEmpty())
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
		
		if(state.getValue(POWERED))
		{
			worldIn.setBlock(pos, state.setValue(POWERED, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
		}
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
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
		if(stateIn.getValue(POWERED))
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
	}
}
