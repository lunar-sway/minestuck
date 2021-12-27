package com.mraof.minestuck.inventory;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class ConsortMerchantInventory implements IInventory
{
	public static final String CANT_AFFORD = "consort.cant_afford";
	
	private final NonNullList<ItemStack> inv = NonNullList.withSize(9, ItemStack.EMPTY);
	private final int[] prices = new int[9];
	private final ConsortEntity consort;
	
	public ConsortMerchantInventory(ConsortEntity consort, ListNBT list)
	{
		this.consort = consort;
		
		for(int i = 0; i < list.size() && i < 9; i++)
		{
			CompoundNBT nbt = list.getCompound(i);
			ItemStack stack = ItemStack.of(nbt);
			inv.set(i, stack);
			if(!stack.isEmpty())
				prices[i] = nbt.getInt("price");
		}
	}
	
	public ConsortMerchantInventory(ConsortEntity consort, List<Pair<ItemStack, Integer>> stocks)
	{
		this.consort = consort;
		
		for (int i = 0; i < stocks.size(); i++)
		{
			Pair<ItemStack, Integer> entry = stocks.get(i);
			inv.set(i, entry.getKey());
			prices[i] = entry.getValue();
		}
	}
	
	public void handlePurchase(ServerPlayerEntity player, boolean all, int index)
	{
		if (!player.level.isClientSide && index >= 0 && index < inv.size())
		{
			ItemStack stack = inv.get(index);
			if (stack.isEmpty())
				return;
			PlayerData playerData = PlayerSavedData.getData(player);
			int amountPurchased = (int) Math.min(prices[index] != 0 ? playerData.getBoondollars() / prices[index] : Integer.MAX_VALUE, all ? stack.getCount() : 1);
			if (amountPurchased == 0)
			{
				player.sendMessage(new TranslationTextComponent(CANT_AFFORD), Util.NIL_UUID);
			} else
			{
				playerData.takeBoondollars(amountPurchased * prices[index]);
				playerData.addConsortReputation(5, consort.getHomeDimension());
				ItemStack items = stack.split(amountPurchased);
				if(stack.isEmpty())
					prices[index] = 0;
				
				if (!player.addItem(items))
				{
					ItemEntity entity = player.drop(items, false);
					if (entity != null)
						entity.setNoPickUpDelay();
					else Debug.warn("Couldn't spawn in an item purchased from a consort! "+items);
				} else player.inventoryMenu.broadcastChanges();
				
				player.containerMenu.broadcastChanges();
			}
		}
	}
	
	public ListNBT writeToNBT()
	{
		ListNBT list = new ListNBT();
		for (int i = 0; i < 9; i++)
		{
			CompoundNBT nbt = inv.get(i).save(new CompoundNBT());
			nbt.putInt("price", prices[i]);
			list.add(nbt);
		}
		return list;
	}
	
	@Override
	public int getContainerSize()
	{
		return 9;
	}
	
	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : inv)
			if(!stack.isEmpty())
				return false;
		
		return true;
	}
	
	@Override
	public ItemStack getItem(int index)
	{
		return inv.get(index);
	}
	
	@Override
	public ItemStack removeItem(int index, int count)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int index)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setItem(int index, ItemStack stack)
	{}
	
	@Override
	public int getMaxStackSize()
	{
		return 0;
	}
	
	@Override
	public void setChanged()
	{
	
	}
	
	@Override
	public boolean stillValid(PlayerEntity player)
	{
		return true;
	}
	
	@Override
	public void startOpen(PlayerEntity player)
	{
	
	}
	
	@Override
	public void stopOpen(PlayerEntity player)
	{
	
	}
	
	@Override
	public boolean canPlaceItem(int index, ItemStack stack)
	{
		return false;
	}
	
	@Override
	public void clearContent()
	{
		inv.clear();
		for(int i = 0; i < 9; i++)
			prices[i] = 0;
	}
	
	public int[] getPrices()
	{
		return Arrays.copyOf(prices, 9);
	}
	
	public int calculatePrice(int price, int consortRep)
	{
		if(consortRep < -500)
			return (int) Math.ceil(2*price);
		else if(consortRep < -200)
			return (int) Math.ceil(1.5*price);
		else if(consortRep < 400)
			return price;
		else if(consortRep < 1200)
			return (int) Math.ceil(0.9*price);
		else if(consortRep < 3500)
			return (int) Math.floor(0.8*price);
		else if(consortRep < 8000)
			return (int) Math.floor(0.7*price);
		else return (int) Math.floor(0.6*price);
	}
	
	public IIntArray createPricesFor(ServerPlayerEntity player)
	{
		PlayerData data = PlayerSavedData.getData(player);
		return new IIntArray()
		{
			@Override
			public int get(int index)
			{
				return calculatePrice(prices[index], data.getConsortReputation(consort.getHomeDimension()));
			}
			
			@Override
			public void set(int index, int value)
			{
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int getCount()
			{
				return 9;
			}
		};
	}
}
