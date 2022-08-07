package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
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
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nonnull;

public class GristWidgetContainer extends MachineContainer
{
	
	private static final int gristWidgetInputX = 27;
	private static final int gristWidgetInputY = 20;
	
	public GristWidgetContainer(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		this(MSContainerTypes.GRIST_WIDGET, windowId, playerInventory, new ItemStackHandler(1), new SimpleContainerData(3), ContainerLevelAccess.NULL, buffer.readBlockPos());
	}
	
	public GristWidgetContainer(int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, ContainerLevelAccess position, BlockPos machinePos)
	{
		this(MSContainerTypes.GRIST_WIDGET, windowId, playerInventory, inventory, parameters, position, machinePos);
	}
	
	public GristWidgetContainer(MenuType<? extends GristWidgetContainer> type, int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, ContainerLevelAccess access, BlockPos machinePos)
	{
		super(type, windowId, parameters, access, machinePos);
		
		assertItemHandlerSize(inventory, 1);
		
		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		addSlot(new SlotItemHandler(inventory, 0, gristWidgetInputX, gristWidgetInputY)
		{
			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return stack.getItem() == MSItems.CAPTCHA_CARD && AlchemyHelper.hasDecodedItem(stack) && !AlchemyHelper.isPunchedCard(stack);
			}
		});
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 84, playerInventory);
	}
	
	@Override
	protected Block getValidBlock()
	{
		return MSBlocks.GRIST_WIDGET;
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
			
			
			if(slotNumber <= 0)
			{
				//if it's a machine slot
				result = moveItemStackTo(itemstackOrig, 2, allSlots, false);
			} else if(slotNumber > 0 && getSlot(0).mayPlace(itemstackOrig))
			{
				//if it's an inventory slot with valid contents
				result = moveItemStackTo(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!itemstackOrig.isEmpty())
				slot.setChanged();
		}
		
		return itemstack;
	}
}