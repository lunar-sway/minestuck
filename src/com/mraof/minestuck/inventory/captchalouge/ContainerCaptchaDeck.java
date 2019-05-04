package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class ContainerCaptchaDeck extends Container
{
	
	public InventoryBasic inventory = new InventoryBasic(new TextComponentString("ModusInventory"), 1);
	
	public ContainerCaptchaDeck(EntityPlayer player)
	{
		addSlots(player);
	}
	
	private void addSlots(EntityPlayer player)
	{
		for(int i = 9; i < 36; i++)
			addSlot(new Slot(player.inventory, i, 9 + (i%9)*18, 63 + ((i - 9)/9)*18));
		for(int i = 0; i < 9; i++)
			addSlot(new Slot(player.inventory, i, 9 + i*18, 121));
		addSlot(new Slot(this.inventory, 0, 81, 32)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return CaptchaDeckHandler.getType(stack) != null || stack.getItem().equals(MinestuckItems.CAPTCHA_CARD) && (!stack.hasTag() || !stack.getTag().getBoolean("punched"));
			}
		});
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		ItemStack stack = this.inventory.removeStackFromSlot(0);
		if(!stack.isEmpty())
			player.dropItem(stack, false);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return ((InventoryPlayer)this.getSlot(0).inventory).player == player;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		Slot slot = getSlot(slotNumber);
		int slotCount = inventorySlots.size();
		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			ItemStack stack2 = stack1.copy();
			if(slotNumber == slotCount - 1)
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