package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.RemoteComparatorBlockEntity;
import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Will send out a redstone signal on its sides if the block it is observing(of a distance set by DISTANCE_1_16) matches the block directly behind it.
 * Checks if the blockstates match as well if CHECK_STATE is true.
 */
public class RemoteComparatorBlock extends MSDirectionalBlock implements EntityBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty CHECK_STATE = MSProperties.MACHINE_TOGGLE;
	public static final IntegerProperty DISTANCE_1_16 = MSProperties.DISTANCE_1_16;
	
	public RemoteComparatorBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(DISTANCE_1_16, 1).setValue(CHECK_STATE, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			if(!player.isCrouching())
			{
				if(state.getValue(DISTANCE_1_16) != 16) //increases property until it gets to the highest value at which it resets
				{
					level.setBlock(pos, state.setValue(DISTANCE_1_16, state.getValue(DISTANCE_1_16) + 1), Block.UPDATE_ALL);
					level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
				} else
				{
					level.setBlock(pos, state.setValue(DISTANCE_1_16, 1), Block.UPDATE_ALL);
					level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
				}
				
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else
			{
				level.setBlock(pos, state.cycle(CHECK_STATE), Block.UPDATE_ALL);
				if(state.getValue(CHECK_STATE))
					level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, 1.5F);
				else
					level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, 0.5F);
				
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		
		return InteractionResult.PASS;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RemoteComparatorBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.REMOTE_COMPARATOR.get(), RemoteComparatorBlockEntity::serverTick) : null;
	}
	
	public static boolean isMatch(Level level, BlockPos comparatorPos)
	{
		BlockState comparatorState = level.getBlockState(comparatorPos);
		BlockPos posToCheck = comparatorPos.relative(comparatorState.getValue(FACING), comparatorState.getValue(DISTANCE_1_16));
		
		if(level.isAreaLoaded(posToCheck, 0) && level.isAreaLoaded(comparatorPos, 1))
		{
			BlockState checkingState = level.getBlockState(posToCheck);
			BlockState referenceState = level.getBlockState(comparatorPos.relative(comparatorState.getValue(FACING).getOpposite())); //state behind the comparator
			
			return comparatorState.getValue(CHECK_STATE) ? checkingState == referenceState : checkingState.getBlock() == referenceState.getBlock();
		} else
			return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return true;
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
		builder.add(CHECK_STATE);
		builder.add(DISTANCE_1_16);
	}
}