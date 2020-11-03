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
			ItemStack stack = ItemStack.read(nbt);
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
		if (!player.world.isRemote && index >= 0 && index < inv.size())
		{
			ItemStack stack = inv.get(index);
			if (stack.isEmpty())
				return;
			PlayerData playerData = PlayerSavedData.getData(player);
			int amountPurchased = (int) Math.min(prices[index] != 0 ? playerData.getBoondollars() / prices[index] : Integer.MAX_VALUE, all ? stack.getCount() : 1);
			if (amountPurchased == 0)
			{
				player.sendMessage(new TranslationTextComponent(CANT_AFFORD));
			} else
			{
				playerData.takeBoondollars(amountPurchased * prices[index]);
				playerData.addConsortReputation(5);
				ItemStack items = stack.split(amountPurchased);
				if(stack.isEmpty())
					prices[index] = 0;
				
				if (!player.addItemStackToInventory(items))
				{
					ItemEntity entity = player.dropItem(items, false);
					if (entity != null)
						entity.setNoPickupDelay();
					else Debug.warn("Couldn't spawn in an item purchased from a consort! "+items);
				} else player.container.detectAndSendChanges();
				
				player.openContainer.detectAndSendChanges();
			}
		}
	}
	
	public ListNBT writeToNBT()
	{
		ListNBT list = new ListNBT();
		for (int i = 0; i < 9; i++)
		{
			CompoundNBT nbt = inv.get(i).write(new CompoundNBT());
			nbt.putInt("price", prices[i]);
			list.add(nbt);
		}
		return list;
	}
	
	@Override
	public int getSizeInventory()
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
	public ItemStack getStackInSlot(int index)
	{
		return inv.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 0;
	}
	
	@Override
	public void markDirty()
	{
	
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}
	
	@Override
	public void openInventory(PlayerEntity player)
	{
	
	}
	
	@Override
	public void closeInventory(PlayerEntity player)
	{
	
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return false;
	}
	
	@Override
	public void clear()
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
		return price;
	}
	
	public IIntArray createPricesFor(ServerPlayerEntity player)
	{
		PlayerData data = PlayerSavedData.getData(player);
		return new IIntArray()
		{
			@Override
			public int get(int index)
			{
				return calculatePrice(prices[index], data.getConsortReputation());
			}
			
			@Override
			public void set(int index, int value)
			{
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size()
			{
				return 9;
			}
		};
	}
}
