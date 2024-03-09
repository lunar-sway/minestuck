package com.mraof.minestuck.inventory;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.slot.ConsortMerchantSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ConsortMerchantMenu extends AbstractContainerMenu
{
	private final EnumConsort consortType;
	private final EnumConsort.MerchantType merchantType;
	private final ContainerData prices;
	
	private final Player player;
	
	public ConsortMerchantMenu(int windowId, Inventory playerInventory, Container storeInv, EnumConsort consortType, EnumConsort.MerchantType merchantType, ContainerData prices)
	{
		super(MSMenuTypes.CONSORT_MERCHANT.get(), windowId);
		this.player = playerInventory.player;
		this.consortType = consortType;
		this.merchantType = merchantType;
		
		checkContainerDataCount(prices, 9);
		this.prices = prices;
		addDataSlots(prices);
		
		checkContainerSize(storeInv, 9);
		
		for(int i = 0; i < 9; i++)
			this.addSlot(new ConsortMerchantSlot(player, storeInv, i, 17 + 35*(i%3), 35 + 33*(i/3)));
	}
	
	public static ConsortMerchantMenu load(int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		EnumConsort consortType = EnumConsort.getFromName(buffer.readUtf());
		EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.getFromName(buffer.readUtf());
		
		return new ConsortMerchantMenu(windowId, playerInventory, new SimpleContainer(9), consortType, merchantType, new SimpleContainerData(9));
	}
	
	public static void write(FriendlyByteBuf buffer, ConsortEntity consort)
	{
		buffer.writeUtf(consort.getConsortType().getName());
		buffer.writeUtf(consort.merchantType.getSerializedName());
	}
	
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index)
	{
		Slot slot = getSlot(index);
		slot.remove(0);
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return this.player == playerIn;
	}
	
	@Override
	public void removed(Player playerIn)
	{
		super.removed(playerIn);
		if(playerIn instanceof ServerPlayer player)
			player.inventoryMenu.sendAllDataToRemote();	//TODO is this still needed?
	}
	
	public int getPrice(int index)
	{
		return prices.get(index);
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