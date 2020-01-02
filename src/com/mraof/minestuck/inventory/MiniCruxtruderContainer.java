package com.mraof.minestuck.inventory;

import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class MiniCruxtruderContainer extends MachineContainer
{
	
	private static final int INPUT_X = 79;
	private static final int INPUT_Y = 57;
	private static final int OUTPUT_X = 79;
	private static final int OUTPUT_Y = 19;
	
	private final IInventory cruxtruderInventory;
	
	public MiniCruxtruderContainer(int windowId, PlayerInventory inventoryPlayer, PacketBuffer buffer)
	{
		this(MSContainerTypes.MINI_CRUXTRUDER, windowId, inventoryPlayer, new Inventory(2), new IntArray(3), buffer.readBlockPos());
	}
	
	public MiniCruxtruderContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_CRUXTRUDER, windowId, playerInventory, inventory, parameters, machinePos);
	}
	
	public MiniCruxtruderContainer(ContainerType<? extends MiniCruxtruderContainer> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters, BlockPos machinePos)
	{
		super(type, windowId, parameters, machinePos);
		
		assertInventorySize(inventory, 2);
		this.cruxtruderInventory = inventory;
		
		addSlot(new InputSlot(inventory, 0, INPUT_X, INPUT_Y, MSItems.RAW_CRUXITE));
		addSlot(new OutputSlot(inventory, 1, OUTPUT_X, OUTPUT_Y));
		
		bindPlayerInventory(playerInventory);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return cruxtruderInventory.isUsableByPlayer(player);
	}
	
	protected void bindPlayerInventory(PlayerInventory playerInventory)
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlot(new Slot(playerInventory, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
		
		for (int i = 0; i < 9; i++)
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotNumber);
		int allSlots = this.inventorySlots.size();
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstackOrig = slot.getStack();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			
			if(slotNumber <= 1)
			{
				//if it's a machine slot
				result = mergeItemStack(itemstackOrig, 2, allSlots, false);
			} else if(slotNumber > 1)
			{
				//if it's an inventory slot with valid contents
				//Debug.print("item ID of " + itemstackOrig.itemID + ". Expected " + Minestuck.rawCruxite.itemID);
				if(itemstackOrig.getItem() == MSItems.RAW_CRUXITE)
				{
					//Debug.print("Transferring...");
					result = mergeItemStack(itemstackOrig, 0, 1, false);
				}
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
}