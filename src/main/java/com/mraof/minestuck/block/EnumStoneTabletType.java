package com.mraof.minestuck.block;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum EnumStoneTabletType implements IStringSerializable
{
	UNCARVED,
	CARVED;
	
	
	@Override
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public static EnumStoneTabletType getForTablet(ItemStack tabletItemStack)
	{
		if(tabletItemStack.isEmpty())
			return UNCARVED;
		else return CARVED;
	}
}
