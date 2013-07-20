package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.Minestuck;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.Icon;

public class ItemDowelCarved extends Item {

	public ItemDowelCarved(int par1) {
		super(par1);
		this.maxStackSize =1;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("dowelCruxiteCarved");
	}

	public void registerIcons(IconRegister par1IconRegister) {
	   itemIcon = par1IconRegister.registerIcon("Minestuck:CruxiteCarved");
	}
	
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (par1ItemStack.hasTagCompound())
        {
	        NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
	        NBTTagString nbttagstring = (NBTTagString)nbttagcompound.getTag("contents");
	
	        if (nbttagstring != null)
	        {
	        	par3List.add("(" + nbttagstring.toString() + ")");
	        }
        }
        par3List.add("(invalid data)");
    }
}
