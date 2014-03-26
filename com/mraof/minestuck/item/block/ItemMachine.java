package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

public class ItemMachine extends ItemBlock {

		private final static String[] subNames = {"cruxtruder","punchDesignex","totemLathe","alchemiter","gristWidget"};
		private final static IIcon[] icons = new IIcon[5];
		
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
		
		@Override
		public void registerIcons(IIconRegister par1IconRegister) {
			   icons[0] = par1IconRegister.registerIcon("minestuck:Cruxtruder");
			   icons[1] = par1IconRegister.registerIcon("minestuck:PunchDesignex");
			   icons[2] = par1IconRegister.registerIcon("minestuck:TotemLathe");
			   icons[3] = par1IconRegister.registerIcon("minestuck:Alchemiter");
			   icons[4] = par1IconRegister.registerIcon("minestuck:GristWidget");
		}
		
		@Override
	    public IIcon getIconFromDamage(int par1)
	    {
	        return icons[par1];
	    }
		
}
