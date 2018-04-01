package com.mraof.minestuck.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMinestuck extends CreativeTabs
{
	public static final TabMinestuck instance = new TabMinestuck("tabMinestuck");
	
	private TabMinestuck(String label)
	{
		super(label);
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(MinestuckItems.zillyhooHammer);
	}
}
