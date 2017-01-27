package com.mraof.minestuck.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class ItemDisk extends Item
{
	
	private String[] subNames = { "Client", "Server" };
	
	public ItemDisk()
	{
		super();
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
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
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < subNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
}
