package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerConsortMerchant extends Container
{
	public InventoryConsortMerchant inventory;
	
	private EntityPlayer player;
	
	public ContainerConsortMerchant(EntityPlayer player, InventoryConsortMerchant inv)
	{
		this(player);
		setInventory(inv);
	}
	
	public ContainerConsortMerchant(EntityPlayer player)
	{
		this.player = player;
	}
	
	public void setInventory(InventoryConsortMerchant inv)
	{
		inventory = inv;
		
		for(int i = 0; i < 9; i++)
			this.addSlot(new SlotConsortMerchant(player, inventory, i, 17 + 35*(i%3), 35 + 33*(i/3)));
		
		for(IContainerListener listener : this.listeners)
			listener.sendAllWindowProperties(this, inventory);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		inventory.handlePurchase(playerIn, true, index);
		return ItemStack.EMPTY;
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		if(inventory != null)
			listener.sendAllWindowProperties(this, inventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.player == playerIn;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		inventory.setField(id, data);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		if(playerIn instanceof EntityPlayerMP)
			((EntityPlayerMP) playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
	}
}
