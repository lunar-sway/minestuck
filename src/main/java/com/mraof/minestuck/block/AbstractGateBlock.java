package com.mraof.minestuck.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractGateBlock extends Block implements LiquidBlockContainer
{
	protected static final VoxelShape SHAPE = Block.box(0.0D, 7.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	
	public AbstractGateBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Override
	protected RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}
	
	protected abstract boolean isValid(BlockPos pos, Level level);
	
	protected abstract void removePortal(BlockPos pos, Level level);
	
	@Nullable
	protected abstract BlockPos findMainComponent(BlockPos pos, Level level);
	
	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		BlockPos mainPos = findMainComponent(pos, level);
		if(mainPos == null)
			level.removeBlock(pos, false);
		else if(!this.isValid(mainPos, level))
			removePortal(mainPos, level);
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		super.onRemove(state, level, pos, newState, isMoving);
		BlockPos mainPos = findMainComponent(pos, level);
		if(mainPos != null)
			removePortal(mainPos, level);
	}
	
	@Override
	protected boolean canBeReplaced(BlockState state, Fluid fluid)
	{
		return false;
	}
	
	// Implement LiquidBlockContainer and explicitly disallow fluids to get FlowingFluid.canHoldFluid(...) to return false.
	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid)
	{
		return false;
	}
	
	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState)
	{
		return false;
	}
}
