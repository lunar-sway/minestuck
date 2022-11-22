package com.mraof.minestuck.inventory.slot;

import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ConsortMerchantSlot extends Slot
{
	private Player player;
	private ConsortMerchantInventory merchant;
	
	public ConsortMerchantSlot(Player player, Container inventoryIn, int index, int xPosition, int yPosition)
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
		if(merchant != null && player instanceof ServerPlayer serverPlayer)
			merchant.handlePurchase(serverPlayer, false, this.index);
		return ItemStack.EMPTY;
	}
}