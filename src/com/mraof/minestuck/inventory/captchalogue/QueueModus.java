package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.LogicalSide;

import java.util.Iterator;

public class QueueModus extends StackModus
{
	
	public QueueModus(ModusType<? extends QueueModus> type, LogicalSide side)
	{
		super(type, side);
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
		
		ItemStack item = list.removeLast();
		if(asCard && !(item.getItem() == MSItems.CAPTCHA_CARD && item.hasTag() && !item.getTag().getBoolean("punched") && item.getTag().contains("id")))
		{
			size--;
			return AlchemyRecipes.createCard(item, false);
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