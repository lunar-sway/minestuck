package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

import java.util.Iterator;

public class QueueModus extends StackModus
{
	
	public QueueModus(ModusType<? extends QueueModus> type, LogicalSide side)
	{
		super(type, side);
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
			for(ItemStack item : list)
				CaptchaDeckHandler.launchAnyItem(player, item);
			list.clear();
			markDirty();
			return ItemStack.EMPTY;
		}
		
		ItemStack item = list.removeLast();
		markDirty();
		if(asCard && !(CaptchaCardItem.isUnpunchedCard(item) && item.has(MSItemComponents.CARD_STORED_ITEM)))
		{
			size--;
			markDirty();
			return CaptchaCardItem.createCardWithItem(item, player.server);
		}
		else return item;
	}
	
	@Override
	protected void fillList(NonNullList<ItemStack> items)
	{
		items.clear();
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < size; i++)
			if(i < size - list.size())
				items.add(ItemStack.EMPTY);
			else items.add(iter.next());
	}
}
