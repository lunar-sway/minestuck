package com.mraof.minestuck.inventory;

import com.mraof.minestuck.blockentity.machine.ProgressTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Objects;

public abstract class MachineContainerMenu extends AbstractContainerMenu
{
	
	private final ContainerData parameters;
	@Deprecated
	public final BlockPos machinePos;	//TODO replace this by a check to the open container menu server-side
	protected final ContainerLevelAccess access;
	
	protected MachineContainerMenu(MenuType<?> type, int id, ContainerData parameters, ContainerLevelAccess access, BlockPos machinePos)
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
	
	public void setIsLooping(boolean value)
	{
		parameters.set(ProgressTracker.LOOPING_INDEX, value ? 1 : 0);
	}
	
	public void setShouldRun(boolean value)
	{
		parameters.set(ProgressTracker.RUN_INDEX, value ? 1 : 0);
	}
	
	public int getProgress()
	{
		return parameters.get(ProgressTracker.PROGRESS_INDEX);
	}
	
	public boolean isLooping()
	{
		return parameters.get(ProgressTracker.LOOPING_INDEX) != 0;
	}
	
	public boolean isRunning()
	{
		return this.parameters.get(ProgressTracker.RUN_INDEX) != 0;
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