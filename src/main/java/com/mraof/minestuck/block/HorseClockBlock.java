package com.mraof.minestuck.block;

import com.mraof.minestuck.block.machine.MachineMultiblock;
import com.mraof.minestuck.block.machine.MultiMachineBlock;
import com.mraof.minestuck.blockentity.HorseClockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class HorseClockBlock extends MultiMachineBlock implements EntityBlock
{
	public HorseClockBlock(MachineMultiblock machine, Properties properties)
	{
		super(machine, properties);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new HorseClockBlockEntity(pos, state);
	}
	
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		updateSurvival(state, level, pos);
	}
	
	public static void updateSurvival(BlockState state, Level level, BlockPos pos)
	{
		if(!level.isClientSide() && state.getBlock() instanceof HorseClockBlock)
		{
			if(state.is(MSBlocks.HORSE_CLOCK.BOTTOM.get()) &&
					!level.getBlockState(pos.above()).is(MSBlocks.HORSE_CLOCK.CENTER.get()))
			{
				breakIfPresent(level, pos, MSBlocks.HORSE_CLOCK.BOTTOM.get());
				breakIfPresent(level, pos.above(2), MSBlocks.HORSE_CLOCK.TOP.get());
			} else if(state.is(MSBlocks.HORSE_CLOCK.CENTER.get()) &&
					(!level.getBlockState(pos.above()).is(MSBlocks.HORSE_CLOCK.TOP.get()) || !level.getBlockState(pos.below()).is(MSBlocks.HORSE_CLOCK.BOTTOM.get())))
			{
				breakIfPresent(level, pos.above(), MSBlocks.HORSE_CLOCK.TOP.get());
				breakIfPresent(level, pos, MSBlocks.HORSE_CLOCK.CENTER.get());
				breakIfPresent(level, pos.below(), MSBlocks.HORSE_CLOCK.BOTTOM.get());
			} else if(state.is(MSBlocks.HORSE_CLOCK.TOP.get()) &&
					!level.getBlockState(pos.below()).is(MSBlocks.HORSE_CLOCK.CENTER.get()))
			{
				breakIfPresent(level, pos, MSBlocks.HORSE_CLOCK.TOP.get());
				breakIfPresent(level, pos.below(2), MSBlocks.HORSE_CLOCK.BOTTOM.get());
			}
		}
	}
	
	private static void breakIfPresent(Level level, BlockPos pos, Block clockBlock)
	{
		if(level.getBlockState(pos).is(clockBlock))
		{
			level.destroyBlock(pos, true);
		}
	}
	
	/*@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.AREA_EFFECT.get(), HorseClockBlockEntity::serverTick) : null;
	}*/
}