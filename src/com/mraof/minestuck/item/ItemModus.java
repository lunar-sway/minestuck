package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemModus extends Item
{
	
	public IIcon[] icons = new IIcon[5];
	public String[] modusNames = {"stack", "queue", "queuestack", "tree", "hashmap"};
	
	public ItemModus()
	{
		this.maxStackSize = 16;
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("modusCard");
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return icons[meta];
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		icons[0] = iconRegister.registerIcon("minestuck:StackModus");
		icons[1] = iconRegister.registerIcon("minestuck:QueueModus");
		icons[2] = iconRegister.registerIcon("minestuck:QueuestackModus");
		icons[3] = iconRegister.registerIcon("minestuck:TreeModus");
		icons[4] = iconRegister.registerIcon("minestuck:HashmapModus");
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
		for(int i = 0; i < icons.length; i++)
			itemList.add(new ItemStack(this, 1, i));
	}
	
}
