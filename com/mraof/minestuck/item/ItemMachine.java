package com.mraof.minestuck.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemMachine extends ItemBlock {

		private final static String[] subNames = {"cruxtruder","punchDesignex","totemLathe","alchemiter","gristWidget"};
		private final static Icon[] icons = new Icon[5];
		
		public ItemMachine(int par1) 
		{
			super(par1);
			setHasSubtypes(true);
			setUnlocalizedName("machine");
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
		
		@Override
		public void registerIcons(IconRegister par1IconRegister) {
			   icons[0] = par1IconRegister.registerIcon("minestuck:Cruxtruder");
			   icons[1] = par1IconRegister.registerIcon("minestuck:PunchDesignex");
			   icons[2] = par1IconRegister.registerIcon("minestuck:TotemLathe");
			   icons[3] = par1IconRegister.registerIcon("minestuck:Alchemiter");
			   icons[4] = par1IconRegister.registerIcon("minestuck:GristWidget");
		}
		
		@Override
	    public Icon getIconFromDamage(int par1)
	    {
	        return icons[par1];
	    }
		
}
