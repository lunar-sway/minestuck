package com.mraof.minestuck.inventory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

import java.util.Objects;

public abstract class MachineContainer extends Container
{
	
	private final IIntArray parameters;
	@Deprecated
	public final BlockPos machinePos;	//TODO replace this by a check to the open container server-side
	protected final IWorldPosCallable position;
	
	protected MachineContainer(ContainerType<?> type, int id, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		super(type, id);
		this.machinePos = Objects.requireNonNull(machinePos);
		this.position = Objects.requireNonNull(position);
		
		checkContainerDataCount(parameters, 3);
		this.parameters = parameters;
		
		addDataSlots(parameters);
	}
	
	protected abstract Block getValidBlock();
	
	@Override
	public boolean stillValid(PlayerEntity playerIn)
	{
		return stillValid(position, playerIn, getValidBlock());
	}
	
	public void setOverrideStop(boolean value)
	{
		parameters.set(1, value ? 1 : 0);
	}
	
	public void setReady(boolean value)
	{
		parameters.set(2, value ? 1 : 0);
	}
	
	public int getProgress()
	{
		return parameters.get(0);
	}
	
	public boolean overrideStop()
	{
		return parameters.get(1) != 0;
	}
	
	public IWorldPosCallable getPosition()
	{
		return position;
	}
	
	protected static void assertItemHandlerSize(IItemHandler handler, int minSize)
	{
		if (handler.getSlots() < minSize)
			throw new IllegalArgumentException("Container size " + handler.getSlots() + " is smaller than the expected " + minSize);
	}
}