package com.mraof.minestuck.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemModus extends Item
{
	
	public static final String[] NAMES = {"stack", "queue", "queuestack", "tree", "hashmap", "set"};
	
	public ItemModus()
	{
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(MinestuckItems.tabMinestuck);
		this.setUnlocalizedName("modusCard");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName()+"."+ NAMES[stack.getItemDamage()];
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		for(int i = 0; i < 6; i++)
			items.add(new ItemStack(this, 1, i));
	}
	
}