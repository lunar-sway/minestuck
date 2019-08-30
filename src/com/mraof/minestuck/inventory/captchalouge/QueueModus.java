package com.mraof.minestuck.inventory.captchalouge;

import com.mraof.minestuck.client.gui.captchalouge.QueueGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;

public class QueueModus extends StackModus
{
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(player == null)
			return ItemStack.EMPTY;
		
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
		
		ItemStack item = list.removeLast();
		if(asCard && !(item.getItem() == MinestuckItems.captchaCard && item.hasTagCompound() && !item.getTagCompound().getBoolean("punched") && item.getTagCompound().hasKey("id")))
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new QueueGuiHandler(this);
		return gui;
	}
	
}