package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.RemoteObserverTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RemoteObserverBlock extends Block
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public RemoteObserverBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isSneaking() && (!player.isPotionActive(MSEffects.CREATIVE_SHOCK.get()) || player.isCreative()))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof RemoteObserverTileEntity)
			{
				RemoteObserverTileEntity te = (RemoteObserverTileEntity) tileEntity;
				
				MSScreenFactories.displayRemoteObserverScreen(te);
			}
		}
		
		return ActionResultType.SUCCESS;
	}
	
	/*@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		super.tick(state, worldIn, pos, rand);
		if(!worldIn.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		
		Debug.debugf("RemoteObserverBlock tick");
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof RemoteObserverTileEntity)
		{
			RemoteObserverTileEntity te = (RemoteObserverTileEntity) tileEntity;
			Debug.debugf("stored tick = %s", te.getStoredTickForCycle());
			if(te.getStoredTickForCycle() % 10 == 1)
			{
				//checkRelaventType(pos, te, te.getActiveType());
				te.setStoredTickForCycle(1);
			} else
				te.setStoredTickForCycle(te.getStoredTickForCycle() + 1);
		}
	}
	
	@Override
	public int tickRate(IWorldReader worldIn)
	{
		return 10;
	}*/
	
	
	
	/*@Override
	public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags)
	{
		Debug.debugf("remote observer updateNeighbors");
		super.updateNeighbors(stateIn, worldIn, pos, flags);
		
		for(int i = 0; i < 4; i++) //TODO I want this to update the neighbors for redstone, but I might need those neighbor blocks themselves to get updated
		{
			worldIn.notifyNeighbors(pos.offset(Direction.byHorizontalIndex(i)), stateIn.getBlock());
		}
		worldIn.notifyNeighbors(pos.down(), stateIn.getBlock());
	}*/
	
	/*@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof RemoteObserverTileEntity)
		{
			return ((RemoteObserverTileEntity) tileentity).getActiveBoolean() ? 15 : 0;
		}
		
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}*/
	
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
		return new RemoteObserverTileEntity();
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWERED);
	}
}