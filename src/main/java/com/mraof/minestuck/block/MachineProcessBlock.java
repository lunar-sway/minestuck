package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.MachineProcessTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MachineProcessBlock extends MachineBlock
{
	public MachineProcessBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof MachineProcessTileEntity)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (MachineProcessTileEntity)tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}