package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

import java.util.Iterator;

public class ArrayModus extends Modus
{
	public static final String MESSAGE = "minestuck.array";
	public static final String MESSAGE_EJECTED = "minestuck.array.ejected";
	
	protected NonNullList<ItemStack> list;
	public boolean ejectByChat = true;
	
	// client only
	protected boolean changed;
	protected NonNullList<ItemStack> items;
	
	public ArrayModus(ModusType<? extends ArrayModus> type, LogicalSide side)
	{
		super(type, side);
	}
	
	@Override
	public void initModus(ItemStack modusItem, ServerPlayer player, NonNullList<ItemStack> prev, int size)
	{
		list = NonNullList.create();
		if(prev != null)
			list.addAll(prev);
		while(list.size() < size)
			list.add(ItemStack.EMPTY);
		
		if(side == LogicalSide.CLIENT)
		{
			items = NonNullList.create();
			changed = true;
		}
	}
	
	@Override
	public void readFromNBT(CompoundTag nbt, HolderLookup.Provider provider)
	{
		int size = nbt.getInt("size");
		ejectByChat = nbt.getBoolean("ejectByChat");
		list = NonNullList.create();
		for(int i = 0; i < size; i++)
		{
			if(nbt.contains("item"+i, Tag.TAG_COMPOUND))
				list.add(ItemStack.parse(provider, nbt.getCompound("item"+i)).orElseThrow());
			else list.add(ItemStack.EMPTY);
		}
		if(side == LogicalSide.CLIENT)
		{
			if(items == null) items = NonNullList.create();
			changed = true;
		}
	}
	
	@Override
	public CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider provider)
	{
		nbt.putInt("size", list.size());
		nbt.putBoolean("ejectByChat", ejectByChat);
		Iterator<ItemStack> iterator = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iterator.next();
			if(!stack.isEmpty())
				nbt.put("item"+i, stack.save(provider));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ServerPlayer player, ItemStack item)
	{
		if(list.size() == 0 || item.isEmpty())
			return false;
		
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).isEmpty())
			{
				list.set(i, item);
				markDirty();
				
				if(ejectByChat && MinestuckConfig.SERVER.hashmapChatModusSetting.get() != MinestuckConfig.AvailableOptions.OFF
						|| MinestuckConfig.SERVER.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.ON)
				{
					player.sendSystemMessage(
							Component.translatable(MESSAGE, item.getDisplayName(), i)
					);
				}
				return true;
			}
		}
		
		CaptchaDeckHandler.launchItem(player, list.get(0));
		list.set(0, item);
		markDirty();
		
		if(ejectByChat)
			player.sendSystemMessage(
					Component.translatable(MESSAGE, item.getDisplayName(), 0)
			);
		
		return true;
	}
	
	@Override
	public ItemStack getItem(ServerPlayer player, int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
			return ItemStack.EMPTY;
		if(list.isEmpty())
			return ItemStack.EMPTY;
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(int i = 0; i < list.size(); i++)
				if(!list.get(i).isEmpty())
				{
					CaptchaDeckHandler.ejectAnyItem(player, list.get(i));
					list.set(i, ItemStack.EMPTY);
					markDirty();
				}
			return ItemStack.EMPTY;
		}
		
		id = id % list.size();
		ItemStack item = list.get(id);
		
		if(asCard)
		{
			list.remove(id);
			markDirty();
			if(item.isEmpty())
				return new ItemStack(MSItems.CAPTCHA_CARD.get());
			else
				return CaptchaCardItem.createCardWithItem(item, player.server);
		}
		else
		{
			list.set(id, ItemStack.EMPTY);
			markDirty();
			return item;
		}
	}
	
	@Override
	public boolean increaseSize(ServerPlayer player)
	{
		if(MinestuckConfig.SERVER.modusMaxSize.get() > 0 && list.size() >= MinestuckConfig.SERVER.modusMaxSize.get())
			return false;
		
		list.add(ItemStack.EMPTY);
		markDirty();
		return true;
	}
	
	private void fillList(NonNullList<ItemStack> items)
	{
		items.clear();
		items.addAll(list);
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if(side == LogicalSide.SERVER)
		{
			NonNullList<ItemStack> items = NonNullList.create();
			fillList(items);
			return items;
		}
		
		if(changed)
			fillList(items);
		
		return items;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return list.size();
	}
	
	@Override
	public void setValue(ServerPlayer player, byte type, int value)
	{
		if(ejectByChat != value > 0)
		{
			ejectByChat = value > 0;
			markDirty();
		}
	}
	
	public void onChatMessage(ServerPlayer player, String str)
	{
		if(!ejectByChat && MinestuckConfig.SERVER.hashmapChatModusSetting.get() != MinestuckConfig.AvailableOptions.ON
				|| MinestuckConfig.SERVER.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.OFF)
			return;
		
		boolean isPrevLetter = false;
		StringBuilder number = new StringBuilder();
		for(int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if(Character.isLetter(c))
				isPrevLetter = true;
			else if(Character.isDigit(c) || (number.length() == 0 && c == '-'))
			{
				if(!isPrevLetter)
					number.append(c);
				continue;
			}
			else
			{
				isPrevLetter = false;
				if(number.length() > 0)
					handleNumber(player, number.toString());
			}
			number = new StringBuilder();
		}
		
		if(number.length() > 0)
			handleNumber(player, number.toString());
		
		checkAndResend(player);
	}
	
	private void handleNumber(ServerPlayer player, String str)
	{
		int i;
		
		try
		{
			i = Integer.parseInt(str);
		} catch(NumberFormatException e) {return;}
		
		int index = i % getSize();
		if(index < 0)
			index += getSize();
		
		ItemStack stack = getItem(player, index, false);
		if(stack == null)
			return;
		
		if(player.getInventory().getSelected().isEmpty())
			player.getInventory().setItem(player.getInventory().selected, stack);
		else
			CaptchaDeckHandler.launchAnyItem(player, stack);
		
		player.sendSystemMessage(
				Component.translatable(MESSAGE_EJECTED, stack.getDisplayName(), index, getSize())
		);
	}
}
