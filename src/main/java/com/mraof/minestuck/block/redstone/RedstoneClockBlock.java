package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.RedstoneClockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Generates a redstone pulse through its block entity at a modifiable increment. Used as a compact means of creating redstone clocks
 */
public class RedstoneClockBlock extends MSDirectionalBlock implements EntityBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public RedstoneClockBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(level.getBlockEntity(pos) instanceof RedstoneClockBlockEntity be) //does not check for creative shock to anticipate use cases in timing puzzle designs
		{
			
			if(!player.isCrouching())
			{
				be.incrementClockSpeed(player);
				
				return InteractionResult.SUCCESS;
			} else
			{
				be.decrementClockSpeed(player);
				
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.PASS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		super.tick(state, level, pos, rand);
		
		if(state.getValue(POWERED))
		{
			level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_ALL_IMMEDIATE);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneClockBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.REDSTONE_CLOCK.get(), RedstoneClockBlockEntity::serverTick) : null;
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(stateIn.getValue(POWERED))
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
	}
}
