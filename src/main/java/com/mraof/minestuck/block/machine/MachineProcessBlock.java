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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

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
			@SuppressWarnings("DataFlowIssue")
			IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, null, null);
			
			if(itemHandler != null)
			{
				for(int i = 0; i < itemHandler.getSlots(); i++)
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
			}
			level.updateNeighbourForOutputSignal(pos, this);
			
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createMachineTicker(Level level, BlockEntityType<T> placedType, BlockEntityType<? extends MachineProcessBlockEntity> blockType) {
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, blockType, MachineProcessBlockEntity::serverTick) : null;
	}
}