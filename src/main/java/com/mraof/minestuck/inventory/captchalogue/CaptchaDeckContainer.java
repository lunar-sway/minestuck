package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.inventory.MSContainerTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
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
			public boolean mayPlace(ItemStack stack)
			{
				return ModusTypes.getTypeFromItem(stack.getItem()) != null || stack.getItem().equals(MSItems.CAPTCHA_CARD) && !AlchemyHelper.isPunchedCard(stack);
			}
		});
	}
	
	@Override
	public void removed(PlayerEntity playerIn)
	{
		ItemStack stack = this.inventory.removeItemNoUpdate(0);
		if(!stack.isEmpty())
			playerIn.drop(stack, false);
	}
	
	@Override
	public boolean stillValid(PlayerEntity playerIn)
	{
		return ((PlayerInventory)this.getSlot(0).container).player == playerIn;
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
	{
		Slot slot = getSlot(index);
		int slotCount = slots.size();
		if(slot.hasItem())
		{
			ItemStack stack1 = slot.getItem();
			ItemStack stack2 = stack1.copy();
			if(index == slotCount - 1)
			{
				if(!moveItemStackTo(stack1, 0, slotCount - 1, false))
					return ItemStack.EMPTY;
			} else
			{
				if(!getSlot(slotCount - 1).mayPlace(stack1) || !moveItemStackTo(stack1, slotCount - 1, slotCount, false))
					return ItemStack.EMPTY;
			}
			
			if (stack1.isEmpty())
				slot.set(ItemStack.EMPTY);
			else slot.setChanged();
			return stack2;
		}
		return ItemStack.EMPTY;
	}
	
}