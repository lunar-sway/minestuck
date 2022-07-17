package com.mraof.minestuck.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;

import java.util.Objects;

public abstract class MachineContainer extends AbstractContainerMenu
{
	
	private final ContainerData parameters;
	@Deprecated
	public final BlockPos machinePos;	//TODO replace this by a check to the open container server-side
	protected final ContainerLevelAccess access;
	
	protected MachineContainer(MenuType<?> type, int id, ContainerData parameters, ContainerLevelAccess access, BlockPos machinePos)
	{
		super(type, id);
		this.machinePos = Objects.requireNonNull(machinePos);
		this.access = Objects.requireNonNull(access);
		
		checkContainerDataCount(parameters, 3);
		this.parameters = parameters;
		
		addDataSlots(parameters);
	}
	
	protected abstract Block getValidBlock();
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return stillValid(access, playerIn, getValidBlock());
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
	
	public ContainerLevelAccess getPosition()
	{
		return access;
	}
	
	protected static void assertItemHandlerSize(IItemHandler handler, int minSize)
	{
		if (handler.getSlots() < minSize)
			throw new IllegalArgumentException("Container size " + handler.getSlots() + " is smaller than the expected " + minSize);
	}
}