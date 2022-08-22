package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.inventory.MSContainerTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CaptchaDeckContainer extends AbstractContainerMenu
{
	
	private final Container inventory = new SimpleContainer(1);
	private final Player owner;
	
	public CaptchaDeckContainer(int windowId, Inventory playerInventory)
	{
		super(MSContainerTypes.CAPTCHA_DECK, windowId);
		this.owner = playerInventory.player;
		addSlots(playerInventory);
	}
	
	public ItemStack getContainerItem()
	{
		return inventory.getItem(0);
	}
	
	public void setContainerItem(ItemStack stack)
	{
		inventory.setItem(0, stack);
	}
	
	private void addSlots(Inventory playerInventory)
	{
		ContainerHelper.addPlayerInventorySlots(this::addSlot, 9, 63, playerInventory);
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
	public void removed(Player playerIn)
	{
		ItemStack stack = this.inventory.removeItemNoUpdate(0);
		if(!stack.isEmpty())
			playerIn.drop(stack, false);
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return owner == playerIn;
	}
	
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index)
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