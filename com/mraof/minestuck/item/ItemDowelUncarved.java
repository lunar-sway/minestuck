package com.mraof.minestuck.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

import com.mraof.minestuck.Minestuck;

public class ItemDowelUncarved extends Item {

	public ItemDowelUncarved(int par1) {
		super(par1);
		this.maxStackSize = 64;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("dowelCruxite");
	}

	public void registerIcons(IconRegister par1IconRegister) {
	   itemIcon = par1IconRegister.registerIcon("minestuck:CruxiteDowel");
	}
}
