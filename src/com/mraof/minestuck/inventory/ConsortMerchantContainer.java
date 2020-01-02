package com.mraof.minestuck.inventory;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.slot.ConsortMerchantSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ConsortMerchantContainer extends Container
{
	private final EnumConsort consortType;
	private final EnumConsort.MerchantType merchantType;
	private final int[] prices;
	
	private final PlayerEntity player;
	
	public ConsortMerchantContainer(int windowId, PlayerInventory playerInventory, IInventory storeInv, EnumConsort consortType, EnumConsort.MerchantType merchantType, int[] prices)
	{
		super(MSContainerTypes.CONSORT_MERCHANT, windowId);
		this.player = playerInventory.player;
		this.consortType = consortType;
		this.merchantType = merchantType;
		this.prices = prices;
		
		assertInventorySize(storeInv, 9);
		
		for(int i = 0; i < 9; i++)
			this.addSlot(new ConsortMerchantSlot(player, storeInv, i, 17 + 35*(i%3), 35 + 33*(i/3)));
	}
	
	public static ConsortMerchantContainer load(int windowId, PlayerInventory playerInventory, PacketBuffer buffer)
	{
		EnumConsort consortType = EnumConsort.getFromName(buffer.readString());
		EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.getFromName(buffer.readString());
		int[] prices = new int[9];
		for(int i = 0; i < 9; i++)
			prices[i] = buffer.readInt();
		
		return new ConsortMerchantContainer(windowId, playerInventory, new Inventory(9), consortType, merchantType, prices);
	}
	
	public static void write(PacketBuffer buffer, ConsortEntity consort, int[] prices)
	{
		buffer.writeString(consort.getConsortType().getName());
		buffer.writeString(consort.merchantType.getName());
		for(int i = 0; i < 9; i++)
			buffer.writeInt(prices[i]);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		Slot slot = getSlot(index);
		if(slot != null)
			slot.decrStackSize(0);
		return ItemStack.EMPTY;
	}
	
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
		if(index >= 0 && index < prices.length)
			return prices[index];
		else return -1;
	}
	
	public EnumConsort getConsortType()
	{
		return consortType;
	}
	
	public EnumConsort.MerchantType getMerchantType()
	{
		return merchantType;
	}
}