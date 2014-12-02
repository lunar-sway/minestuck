package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.block.BlockColoredDirt;

public class ItemColoredDirt extends ItemBlock
{
	public ItemColoredDirt(Block par1) 
	{
		super(par1);
		setHasSubtypes(true);
		setUnlocalizedName("coloredDirt");
	}
	@Override
	public String getUnlocalizedName(ItemStack itemstack) 
	{
		return getUnlocalizedName() + "." + ((BlockColoredDirt)block).iconNames[itemstack.getItemDamage()];
	}
	@Override
	public int getMetadata (int damageValue) 
	{
		return damageValue;
	}
	
}
