package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

import java.util.Iterator;
import java.util.LinkedList;

public class StackModus extends Modus
{
	
	protected int size;
	protected LinkedList<ItemStack> list;
	
	//client side
	protected boolean changed;
	protected NonNullList<ItemStack> items;
	
	public StackModus(ModusType<? extends StackModus> type, LogicalSide side)
	{
		super(type, side);
	}
	
	@Override
	public void initModus(ItemStack modusItem, ServerPlayer player, NonNullList<ItemStack> prev, int size)
	{
		this.size = size;
		list = new LinkedList<>();
		if(prev != null)
		{
			for(ItemStack stack : prev)
				if(!stack.isEmpty())
					list.add(stack);
		}
		
		if(player.level().isClientSide)
		{
			items = NonNullList.create();
			changed = prev != null;
		}
	}
	
	@Override
	public void readFromNBT(CompoundTag nbt, HolderLookup.Provider provider)
	{
		size = nbt.getInt("size");
		list = new LinkedList<>();
		
		for(int i = 0; i < size; i++)
			if(nbt.contains("item"+i, Tag.TAG_COMPOUND))
				list.add(ItemStack.parse(provider, nbt.getCompound("item"+i)).orElseThrow());
			else break;
		if(side == LogicalSide.CLIENT)
		{
			items = NonNullList.create();
			changed = true;
		}
	}
	
	@Override
	public CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider provider)
	{
		nbt.putInt("size", size);
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < list.size(); i++)
		{
			ItemStack stack = iter.next();
			nbt.put("item"+i, stack.save(provider));
		}
		return nbt;
	}
	
	@Override
	public boolean putItemStack(ServerPlayer player, ItemStack item)
	{
		if(size == 0 || item.isEmpty())
			return false;
		
		ItemStack firstItem = !list.isEmpty() ? list.getFirst() : ItemStack.EMPTY;
		if(ItemStack.isSameItemSameComponents(firstItem, item)
				&& firstItem.getCount() + item.getCount() <= firstItem.getMaxStackSize())
			firstItem.grow(item.getCount());
		else if(list.size() < size)
		{
			list.addFirst(item);
		} else
		{
			list.addFirst(item);
			CaptchaDeckHandler.launchItem(player, list.removeLast());
		}
		
		markDirty();
		
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
	public boolean increaseSize(ServerPlayer player)
	{
		if(hasHitMaxCards(player, size))
		{
			player.displayClientMessage(Component.translatable(CAPTCHA_LIMIT), true);
			return false;
		}
		
		size++;
		markDirty();
		
		return true;
	}

	@Override
	public ItemStack getItem(ServerPlayer player, int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				markDirty();
				return new ItemStack(MSItems.CAPTCHA_CARD.get());
			} else return ItemStack.EMPTY;
		}
		
		if(list.isEmpty())
			return ItemStack.EMPTY;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.EVENT_CAPTCHALOGUE_SHUFFLE.get(), SoundSource.AMBIENT, 1F, 1F);
			for(ItemStack item : list)
				CaptchaDeckHandler.ejectAnyItem(player, item);
			list.clear();
			markDirty();
			return ItemStack.EMPTY;
		}
		
		if(asCard)
		{
			size--;
			markDirty();
			return CaptchaCardItem.createCardWithItem(list.removeFirst(), player.server);
		}
		else
		{
			markDirty();
			return list.removeFirst();
		}
	}

	@Override
	public boolean canSwitchFrom(Modus modus)
	{
		return modus instanceof StackModus;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}
}
