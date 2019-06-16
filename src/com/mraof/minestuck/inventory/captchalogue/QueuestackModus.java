package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.client.gui.captchalouge.QueuestackGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class QueuestackModus extends StackModus {
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				return new ItemStack(MinestuckItems.CAPTCHA_CARD);
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
			return AlchemyRecipes.createCard(id == 0 ? list.removeFirst() : list.removeLast(), false);
		}
		else return id == 0 ? list.removeFirst() : list.removeLast();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new QueuestackGuiHandler(this);
		return gui;
	}
	
}