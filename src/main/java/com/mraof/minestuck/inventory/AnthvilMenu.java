package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AnthvilMenu extends AbstractContainerMenu
{
	private static final int uraniumInputX = 143;
	private static final int uraniumInputY = 54;
	private static final int itemInputX = 81;
	private static final int itemInputY = 35;
	
	private final DataSlot fuelHolder;
	private final ContainerLevelAccess levelAccess;
	
	public AnthvilMenu(int windowId, Inventory playerInventory)
	{
		this(MSMenuTypes.ANTHVIL.get(), windowId, playerInventory, new ItemStackHandler(2), DataSlot.standalone(), ContainerLevelAccess.NULL);
	}
	
	public AnthvilMenu(int windowId, Inventory playerInventory, IItemHandler inventory, DataSlot fuelHolder, ContainerLevelAccess access)
	{
		this(MSMenuTypes.ANTHVIL.get(), windowId, playerInventory, inventory, fuelHolder, access);
	}
	
	public AnthvilMenu(MenuType<? extends AnthvilMenu> type, int windowId, Inventory playerInventory, IItemHandler inventory, DataSlot fuelHolder, ContainerLevelAccess access)
	{
		super(type, windowId);
		
		this.levelAccess = access;
		
		MachineContainerMenu.assertItemHandlerSize(inventory, 2);
		this.fuelHolder = fuelHolder;
		
		addSlot(new SlotItemHandler(inventory, 0, itemInputX, itemInputY));
		addSlot(new InputSlot(inventory, 1, uraniumInputX, uraniumInputY, MSItems.RAW_URANIUM.get()));
		addDataSlot(fuelHolder);
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if(slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem().copy();
			itemstack = itemstackOrig.copy();
			boolean result;
			
			if(slotNumber == 0)    //Shift-clicking from the item input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
				
			} else if(slotNumber == 1)    //Shift-clicking from the uranium input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
			} else    //Shift-clicking from the inventory
			{
				if(itemstackOrig.getItem() == MSItems.RAW_URANIUM.get())
				{
					result = moveItemStackTo(itemstackOrig, 1, 2, false);    //Send the uranium to the uranium input
				} else
				{
					result = moveItemStackTo(itemstackOrig, 0, 1, false);    //Send the non-uranium to the other input
				}
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!ItemStack.matches(itemstackOrig, slot.getItem()))
				slot.set(itemstackOrig);
		}
		
		return itemstack;
	}
	
	@Override
	public boolean stillValid(Player player)
	{
		return stillValid(levelAccess, player, MSBlocks.ANTHVIL.get());
	}
	
	public ContainerLevelAccess getPosition()
	{
		return levelAccess;
	}
	
	public int getFuel()
	{
		return fuelHolder.get();
	}
}