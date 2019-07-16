package com.mraof.minestuck.inventory;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class MachineContainer extends Container
{
	
	private final IIntArray parameters;
	
	public MachineContainer(ContainerType<?> type, int id, IIntArray parameters)
	{
		super(type, id);
		
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
	
	@OnlyIn(Dist.CLIENT)
	public int getProgress()
	{
		return parameters.get(0);
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean overrideStop()
	{
		return parameters.get(1) != 0;
	}
}