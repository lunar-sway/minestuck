package com.mraof.minestuck.inventory;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public abstract class MachineContainer extends Container
{
	
	private final IIntArray parameters;
	public final BlockPos machinePos;
	
	public MachineContainer(ContainerType<?> type, int id, IIntArray parameters, BlockPos machinePos)
	{
		super(type, id);
		this.machinePos = Objects.requireNonNull(machinePos);
		
		assertIntArraySize(parameters, 3);
		this.parameters = parameters;
		
		trackIntArray(parameters);
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
}