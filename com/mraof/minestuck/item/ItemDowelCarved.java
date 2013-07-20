package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
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
}
