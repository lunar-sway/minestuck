package com.mraof.minestuck.inventory;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
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

public class GristWidgetMenu extends MachineContainerMenu
{
	
	private static final int gristWidgetInputX = 27;
	private static final int gristWidgetInputY = 20;
	
	public GristWidgetMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		this(MSMenuTypes.GRIST_WIDGET.get(), windowId, playerInventory, new ItemStackHandler(1), new SimpleContainerData(3), ContainerLevelAccess.NULL, buffer.readBlockPos());
	}
	
	public GristWidgetMenu(int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, ContainerLevelAccess position, BlockPos machinePos)
	{
		this(MSMenuTypes.GRIST_WIDGET.get(), windowId, playerInventory, inventory, parameters, position, machinePos);
	}
	
	public GristWidgetMenu(MenuType<? extends GristWidgetMenu> type, int windowId, Inventory playerInventory, IItemHandler inventory, ContainerData parameters, ContainerLevelAccess access, BlockPos machinePos)
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
				return stack.getItem() == MSItems.CAPTCHA_CARD.get() && AlchemyHelper.hasDecodedItem(stack) && !AlchemyHelper.isPunchedCard(stack);
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
		return MSBlocks.GRIST_WIDGET.get();
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		ItemStack comparing = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNumber);
		int allSlots = this.slots.size();
		
		if (slot.hasItem())
		{
			ItemStack itemstackOrig = slot.getItem().copy();
			comparing = itemstackOrig.copy();
			boolean result = false;
			
			
			if(slotNumber == 0) //if it's the card slot, move item to the inventory
			{
				
				result = moveItemStackTo(itemstackOrig, 1, allSlots, false);
			} else //if it's an inventory slot, move item to the card slot if possible
			{
				
				if(getSlot(0).mayPlace(itemstackOrig))
					result = moveItemStackTo(itemstackOrig, 0, 1, false);
			}
			
			if(!result)
				return ItemStack.EMPTY;
			
			if(!ItemStack.matches(itemstackOrig, slot.getItem()))
				slot.set(itemstackOrig);
		}
		
		return comparing;
	}
}