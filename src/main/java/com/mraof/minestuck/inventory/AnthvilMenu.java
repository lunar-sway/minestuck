package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AnthvilMenu extends MachineContainerMenu
{
	private static final int cruxiteInputX = 95;
	private static final int cruxiteInputY = 54;
	private static final int uraniumInputX = 145;
	private static final int uraniumInputY = 54;
	private static final int itemInputX = 15;
	private static final int itemInputY = 50;
	
	private final DataSlot fuelHolder;
	private final DataSlot cruxiteHolder;
	
	public AnthvilMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		this(MSMenuTypes.ANTHVIL.get(), windowId, playerInventory, new ItemStackHandler(3), new SimpleContainerData(3), DataSlot.standalone(), DataSlot.standalone(), ContainerLevelAccess.NULL, buffer.readBlockPos());
	}
	
	public AnthvilMenu(int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, DataSlot fuelHolder, DataSlot cruxiteHolder, ContainerLevelAccess access, BlockPos machinePos)
	{
		this(MSMenuTypes.ANTHVIL.get(), windowId, playerInventory, inventory, parameters, fuelHolder, cruxiteHolder, access, machinePos);
	}
	
	public AnthvilMenu(MenuType<? extends AnthvilMenu> type, int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, DataSlot fuelHolder, DataSlot cruxiteHolder, ContainerLevelAccess access, BlockPos machinePos)
	{
		super(type, windowId, parameters, access, machinePos);
		
		assertItemHandlerSize(inventory, 3);
		this.fuelHolder = fuelHolder;
		this.cruxiteHolder = cruxiteHolder;
		
		addSlot(new SlotItemHandler(inventory, 0, itemInputX, itemInputY));
		addSlot(new InputSlot(inventory, 1, uraniumInputX, uraniumInputY, MSItems.RAW_URANIUM.get()));
		addSlot(new InputSlot(inventory, 2, cruxiteInputX, cruxiteInputY, MSItems.RAW_CRUXITE.get()));
		addDataSlot(fuelHolder);
		addDataSlot(cruxiteHolder);
		
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.ANTHVIL.get();
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
			} else if(slotNumber == 2)    //Shift-clicking from the cruxite input
			{
				result = moveItemStackTo(itemstackOrig, 3, allSlots, false);    //Send into the inventory
			} else    //Shift-clicking from the inventory
			{
				if(itemstackOrig.getItem() == MSItems.RAW_CRUXITE.get())
				{
					result = moveItemStackTo(itemstackOrig, 2, 3, false);    //Send the cruxite to the cruxite input
				} else if(itemstackOrig.getItem() == MSItems.RAW_URANIUM.get())
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
	
	public int getCruxite()
	{
		return cruxiteHolder.get();
	}
}