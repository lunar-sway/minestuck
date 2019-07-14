package com.mraof.minestuck.inventory;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.inventory.slot.InputSlot;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.GristWidgetTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class GristWidgetContainer extends Container
{
	
	private static final int gristWidgetInputX = 27;
	private static final int gristWidgetInputY = 20;
	
	private final IInventory widgetInventory;
	private final IIntArray parameters;
	
	public GristWidgetContainer(int windowId, PlayerInventory playerInventory)
	{
		this(ModContainerTypes.GRIST_WIDGET, windowId, playerInventory, new Inventory(1), new IntArray(1));
	}
	
	public GristWidgetContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters)
	{
		this(ModContainerTypes.GRIST_WIDGET, windowId, playerInventory, inventory, parameters);
	}
	
	public GristWidgetContainer(ContainerType<? extends GristWidgetContainer> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray parameters)
	{
		super(type, windowId);
		
		assertInventorySize(inventory, 1);
		assertIntArraySize(parameters, 1);
		this.widgetInventory = inventory;
		this.parameters = parameters;
		
		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		addSlot(new InputSlot(inventory, 0, gristWidgetInputX, gristWidgetInputY, MinestuckItems.CAPTCHA_CARD)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack) && AlchemyRecipes.hasDecodedItem(stack) && !AlchemyRecipes.isPunchedCard(stack);
			}
		});
		trackIntArray(parameters);
		
		bindPlayerInventory(playerInventory);
	}
	@Override
	public boolean canInteractWith(PlayerEntity player)
	{
		return widgetInventory.isUsableByPlayer(player);
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
	
	@OnlyIn(Dist.CLIENT)
	public int getProgress()
	{
		return parameters.get(0);
	}
}