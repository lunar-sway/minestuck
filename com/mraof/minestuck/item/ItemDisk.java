package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemDisk extends Item {
	
	private Icon[] icons = new Icon[2];
	private String[] subNames = {"client","server"};

	public ItemDisk(int par1) {
		super(par1);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("disk");
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
	   icons[0] = par1IconRegister.registerIcon("minestuck:ClientDisk");
	   icons[1] = par1IconRegister.registerIcon("minestuck:ServerDisk");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) 
	{
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
	
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int unknown, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < subNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
}
