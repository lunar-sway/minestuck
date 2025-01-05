package com.mraof.minestuck.block;

import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

public enum EnumDowelType implements StringRepresentable
{
	NONE,
	DOWEL,
	CARVED_DOWEL;
	
	
	@Override
	public String getSerializedName()
	{
		return name().toLowerCase();
	}
	
	public static EnumDowelType getForDowel(ItemStack dowel)
	{
		if(dowel.isEmpty())
			return NONE;
		else if(dowel.has(MSItemComponents.ENCODED_ITEM))
			return CARVED_DOWEL;
		else return DOWEL;
	}
}
