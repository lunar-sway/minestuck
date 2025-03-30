package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import javax.annotation.Nonnull;

public class UraniumCookerMenu extends MachineContainerMenu
{
	
	private static final int uraniumInputX = 38;
	private static final int uraniumInputY = 51;
	private static final int itemInputX = 38;
	private static final int itemInputY = 22;
	private static final int itemOutputX = 117;
	private static final int itemOutputY = 35;
	
	private final DataSlot fuelHolder;
	
	public UraniumCookerMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		this(MSMenuTypes.URANIUM_COOKER.get(), windowId, playerInventory, new ItemStackHandler(3), new SimpleContainerData(3), DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
	}
	
	public UraniumCookerMenu(int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, DataSlot fuelHolder, ContainerLevelAccess access, BlockPos machinePos)
	{
		this(MSMenuTypes.URANIUM_COOKER.get(), windowId, playerInventory, inventory, parameters, fuelHolder, access, machinePos);
	}
	
	public UraniumCookerMenu(MenuType<? extends UraniumCookerMenu> type, int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, DataSlot fuelHolder, ContainerLevelAccess access, BlockPos machinePos)
	{
		super(type, windowId, parameters, access, machinePos);
		
		assertItemHandlerSize(inventory, 3);
		this.fuelHolder = fuelHolder;
		
		addSlot(new SlotItemHandler(inventory, 0, itemInputX, itemInputY));
		addSlot(new InputSlot(inventory, 1, uraniumInputX, uraniumInputY, MSItems.RAW_URANIUM.get()));
		addSlot(new OutputSlot(inventory, 2, itemOutputX, itemOutputY));
		addDataSlot(fuelHolder);
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.URANIUM_COOKER.get();
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if (slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem().copy();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			if(slotNumber == 0)    //Shift-clicking from the item input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
				
			} else if(slotNumber == 1)    //Shift-clicking from the Uranium input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
			} else if(slotNumber == 2)    //Shift-clicking from the output slot
			{
				if(itemstackOrig.getItem() == MSItems.RAW_URANIUM.get())
					result = moveItemStackTo(itemstackOrig, 0, 1, false);    //Send the uranium back to the uranium input
				else
					result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send the non-uranium to the inventory
				
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
	
	public int getFuel()
	{
		return fuelHolder.get();
	}
}