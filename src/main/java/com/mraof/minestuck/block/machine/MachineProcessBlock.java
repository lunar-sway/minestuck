package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.tileentity.machine.MachineProcessTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class MachineProcessBlock extends MachineBlock
{
	public MachineProcessBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof MachineProcessTileEntity)
			{
				tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler ->
				{
					for(int i = 0; i < handler.getSlots(); i++)
						InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
				});
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}
			
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
}