package com.mraof.minestuck.inventory.slot;

import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ConsortMerchantSlot extends Slot
{
	private PlayerEntity player;
	private ConsortMerchantInventory merchant;
	
	public ConsortMerchantSlot(PlayerEntity player, IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.player = player;
		if(inventoryIn instanceof ConsortMerchantInventory)
			merchant = (ConsortMerchantInventory) inventoryIn;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public ItemStack remove(int amount)
	{
		if(merchant != null && player instanceof ServerPlayerEntity)
			merchant.handlePurchase((ServerPlayerEntity) player, false, this.index);
		return ItemStack.EMPTY;
	}
}