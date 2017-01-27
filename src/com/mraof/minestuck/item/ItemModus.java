package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModus extends Item
{
	
	public String[] modusNames = {"stack", "queue", "queuestack", "tree", "hashmap", "set"};
	
	public ItemModus()
	{
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("modusCard");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName()+"."+modusNames[stack.getItemDamage()];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for(int i = 0; i < 6; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
}