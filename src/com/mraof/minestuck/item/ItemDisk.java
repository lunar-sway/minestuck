package com.mraof.minestuck.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemDisk extends Item
{
	
	private String[] subNames = { "Client", "Server" };
	
	public ItemDisk()
	{
		super();
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(MinestuckItems.tabMinestuck);
		this.setUnlocalizedName("disk");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
	
	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		for (int i = 0; i < subNames.length; i++)
			items.add(new ItemStack(this, 1, i));
	}
}
