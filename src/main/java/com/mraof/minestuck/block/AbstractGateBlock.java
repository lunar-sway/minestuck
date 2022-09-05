package com.mraof.minestuck.block;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.IBlockRenderProperties;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class AbstractGateBlock extends Block
{
	
	protected static final VoxelShape SHAPE = Block.box(0.0D, 7.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	public static final BooleanProperty MAIN = MSProperties.MAIN;
	
	public AbstractGateBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(MAIN, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public void initializeClient(Consumer<IBlockRenderProperties> consumer)
	{
		consumer.accept(new IBlockRenderProperties()
		{
			@Override
			public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager)
			{
				return true;
			}
		});
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MAIN);
	}
	
	protected boolean isValid(BlockPos pos, Level level, BlockState state)
	{
		if(state.getValue(MAIN))
			return isValid(pos, level);
		else
		{
			BlockPos mainPos = findMainComponent(pos, level);
			if(mainPos != null)
				return isValid(mainPos, level);
			else return false;
		}
	}
	
	protected abstract boolean isValid(BlockPos pos, Level level);
	
	protected abstract void removePortal(BlockPos pos, Level level);
	
	@Nullable
	protected abstract BlockPos findMainComponent(BlockPos pos, Level level);
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		super.onRemove(state, level, pos, newState, isMoving);
		if(state.getValue(MAIN))
			removePortal(pos, level);
		else
		{
			BlockPos mainPos = findMainComponent(pos, level);
			if(mainPos != null)
				removePortal(mainPos, level);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if(!this.isValid(pos, level, state))
		{
			BlockPos mainPos = state.getValue(MAIN) ? pos : findMainComponent(pos, level);
			
			if(mainPos == null)
				level.removeBlock(pos, false);
			else removePortal(mainPos, level);
		}
	}
}