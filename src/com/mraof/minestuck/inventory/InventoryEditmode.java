package com.mraof.minestuck.inventory;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryEditmode implements IInventory  
{
	
	public ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	
	public int scrollIndex;
	
	@Override
	public int getSizeInventory()
	{
		return 14;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		index += scrollIndex * 2;
		return index >= 0 && index < items.size() && items.get(index) != null? items.get(index).copy():null;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int i)
	{
		ItemStack stack = getStackInSlot(index);
		stack.stackSize = i;
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return getStackInSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack item)
	{}
	
	@Override
	public String getInventoryName()
	{
		return "inventoryEditmode";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void markDirty()
	{}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory()
	{}

	@Override
	public void closeInventory()
	{}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack item)
	{
		return true;
	}

}
