package com.mraof.minestuck.inventory.captchalouge;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.captchalouge.SetGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class SetModus extends Modus
{
	
	protected int size;
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
		this.size = size;
		list = new ArrayList<ItemStack>();
		/*if(prev != null)
		{
			for(ItemStack stack : prev)
				if(stack != null)
					list.add(stack);
		}*/
		
		if(player.worldObj.isRemote)
		{
			items = new ItemStack[size];
			changed = prev != null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		size = nbt.getInteger("size");
		list = new ArrayList<ItemStack>();
		
		for(int i = 0; i < size; i++)
			if(nbt.hasKey("item"+i))
				list.add(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"+i)));
			else break;
		if(side.isClient())
		{
			items = new ItemStack[size];
			changed = true;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("size", size);
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iter.next();
			nbt.setTag("item"+i, stack.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ItemStack item)
	{
		if(size <= list.size() || item == null)
			return false;
		
		for(ItemStack stack : list)
			if(stack.getItem() == item.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == item.getMetadata()))
			{
				CaptchaDeckHandler.launchItem(player, item);
				return true;
				
			}
		
		if(item.stackSize > 1)
		{
			ItemStack stack = item.copy();
			stack.stackSize--;
			item.stackSize = 1;
			CaptchaDeckHandler.launchItem(player, stack);
		}
		list.add(item);
		
		return true;
	}
	
	@Override
	public ItemStack[] getItems()
	{
		if(side.isServer())	//Used only when replacing the modus
		{
			ItemStack[] items = new ItemStack[size];
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
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < size; i++)
			if(iter.hasNext())
				items[i] = iter.next();
			else items[i] = null;
	}
	
	@Override
	public boolean increaseSize()
	{
		if(MinestuckConfig.modusMaxSize > 0 && size >= MinestuckConfig.modusMaxSize)
			return false;
		
		size++;
		
		return true;
	}
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				return new ItemStack(MinestuckItems.captchaCard);
			} else return null;
		}
		
		if(list.isEmpty())
			return null;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ItemStack item : list)
				CaptchaDeckHandler.launchAnyItem(player, item);
			list.clear();
			return null;
		}
		
		if(id < 0 || id >= list.size())
			return null;
		
		ItemStack item = list.remove(id);
		
		if(asCard)
		{
			size--;
			item = AlchemyRecipeHandler.createCard(item, false);
		}
		return item;
	}
	
	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new SetGuiHandler(this);
		return gui;
	}
}