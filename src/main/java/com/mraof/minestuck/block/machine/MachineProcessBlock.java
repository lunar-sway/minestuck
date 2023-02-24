package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public abstract class MachineProcessBlock extends MachineBlock
{
	public MachineProcessBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if(blockEntity instanceof MachineProcessBlockEntity)
			{
				blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler ->
				{
					for(int i = 0; i < handler.getSlots(); i++)
						Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
				});
				level.updateNeighbourForOutputSignal(pos, this);
			}
			
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createMachineTicker(Level level, BlockEntityType<T> placedType, BlockEntityType<? extends MachineProcessBlockEntity> blockType) {
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, blockType, MachineProcessBlockEntity::serverTick) : null;
	}
}