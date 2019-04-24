package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.captchalouge.HashmapGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;

public class HashmapModus extends Modus
{
	
	protected NonNullList<ItemStack> list;
	public boolean ejectByChat = true;
	
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;
	
	@Override
	public void initModus(NonNullList<ItemStack> prev, int size)
	{
		list = NonNullList.<ItemStack>create();
		if(prev != null)
		{
			for(ItemStack stack : prev)
				list.add(stack);
		}
		while(list.size() < size)
			list.add(ItemStack.EMPTY);
		
		if(player.world.isRemote)
		{
			items = NonNullList.<ItemStack>create();
			changed = true;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int size = nbt.getInteger("size");
		ejectByChat = nbt.getBoolean("ejectByChat");
		list = NonNullList.<ItemStack>create();
		
		for(int i = 0; i < size; i++)
			if(nbt.hasKey("item"+i))
				list.add(new ItemStack(nbt.getCompoundTag("item"+i)));
			else list.add(ItemStack.EMPTY);
		if(side.isClient())
		{
			if(items == null)
				items = NonNullList.<ItemStack>create();
			changed = true;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("size", list.size());
		nbt.setBoolean("ejectByChat", ejectByChat);
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iter.next();
			if(!stack.isEmpty())
				nbt.setTag("item"+i, stack.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack item)
	{
		if(list.size() == 0 || item.isEmpty())
			return false;
		
		//TODO use registry names when 1.13 comes out
		
		String unloc = item.getUnlocalizedName();
		unloc = unloc.substring(unloc.indexOf('.')+1, unloc.length());

		if(unloc.indexOf('.') != -1)
		{
			String adj = unloc.substring(unloc.indexOf('.')+1, unloc.length());
			unloc = adj + " " + unloc.substring(0, unloc.indexOf('.'));
		}
		
		int index = ((item.hasDisplayName()) ? item.getDisplayName() : unloc).hashCode() % list.size();
				
		if(index < 0)
			index += list.size();
		
		if(!list.get(index).isEmpty())
		{
			ItemStack otherItem = list.get(index);
			if(otherItem.getItem() == item.getItem() && otherItem.getItemDamage() == item.getItemDamage() && ItemStack.areItemStackTagsEqual(otherItem, item)
					&& otherItem.getCount() + item.getCount() <= otherItem.getMaxStackSize())
			{
				otherItem.grow(item.getCount());
				return true;
			} else CaptchaDeckHandler.launchItem(player, list.get(index));
		}
		
		list.set(index, item);
		
		if(ejectByChat && MinestuckConfig.hashmapChatModusSetting != 2 || MinestuckConfig.hashmapChatModusSetting == 1)
			this.player.sendMessage(new TextComponentTranslation("message.hashmap", item.getTextComponent(), getSize(), index));
		
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if(side.isServer())	//Used only when replacing the modus
		{
			NonNullList<ItemStack> items = NonNullList.<ItemStack>create();
			fillList(items);
			return items;
		}
		
		if(changed)
		{
			fillList(items);
		}
		return items;
	}
	
	protected void fillList(NonNullList<ItemStack> items)
	{
		items.clear();
		for(int i = 0; i < list.size(); i++)
			items.add(list.get(i));
	}
	
	@Override
	public boolean increaseSize()
	{
		if(MinestuckConfig.modusMaxSize > 0 && list.size() >= MinestuckConfig.modusMaxSize)
			return false;
		
		list.add(ItemStack.EMPTY);
		return true;
	}
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
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
				}
			return ItemStack.EMPTY;
		}
		
		id = id % list.size();
		
		ItemStack item = list.get(id);
		
		if(asCard)
		{
			list.remove(id);
			if(item.isEmpty())
				return new ItemStack(MinestuckItems.captchaCard);
			else return AlchemyRecipes.createCard(item, false);
		} else
		{
			list.set(id, ItemStack.EMPTY);
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
	public void setValue(byte type, int value)
	{
		ejectByChat = value > 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new HashmapGuiHandler(this);
		return gui;
	}
	
	public void onChatMessage(String str)
	{
		if(!ejectByChat && MinestuckConfig.hashmapChatModusSetting != 1 || MinestuckConfig.hashmapChatModusSetting == 2)
			return;
		
		boolean isPrevLetter = false;
		String number = "";
		for(int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if(Character.isLetter(c))
				isPrevLetter = true;
			else if(Character.isDigit(c) || (number.isEmpty() && c == '-'))
			{
				if(!isPrevLetter)
					number = number + c;
				continue;
			} else
			{
				isPrevLetter = false;
				
				if(!number.isEmpty())
					handleNumber(number);
			}
			
			number = "";
		}
		
		if(!number.isEmpty())
			handleNumber(number);
		
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.DATA, CaptchaDeckHandler.writeToNBT(this));
		MinestuckChannelHandler.sendToPlayer(packet, player);
		
	}
	
	private void handleNumber(String str)
	{
		int i;
		
		try
		{
			i = Integer.parseInt(str);
		} catch(NumberFormatException e) {return;}
		
		int index = i % getSize();
		if(index < 0)
			index += getSize();
		
		ItemStack stack = getItem(index, false);
		if(stack == null)
			return;
		
		if(player.inventory.getCurrentItem() == null)
			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
		else CaptchaDeckHandler.launchAnyItem(player, stack);
		
		this.player.sendMessage(new TextComponentTranslation("message.hashmap", i, getSize(), index, stack.getTextComponent()));
	}
	
}