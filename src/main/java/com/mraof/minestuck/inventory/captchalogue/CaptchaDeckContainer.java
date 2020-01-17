package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.inventory.MSContainerTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CaptchaDeckContainer extends Container
{
	
	public Inventory inventory = new Inventory(1);
	
	public CaptchaDeckContainer(int windowId, PlayerInventory playerInventory)
	{
		super(MSContainerTypes.CAPTCHA_DECK, windowId);
		addSlots(playerInventory);
	}
	
	private void addSlots(PlayerInventory playerInventory)
	{
		for(int i = 9; i < 36; i++)
			addSlot(new Slot(playerInventory, i, 9 + (i%9)*18, 63 + ((i - 9)/9)*18));
		for(int i = 0; i < 9; i++)
			addSlot(new Slot(playerInventory, i, 9 + i*18, 121));
		addSlot(new Slot(this.inventory, 0, 81, 32)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return ModusTypes.getTypeFromItem(stack.getItem()) != null || stack.getItem().equals(MSItems.CAPTCHA_CARD) && (!stack.hasTag() || !stack.getTag().getBoolean("punched"));
			}
		});
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn)
	{
		ItemStack stack = this.inventory.removeStackFromSlot(0);
		if(!stack.isEmpty())
			playerIn.dropItem(stack, false);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return ((PlayerInventory)this.getSlot(0).inventory).player == playerIn;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		Slot slot = getSlot(index);
		int slotCount = inventorySlots.size();
		if(slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			ItemStack stack2 = stack1.copy();
			if(index == slotCount - 1)
			{
				if(!mergeItemStack(stack1, 0, slotCount - 1, false))
					return ItemStack.EMPTY;
			} else
			{
				if(!getSlot(slotCount - 1).isItemValid(stack1) || !mergeItemStack(stack1, slotCount - 1, slotCount, false))
					return ItemStack.EMPTY;
			}
			
			if (stack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();
			return stack2;
		}
		return ItemStack.EMPTY;
	}
	
}