package com.mraof.minestuck.block;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum EnumDowelType implements IStringSerializable
{
	NONE,
	DOWEL,
	CARVED_DOWEL;
	
	
	@Override
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public static EnumDowelType getForDowel(ItemStack dowel)
	{
		if(dowel.isEmpty())
			return NONE;
		else if(AlchemyRecipes.hasDecodedItem(dowel))
			return CARVED_DOWEL;
		else return DOWEL;
	}
}
