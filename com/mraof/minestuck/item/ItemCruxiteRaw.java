package com.mraof.minestuck.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

import com.mraof.minestuck.Minestuck;

public class ItemCruxiteRaw extends Item {

	public ItemCruxiteRaw(int par1) {
		super(par1);
		this.maxStackSize = 64;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("rawCruxite");
	}

	public void registerIcons(IconRegister par1IconRegister) {
	   itemIcon = par1IconRegister.registerIcon("minestuck:CruxiteRaw");
	}
}
