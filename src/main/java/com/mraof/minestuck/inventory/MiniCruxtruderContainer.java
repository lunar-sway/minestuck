package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.inventory.slot.OutputSlot;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MiniCruxtruderContainer extends MachineContainer
{
	
	private static final int INPUT_X = 79;
	private static final int INPUT_Y = 57;
	private static final int OUTPUT_X = 79;
	private static final int OUTPUT_Y = 19;
	
	public MiniCruxtruderContainer(int windowId, PlayerInventory inventoryPlayer, PacketBuffer buffer)
	{
		this(MSContainerTypes.MINI_CRUXTRUDER, windowId, inventoryPlayer, new ItemStackHandler(2), new IntArray(3), IWorldPosCallable.NULL, buffer.readBlockPos());
	}
	
	public MiniCruxtruderContainer(int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_CRUXTRUDER, windowId, playerInventory, inventory, parameters, position, machinePos);
	}
	
	public MiniCruxtruderContainer(ContainerType<? extends MiniCruxtruderContainer> type, int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		super(type, windowId, parameters, position, machinePos);
		
		assertItemHandlerSize(inventory, 2);
		
		addSlot(new InputSlot(inventory, 0, INPUT_X, INPUT_Y, MSItems.RAW_CRUXITE));
		addSlot(new OutputSlot(inventory, 1, OUTPUT_X, OUTPUT_Y));
		
		bindPlayerInventory(playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.MINI_CRUXTRUDER;
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
	public ItemStack quickMoveStack(PlayerEntity player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if (slot != null && slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem();
			itemstack = itemstackOrig.copy();
			boolean result = false;
			
			
			if(slotNumber <= 1)
			{
				//if it's a machine slot
				result = moveItemStackTo(itemstackOrig, 2, allSlots, false);
			} else if(slotNumber > 1)
			{
				//if it's an inventory slot with valid contents
				//Debug.print("item ID of " + itemstackOrig.itemID + ". Expected " + Minestuck.rawCruxite.itemID);
				if(itemstackOrig.getItem() == MSItems.RAW_CRUXITE)
				{
					//Debug.print("Transferring...");
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
				}
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
		}
		
		return itemstack;
	}
}