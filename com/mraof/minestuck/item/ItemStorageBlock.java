package com.mraof.minestuck.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStorageBlock extends ItemBlock
{
	private final static String[] subNames = {"Cruxite","GenericObject"};
	
	public ItemStorageBlock(int par1) 
	{
		super(par1);
		setHasSubtypes(true);
		setUnlocalizedName("blockStorage");
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
