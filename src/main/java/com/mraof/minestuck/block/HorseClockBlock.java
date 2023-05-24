package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import com.mraof.minestuck.block.machine.MultiMachineBlock;
import com.mraof.minestuck.blockentity.HorseClockBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * The horse clock is a multiblock clock that also gives off(all components) redstone power with a strength dependent on the clock sound it is making.
 * The bottom block has a block entity
 */
public class HorseClockBlock extends MultiMachineBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	
	public HorseClockBlock(MachineMultiblock machine, Properties properties)
	{
		super(machine, properties);
		registerDefaultState(stateDefinition.any().setValue(POWER, 0));
	}
	
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE; //The block itself does not have a texture but is dependent on the modeled block entity
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		updateSurvival(state, level, pos);
	}
	
	public static void updateSurvival(BlockState state, Level level, BlockPos pos)
	{
		if(state.is(MSBlocks.HORSE_CLOCK.BOTTOM.get()) &&
				!level.getBlockState(pos.above()).is(MSBlocks.HORSE_CLOCK.CENTER.get()))
			level.destroyBlock(pos, true);
		else if(state.is(MSBlocks.HORSE_CLOCK.CENTER.get()) &&
				(!level.getBlockState(pos.above()).is(MSBlocks.HORSE_CLOCK.TOP.get()) || !level.getBlockState(pos.below()).is(MSBlocks.HORSE_CLOCK.BOTTOM.get())))
			level.destroyBlock(pos, true);
		else if(state.is(MSBlocks.HORSE_CLOCK.TOP.get()) &&
				!level.getBlockState(pos.below()).is(MSBlocks.HORSE_CLOCK.CENTER.get()))
			level.destroyBlock(pos, true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		super.tick(state, level, pos, rand);
		
		if(state.getValue(POWER) > 0) //works to reset the power through the scheduleTick method call in the block entity
		{
			level.setBlock(pos, state.setValue(POWER, 0), Block.UPDATE_ALL_IMMEDIATE);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWER) > 0;
	}
	
	@SuppressWarnings("deprecation")
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
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
	}
	
	public static class Bottom extends HorseClockBlock implements EntityBlock
	{
		public Bottom(MachineMultiblock machine, Properties properties)
		{
			super(machine, properties);
		}
		
		@Override
		public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
		{
			if(level.getBlockEntity(pos) instanceof HorseClockBlockEntity be)
			{
				HorseClockBlockEntity.fullPower(level, be);
				
				return InteractionResult.SUCCESS;
			}
			
			return InteractionResult.PASS;
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new HorseClockBlockEntity(pos, state);
		}
		
		@Nullable
		@Override
		public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
		{
			return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.HORSE_CLOCK.get(), HorseClockBlockEntity::serverTick) : null;
		}
	}
}