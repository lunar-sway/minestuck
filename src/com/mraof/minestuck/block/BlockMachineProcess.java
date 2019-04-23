package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityMachineProcess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMachineProcess extends BlockMachine
{
	public BlockMachineProcess(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityMachineProcess)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityMachineProcess)tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}