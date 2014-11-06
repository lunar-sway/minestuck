package com.mraof.minestuck.inventory.captchalouge;

import java.util.Iterator;
import java.util.LinkedList;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler.ModusType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class QueueModus extends StackModus
{
	
	@Override
	public ItemStack getItem(int id)
	{
		return list.removeLast();
	}
	
	@Override
	protected void fillList(ItemStack[] items)
	{
		Iterator<ItemStack> iter = list.iterator();
		for(int i = 0; i < size; i++)
			if(i < size - list.size())
				items[i] = null;
			else items[i] = iter.next();
	}
	
	@Override
	public boolean canSwitchFrom(ModusType modus)
	{
		return modus == ModusType.STACK;
	}
	
}
