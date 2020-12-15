package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
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
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nonnull;

public class GristWidgetContainer extends MachineContainer
{
	
	private static final int gristWidgetInputX = 27;
	private static final int gristWidgetInputY = 20;
	
	public GristWidgetContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		this(MSContainerTypes.GRIST_WIDGET, windowId, playerInventory, new ItemStackHandler(1), new IntArray(3), IWorldPosCallable.DUMMY, buffer.readBlockPos());
	}
	
	public GristWidgetContainer(int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		this(MSContainerTypes.GRIST_WIDGET, windowId, playerInventory, inventory, parameters, position, machinePos);
	}
	
	public GristWidgetContainer(ContainerType<? extends GristWidgetContainer> type, int windowId, PlayerInventory playerInventory, IItemHandler inventory, IIntArray parameters, IWorldPosCallable position, BlockPos machinePos)
	{
		super(type, windowId, parameters, position, machinePos);
		
		assertItemHandlerSize(inventory, 1);
		
		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		addSlot(new SlotItemHandler(inventory, 0, gristWidgetInputX, gristWidgetInputY)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() == MSItems.CAPTCHA_CARD && AlchemyHelper.hasDecodedItem(stack) && !AlchemyHelper.isPunchedCard(stack);
			}
		});
		
		bindPlayerInventory(new PlayerMainInvWrapper(playerInventory));
	}
	
	protected void bindPlayerInventory(IItemHandler playerInventory)
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
		
		for (int i = 0; i < 9; i++)
			addSlot(new SlotItemHandler(playerInventory, i, 8 + i * 18, 142));
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.GRIST_WIDGET;
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
			
			
			if(slotNumber <= 0)
			{
				//if it's a machine slot
				result = mergeItemStack(itemstackOrig, 2, allSlots, false);
			} else if(slotNumber > 0 && getSlot(0).isItemValid(itemstackOrig))
			{
				//if it's an inventory slot with valid contents
				result = mergeItemStack(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
}