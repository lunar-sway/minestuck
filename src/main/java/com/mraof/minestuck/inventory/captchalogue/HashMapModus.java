package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ModusDataPacket;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;

import java.util.Iterator;

public class HashMapModus extends Modus
{
	public static final String MESSAGE = "minestuck.hash_map";
	
	protected NonNullList<ItemStack> list;
	public boolean ejectByChat = true;
	
	//client only
	protected boolean changed;
	protected NonNullList<ItemStack> items;
	
	public HashMapModus(ModusType<? extends HashMapModus> type, PlayerSavedData savedData, LogicalSide side)
	{
		super(type, savedData, side);
	}
	
	@Override
	public void initModus(ServerPlayerEntity player, NonNullList<ItemStack> prev, int size)
	{
		list = NonNullList.create();
		if(prev != null)
		{
			list.addAll(prev);
		}
		while(list.size() < size)
			list.add(ItemStack.EMPTY);
		
		if(side == LogicalSide.CLIENT)
		{
			items = NonNullList.create();
			changed = true;
		}
	}
	
	@Override
	public void readFromNBT(CompoundNBT nbt)
	{
		int size = nbt.getInt("size");
		ejectByChat = nbt.getBoolean("ejectByChat");
		list = NonNullList.create();
		
		for(int i = 0; i < size; i++)
			if(nbt.contains("item"+i))
				list.add(ItemStack.read(nbt.getCompound("item"+i)));
			else list.add(ItemStack.EMPTY);
		if(side == LogicalSide.CLIENT)
		{
			if(items == null)
				items = NonNullList.create();
			changed = true;
		}
	}
	
	@Override
	public CompoundNBT writeToNBT(CompoundNBT nbt)
	{
		nbt.putInt("size", list.size());
		nbt.putBoolean("ejectByChat", ejectByChat);
		Iterator<ItemStack> iterator = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iterator.next();
			if(!stack.isEmpty())
				nbt.put("item"+i, stack.write(new CompoundNBT()));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ServerPlayerEntity player, ItemStack item)
	{
		if(list.size() == 0 || item.isEmpty())
			return false;
		
		String itemName = item.getItem().getRegistryName().getPath().replace('_', ' ');
		
		int index = ((item.hasDisplayName()) ? item.getDisplayName() : itemName).hashCode() % list.size();	//TODO Perhaps use a custom hashcode function that behaves more like the one in comic
		
		if(index < 0)
			index += list.size();
		
		if(!list.get(index).isEmpty())
		{
			ItemStack otherItem = list.get(index);
			if(otherItem.getItem() == item.getItem() && ItemStack.areItemStackTagsEqual(otherItem, item)
					&& otherItem.getCount() + item.getCount() <= otherItem.getMaxStackSize())
			{
				otherItem.grow(item.getCount());
				markDirty();
				return true;
			} else CaptchaDeckHandler.launchItem(player, list.get(index));
		}
		
		list.set(index, item);
		markDirty();
		
		if(ejectByChat && MinestuckConfig.hashmapChatModusSetting.get() != MinestuckConfig.AvailableOptions.OFF || MinestuckConfig.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.ON)
			player.sendMessage(new TranslationTextComponent(MESSAGE, item.getTextComponent(), getSize(), index));
		
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if(side == LogicalSide.SERVER)	//Used only when replacing the modus
		{
			NonNullList<ItemStack> items = NonNullList.create();
			fillList(items);
			return items;
		}
		
		if(changed)
		{
			fillList(items);
		}
		return items;
	}
	
	private void fillList(NonNullList<ItemStack> items)
	{
		items.clear();
		items.addAll(list);
	}
	
	@Override
	public boolean increaseSize(ServerPlayerEntity player)
	{
		if(MinestuckConfig.modusMaxSize.get() > 0 && list.size() >= MinestuckConfig.modusMaxSize.get())
			return false;
		
		list.add(ItemStack.EMPTY);
		markDirty();
		return true;
	}
	
	@Override
	public ItemStack getItem(ServerPlayerEntity player, int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			return ItemStack.EMPTY;	//Empty card is retrieved the same way as a normal item
		}
		
		if(list.isEmpty())
			return ItemStack.EMPTY;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(int i = 0; i < list.size(); i++)
				if(!list.get(i).isEmpty())
				{
					CaptchaDeckHandler.launchAnyItem(player, list.get(i));
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
				return new ItemStack(MSItems.CAPTCHA_CARD);
			else return AlchemyHelper.createCard(item, false);
		} else
		{
			list.set(id, ItemStack.EMPTY);
			markDirty();
			return item;
		}
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
	public void setValue(ServerPlayerEntity player, byte type, int value)
	{
		if(ejectByChat != value > 0)
		{
			ejectByChat = value > 0;
			markDirty();
		}
	}
	
	public void onChatMessage(ServerPlayerEntity player, String str)
	{
		if(!ejectByChat && MinestuckConfig.hashmapChatModusSetting.get() != MinestuckConfig.AvailableOptions.ON || MinestuckConfig.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.OFF)
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
			} else
			{
				isPrevLetter = false;
				
				if(number.length() > 0)
					handleNumber(player, number.toString());
			}
			
			number = new StringBuilder();
		}
		
		if(number.length() > 0)
			handleNumber(player, number.toString());
		
		ModusDataPacket packet = ModusDataPacket.create(CaptchaDeckHandler.writeToNBT(this));
		MSPacketHandler.sendToPlayer(packet, player);
		
	}
	
	private void handleNumber(ServerPlayerEntity player, String str)
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
		
		if(player.inventory.getCurrentItem().isEmpty())
			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
		else CaptchaDeckHandler.launchAnyItem(player, stack);
		
		player.sendMessage(new TranslationTextComponent("message.hash_map", i, getSize(), index, stack.getTextComponent()));
	}
	
}