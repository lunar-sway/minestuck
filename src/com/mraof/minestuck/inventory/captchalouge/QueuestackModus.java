package com.mraof.minestuck.inventory.captchalouge;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.captchalouge.QueuestackGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class QueuestackModus extends StackModus {
	
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				return new ItemStack(Minestuck.captchaCard);
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
		
		if(asCard)
		{
			size--;
			return AlchemyRecipeHandler.createCard(id == 0 ? list.removeFirst() : list.removeLast(), false);
		}
		else return id == 0 ? list.removeFirst() : list.removeLast();
	}
	
	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return modus == ModusType.STACK || modus == ModusType.QUEUE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new QueuestackGuiHandler(this);
		return gui;
	}
	
}
