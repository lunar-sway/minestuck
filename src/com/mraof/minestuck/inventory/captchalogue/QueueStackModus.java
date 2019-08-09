package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.client.gui.captchalouge.QueuestackSylladexScreen;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;

public class QueueStackModus extends StackModus
{
	
	public QueueStackModus(LogicalSide side)
	{
		super(side);
	}
	
	@Override
	public ResourceLocation getRegistryName()
	{
		return CaptchaDeckHandler.QUEUE_STACK;
	}
	
	@Override
	public ItemStack getItem(ServerPlayerEntity player, int id, boolean asCard)
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
	public SylladexScreen getGuiHandler()
	{
		if(gui == null)
			gui = new QueuestackSylladexScreen(this);
		return gui;
	}
	
}