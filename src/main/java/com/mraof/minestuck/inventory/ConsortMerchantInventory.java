package com.mraof.minestuck.inventory;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortReputation;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class ConsortMerchantInventory implements Container
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String CANT_AFFORD = "consort.cant_afford";
	
	private final NonNullList<ItemStack> inv = NonNullList.withSize(9, ItemStack.EMPTY);
	private final int[] prices = new int[9];
	private final ConsortEntity consort;
	
	public ConsortMerchantInventory(ConsortEntity consort, ListTag list)
	{
		this.consort = consort;
		
		for(int i = 0; i < list.size() && i < 9; i++)
		{
			CompoundTag nbt = list.getCompound(i);
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
	
	public void handlePurchase(ServerPlayer player, boolean all, int index)
	{
		if (!player.level().isClientSide && index >= 0 && index < inv.size())
		{
			ItemStack stack = inv.get(index);
			if (stack.isEmpty())
				return;
			PlayerData playerData = PlayerData.get(player).orElseThrow();
			int amountPurchased = (int) Math.min(prices[index] != 0 ? PlayerBoondollars.getBoondollars(playerData) / prices[index] : Integer.MAX_VALUE, all ? stack.getCount() : 1);
			if (amountPurchased == 0)
			{
				player.sendSystemMessage(Component.translatable(CANT_AFFORD));
			} else
			{
				PlayerBoondollars.takeBoondollars(playerData, amountPurchased * prices[index]);
				ConsortReputation.get(playerData).addConsortReputation(5, consort.getHomeDimension());
				ItemStack items = stack.split(amountPurchased);
				if(stack.isEmpty())
				{
					prices[index] = 0;
					if (Arrays.stream(prices).sum() == 0)
						MSCriteriaTriggers.BUY_OUT_SHOP.get().trigger(player);
				}
				
				if (player.addItem(items))
					player.inventoryMenu.broadcastChanges();
				else
				{
					ItemEntity entity = player.drop(items, false);
					if (entity != null)
						entity.setNoPickUpDelay();
					else LOGGER.warn("Couldn't spawn in an item purchased from a consort: {}", items);
				}
			}
		}
	}
	
	public ListTag writeToNBT()
	{
		ListTag list = new ListTag();
		for (int i = 0; i < 9; i++)
		{
			CompoundTag nbt = inv.get(i).save(new CompoundTag());
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
	public boolean stillValid(Player player)
	{
		return true;
	}
	
	@Override
	public void startOpen(Player player)
	{
	
	}
	
	@Override
	public void stopOpen(Player player)
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
	
	public ContainerData createPricesFor(ServerPlayer player)
	{
		ConsortReputation reputation = ConsortReputation.get(player);
		return new ContainerData()
		{
			@Override
			public int get(int index)
			{
				return calculatePrice(prices[index], reputation.getConsortReputation(consort.getHomeDimension()));
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
