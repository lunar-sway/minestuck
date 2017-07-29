package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerConsortMerchant extends Container
{
	private final InventoryConsortMerchant inventory;
	
	private EntityPlayer player;
	
	public ContainerConsortMerchant(EntityPlayer player)
	{
		inventory = new InventoryConsortMerchant();
		this.player = player;
		
		this.addSlotToContainer(new Slot(inventory, 0, 17, 35));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.player == playerIn;
	}
	
}
