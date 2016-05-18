package com.mraof.minestuck.inventory.captchalouge;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.captchalouge.HashmapGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class HashmapModus extends Modus
{
	
	protected ArrayList<ItemStack> list;
	
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected ItemStack[] items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;	
	
	@Override
	public void initModus(ItemStack[] prev, int size)
	{
		list = new ArrayList<ItemStack>();
		if(prev != null)
		{
			for(ItemStack stack : prev)
				list.add(stack);
		}
		for(int i = list.size(); i < size; i++)
			list.add(null);
		
		if(player.worldObj.isRemote)
		{
			items = new ItemStack[size];
			changed = prev != null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int size = nbt.getInteger("size");
		list = new ArrayList<ItemStack>();
		
		for(int i = 0; i < size; i++)
			if(nbt.hasKey("item"+i))
				list.add(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"+i)));
			else list.add(null);
		if(side.isClient())
		{
			items = new ItemStack[size];
			changed = true;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("size", list.size());
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iter.next();
			if(stack != null)
				nbt.setTag("item"+i, stack.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack item)
	{
		if(list.size() == 0 || item == null)
			return false;
		
		int index = item.getUnlocalizedName().hashCode() % list.size();
		if(index < 0)
			index += list.size();
		
		while(list.size() < list.size())
			list.add(null);
		
		if(list.get(index) != null)
			CaptchaDeckHandler.launchItem(player, list.get(index));
		
		list.set(index, item);
		
		return true;
	}
	
	@Override
	public ItemStack[] getItems()
	{
		if(side.isServer())	//Used only when replacing the modus
		{
			ItemStack[] items = new ItemStack[list.size()];
			fillList(items);
			return items;
		}
		
		if(changed)
		{
			fillList(items);
		}
		return items;
	}
	
	protected void fillList(ItemStack[] items)
	{
		for(int i = 0; i < list.size(); i++)
			items[i] = list.get(i);
	}
	
	@Override
	public boolean increaseSize()
	{
		if(MinestuckConfig.modusMaxSize > 0 && list.size() >= MinestuckConfig.modusMaxSize)
			return false;
		
		list.add(null);
		return true;
	}
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			return null;	//Empty card is retrieved the same way as a normal item
		}
		
		if(list.isEmpty())
			return null;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(int i = 0; i < list.size(); i++)
				if(list.get(i) != null)
				{
					CaptchaDeckHandler.launchAnyItem(player, list.get(i));
					list.set(i, null);
				}
			return null;
		}
		
		id = id % list.size();
		
		ItemStack item = list.get(id);
		
		if(asCard)
		{
			list.remove(id);
			if(item == null)
				return new ItemStack(MinestuckItems.captchaCard);
			else return AlchemyRecipeHandler.createCard(item, false);
		} else
		{
			list.set(id, null);
			return item;
		}
	}
	
	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return list.size();
	}
	
	@Override
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new HashmapGuiHandler(this);
		return gui;
	}
	
}