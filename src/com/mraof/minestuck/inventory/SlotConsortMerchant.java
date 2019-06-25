package com.mraof.minestuck.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotConsortMerchant extends Slot
{
	private EntityPlayer player;
	private InventoryConsortMerchant merchant;
	
	public SlotConsortMerchant(EntityPlayer player, InventoryConsortMerchant inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.player = player;
		merchant = inventoryIn;
	}
	
	@Override
	public ItemStack decrStackSize(int amount)
	{
		if(player instanceof EntityPlayerMP)
			merchant.handlePurchase((EntityPlayerMP) player, false, this.slotNumber);
		return ItemStack.EMPTY;
	}
}