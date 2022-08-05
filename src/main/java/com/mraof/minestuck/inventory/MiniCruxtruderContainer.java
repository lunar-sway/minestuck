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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MiniCruxtruderContainer extends MachineContainer
{
	
	private static final int INPUT_X = 79;
	private static final int INPUT_Y = 57;
	private static final int OUTPUT_X = 79;
	private static final int OUTPUT_Y = 19;
	
	public MiniCruxtruderContainer(int windowId, Inventory inventoryPlayer, FriendlyByteBuf buffer)
	{
		this(MSContainerTypes.MINI_CRUXTRUDER, windowId, inventoryPlayer, new ItemStackHandler(2), new SimpleContainerData(3), ContainerLevelAccess.NULL, buffer.readBlockPos());
	}
	
	public MiniCruxtruderContainer(int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, ContainerLevelAccess access, BlockPos machinePos)
	{
		this(MSContainerTypes.MINI_CRUXTRUDER, windowId, playerInventory, inventory, parameters, access, machinePos);
	}
	
	public MiniCruxtruderContainer(MenuType<? extends MiniCruxtruderContainer> type, int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, ContainerLevelAccess access, BlockPos machinePos)
	{
		super(type, windowId, parameters, access, machinePos);
		
		assertItemHandlerSize(inventory, 2);
		
		addSlot(new InputSlot(inventory, 0, INPUT_X, INPUT_Y, MSItems.RAW_CRUXITE9j));
		addSlot(new OutputSlot(inventory, 1, OUTPUT_X, OUTPUT_Y));
		
		bindPlayerInventory(playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.MINI_CRUXTRUDER.get();
	}
	
	protected void bindPlayerInventory(Inventory playerInventory)
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
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if (slot.hasItem())
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
				if(itemstackOrig.getItem() == MSItems.RAW_CRUXITE9j)
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