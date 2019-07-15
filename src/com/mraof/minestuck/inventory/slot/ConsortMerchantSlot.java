package com.mraof.minestuck.inventory.slot;

import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ConsortMerchantSlot extends Slot
{
	private PlayerEntity player;
	private ConsortMerchantInventory merchant;
	
	public ConsortMerchantSlot(PlayerEntity player, ConsortMerchantInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.player = player;
		merchant = inventoryIn;
	}
	
	@Override
	public ItemStack decrStackSize(int amount)
	{
		if(player instanceof ServerPlayerEntity)
			merchant.handlePurchase((ServerPlayerEntity) player, false, this.slotNumber);
		return ItemStack.EMPTY;
	}
}