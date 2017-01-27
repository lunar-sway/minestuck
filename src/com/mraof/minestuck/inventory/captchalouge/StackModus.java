package com.mraof.minestuck.inventory.captchalouge;

import java.util.Iterator;
import java.util.LinkedList;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.captchalouge.StackGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StackModus extends Modus
{
	
	protected int size;
	protected LinkedList<ItemStack> list;
	
	@SideOnly(Side.CLIENT)
	protected boolean changed;
	@SideOnly(Side.CLIENT)
	protected NonNullList<ItemStack> items;
	@SideOnly(Side.CLIENT)
	protected SylladexGuiHandler gui;
	
	@Override
	public void initModus(NonNullList<ItemStack> prev, int size)
	{
		this.size = size;
		list = new LinkedList<ItemStack>();
		if(prev != null)
		{
			for(ItemStack stack : prev)
				if(!stack.isEmpty())
					list.add(stack);
		}
		
		if(player.world.isRemote)
		{
			items = NonNullList.<ItemStack>create();
			changed = prev != null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		size = nbt.getInteger("size");
		list = new LinkedList<ItemStack>();
		
		for(int i = 0; i < size; i++)
			if(nbt.hasKey("item"+i))
				list.add(new ItemStack(nbt.getCompoundTag("item"+i)));
			else break;
		if(side.isClient())
		{
			items = NonNullList.<ItemStack>create();
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
		if(size == 0 || item.isEmpty())
			return false;
		
		ItemStack firstItem = list.size() > 0 ? list.getFirst() : ItemStack.EMPTY;
		if(firstItem.getItem() == item.getItem() && firstItem.getItemDamage() == item.getItemDamage() && ItemStack.areItemStackTagsEqual(firstItem, item)
				&& firstItem.getCount() + item.getCount() <= firstItem.getMaxStackSize())
			firstItem.grow(item.getCount());
		else if(list.size() < size)
			list.addFirst(item);
		else
		{
			list.addFirst(item);
			CaptchaDeckHandler.launchItem(player, list.removeLast());
		}
		
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
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < size; i++)
			if(iter.hasNext())
				items.add(iter.next());
			else items.add(ItemStack.EMPTY);
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
			} else return ItemStack.EMPTY;
		}
		
		if(list.isEmpty())
			return ItemStack.EMPTY;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ItemStack item : list)
				CaptchaDeckHandler.launchAnyItem(player, item);
			list.clear();
			return ItemStack.EMPTY;
		}
		
		if(asCard)
		{
			size--;
			return AlchemyRecipeHandler.createCard(list.removeFirst(), false);
		}
		else return list.removeFirst();
	}

	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return modus == ModusType.QUEUE || modus == ModusType.QUEUE_STACK;
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
			gui = new StackGuiHandler(this);
		return gui;
	}
	
}