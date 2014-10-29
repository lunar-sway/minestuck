package com.mraof.minestuck.inventory;

import java.util.ArrayList;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.editmode.DeployList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInvEditmode extends Container {
	
	private EntityPlayer player;
	private static InventoryBasic inventory = new InventoryBasic("tmp", true, 14);
	public static int scrollIndex;
	private boolean mode;
	
	public ContainerInvEditmode(boolean mode)
	{
		this.player = ClientProxy.getPlayer();
		this.mode = mode;
		addSlots();
	}
	
	public ContainerInvEditmode(EntityPlayer player, boolean mode)
	{
		this.player = player;
		this.mode = mode;
		addSlots();
		if(player instanceof EntityPlayerMP)
			addCraftingToCrafters((EntityPlayerMP) player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		if(slotIndex >= 14 && slotIndex < this.inventorySlots.size())
		{
			Slot slot = (Slot) this.inventorySlots.get(slotIndex);
			ItemStack stack = slot.getStack();
			slot.putStack(null);
			return stack;
		}
		if(slotIndex >= 0 && slotIndex < 14)
		{
			Slot slot = (Slot) this.inventorySlots.get(slotIndex);
			ItemStack stack = slot.getStack();
			if(stack != null)
				mergeItemStack(stack.copy(), 14, this.inventorySlots.size(), false);
		}
		return null;
	}
	
	private void addSlots()
	{
		
		for(int i = 0; i < 14; i++)
			addSlotToContainer(new Slot(inventory, i, 26+(i%7)*18, 16+(i/7)*18));
		
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 74));
		updateInventory();
	}
	
	private void updateInventory()
	{
		for(int i = 0; i < 7; i++)
		{
			inventory.setInventorySlotContents(i, null);
		}
		ArrayList<ItemStack> list = DeployList.getItemList();
		for(int i = 0; i < 7; i++)
		{
			inventory.setInventorySlotContents(i+7, list.size()>i?list.get(i):null);
		}
	}
	
}
