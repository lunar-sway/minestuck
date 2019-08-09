package com.mraof.minestuck.inventory;

import com.mraof.minestuck.inventory.slot.ConsortMerchantSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class ConsortMerchantContainer extends Container
{
	public ConsortMerchantInventory inventory;
	
	private PlayerEntity player;
	
	public ConsortMerchantContainer(int windowId, PlayerInventory playerInventory)
	{
		super(ModContainerTypes.CONSORT_MERCHANT, windowId);
		//TODO
	}
	
	public ConsortMerchantContainer(int windowId, PlayerInventory playerInventory, ConsortMerchantInventory inv)
	{
		super(ModContainerTypes.CONSORT_MERCHANT, windowId);
		this.player = playerInventory.player;
		setInventory(inv);
	}
	
	public void setInventory(ConsortMerchantInventory inv)
	{
		inventory = inv;
		
		for(int i = 0; i < 9; i++)
			this.addSlot(new ConsortMerchantSlot(player, inventory, i, 17 + 35*(i%3), 35 + 33*(i/3)));
		
		/*for(IContainerListener listener : this.listeners)
			listener.sendAllWindowProperties(this, inventory);*/
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		if(playerIn instanceof ServerPlayerEntity)
			inventory.handlePurchase((ServerPlayerEntity) playerIn, true, index);
		return ItemStack.EMPTY;
	}
	/*
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		if(inventory != null)
			listener.sendAllWindowProperties(this, inventory);
	}*/
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return this.player == playerIn;
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn)
	{
		super.onContainerClosed(playerIn);
		if(playerIn instanceof ServerPlayerEntity)
			((ServerPlayerEntity) playerIn).sendContainerToPlayer(playerIn.container);
	}
	
	public int getPrice(int index)
	{
		if(index >= 0 && index < inventory.prices.length)
			return inventory.prices[index];
		else return -1;
	}
}