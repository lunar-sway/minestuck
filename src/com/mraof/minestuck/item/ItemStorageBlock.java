package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStorageBlock extends ItemBlock
{
	private final static String[] subNames = {"Cruxite","GenericObject"};
	
	public ItemStorageBlock(Block block) {
		super(block);
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
