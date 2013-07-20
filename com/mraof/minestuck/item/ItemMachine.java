package com.mraof.minestuck.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMachine extends ItemBlock {

		private final static String[] subNames = {"cruxtruder","punchDesignex","totemLathe","alchemiter"};
		
		public ItemMachine(int par1) 
		{
			super(par1);
			setHasSubtypes(true);
			setUnlocalizedName("chessTile");
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
}
