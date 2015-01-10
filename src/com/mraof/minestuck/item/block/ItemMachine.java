package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMachine extends ItemBlock {

		public final static String[] subNames = {"cruxtruder","punchDesignix","totemLathe","alchemiter","gristWidget"};
		
		public ItemMachine(Block block) 
		{
			super(block);
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
		
}
