package com.mraof.minestuck.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.mraof.minestuck.Minestuck;

public class ItemCardBlank extends Item {

	public ItemCardBlank() {
		this.maxStackSize = 64;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("cardBlank");
	}

	public void registerIcons(IIconRegister par1IconRegister) {
	   itemIcon = par1IconRegister.registerIcon("minestuck:CardBlank");
	}
}
