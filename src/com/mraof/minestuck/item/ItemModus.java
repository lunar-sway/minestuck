package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.Minestuck;




import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModus extends Item
{
	
	public String[] modusNames = {"stack", "queue", "queuestack", "tree", "hashmap"};
	
	public ItemModus()
	{
		this.maxStackSize = 16;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("modusCard");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName()+"."+modusNames[stack.getItemDamage()];
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List itemList)
	{
		for(int i = 0; i < modusNames.length; i++)
			itemList.add(new ItemStack(this, 1, i));
	}
	
}
