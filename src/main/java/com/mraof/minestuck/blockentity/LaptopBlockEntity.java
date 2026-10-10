package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class LaptopBlockEntity extends ComputerBlockEntity
{
	private boolean pickedUp = false;
	
	public LaptopBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.LAPTOP.get(), pos, state);
	}
	
	public void markPickedUp()
	{
		this.pickedUp = true;
	}
	
	public boolean isPickedUp()
	{
		return pickedUp;
	}
}