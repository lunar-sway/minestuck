package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.LogicalSide;

import java.util.Iterator;

public class SetModus extends Modus
{
	
	protected int size;
	protected NonNullList<ItemStack> list;
	
	//client side
	protected boolean changed;
	protected NonNullList<ItemStack> items;
	
	public SetModus(ModusType<? extends SetModus> type, LogicalSide side)
	{
		super(type, side);
	}
	
	@Override
	public void initModus(ServerPlayerEntity player, NonNullList<ItemStack> prev, int size)
	{
		this.size = size;
		list = NonNullList.create();
		/*if(prev != null)
		{
			for(ItemStack stack : prev)
				if(stack != null)
					list.add(stack);
		}*/
		
		if(side == LogicalSide.CLIENT)
		{
			items = NonNullList.create();
			changed = true;
		}
	}
	
	@Override
	public void readFromNBT(CompoundNBT nbt)
	{
		size = nbt.getInt("size");
		list = NonNullList.create();
		
		for(int i = 0; i < size; i++)
			if(nbt.contains("item"+i))
				list.add(ItemStack.read(nbt.getCompound("item"+i)));
			else break;
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
		nbt.putInt("size", size);
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iter.next();
			nbt.put("item"+i, stack.write(new CompoundNBT()));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ServerPlayerEntity player, ItemStack item)
	{
		if(size <= list.size() || item.isEmpty())
			return false;
		
		for(ItemStack stack : list)
			if(stack.getItem() == item.getItem())
			{
				CaptchaDeckHandler.launchItem(player, item);
				return true;
				
			}
		
		if(item.getCount() > 1)
		{
			ItemStack stack = item.copy();
			stack.shrink(1);
			item.setCount(1);
			CaptchaDeckHandler.launchItem(player, stack);
		}
		list.add(item);
		
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
	public boolean increaseSize(ServerPlayerEntity player)
	{
		if(MinestuckConfig.modusMaxSize.get() > 0 && size >= MinestuckConfig.modusMaxSize.get())
			return false;
		
		size++;
		
		return true;
	}
	
	@Override
	public ItemStack getItem(ServerPlayerEntity player, int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				return new ItemStack(MSItems.CAPTCHA_CARD);
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
		
		if(id < 0 || id >= list.size())
			return ItemStack.EMPTY;
		
		ItemStack item = list.remove(id);
		
		if(asCard)
		{
			size--;
			item = AlchemyRecipes.createCard(item, false);
		}
		return item;
	}
	
	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return false;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}
}