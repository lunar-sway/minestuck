package com.mraof.minestuck.block.redstone;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneReceiverBlockEntity;
import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
 * Receives redstone inputs from wireless redstone transmitters. Has a blockstate that allows the receiver to retain the strongest redstone signal it has received
 */
public class WirelessRedstoneReceiverBlock extends HorizontalDirectionalBlock implements EntityBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED; //used for texture purposes
	public static final BooleanProperty AUTO_RESET = MSProperties.MACHINE_TOGGLE;
	
	public static final String NOW_AUTO = "minestuck.receiver_now_auto_reset";
	public static final String NOW_NOT_AUTO = "minestuck.receiver_now_not_auto_reset";
	
	public WirelessRedstoneReceiverBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWER, 0).setValue(POWERED, false).setValue(AUTO_RESET, true).setValue(FACING, Direction.NORTH));
	}
	
	@Override
	protected MapCodec<WirelessRedstoneReceiverBlock> codec()
	{
		return null; //todo
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new WirelessRedstoneReceiverBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.WIRELESS_REDSTONE_RECEIVER.get(), WirelessRedstoneReceiverBlockEntity::serverTick) : null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			level.setBlock(pos, state.cycle(AUTO_RESET), Block.UPDATE_ALL);
			if(state.getValue(AUTO_RESET))
			{
				if(!level.isClientSide)
					player.sendSystemMessage(Component.translatable(NOW_NOT_AUTO));
				level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			} else
			{
				if(!level.isClientSide)
					player.sendSystemMessage(Component.translatable(NOW_AUTO));
				level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			}
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	public void updatePower(Level level, BlockPos posIn, BlockPos transmitterPos)
	{
		BlockState receiverState = level.getBlockState(posIn);
		int newPower = level.getBlockState(transmitterPos).getValue(WirelessRedstoneTransmitterBlock.POWER);
		
		BlockState newState = setPower(receiverState, newPower);
		if(receiverState != newState)
			level.setBlockAndUpdate(posIn, newState);
		
		if(level.getBlockEntity(posIn) instanceof WirelessRedstoneReceiverBlockEntity be)
		{
			be.setLastTransmitterBlockPos(transmitterPos);
		}
	}
	
	public static BlockState setPower(BlockState state, int newPower)
	{
		return state.setValue(POWER, newPower).setValue(POWERED, newPower > 0);
	}
	
	@Override
	public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWER);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(rand.nextInt(15) < stateIn.getValue(POWER))
		{
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
		builder.add(POWERED);
		builder.add(AUTO_RESET);
		builder.add(FACING);
	}
}