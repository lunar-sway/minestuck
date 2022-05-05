package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.world.Container;

public interface ItemCombiner extends Container
{
	CombinationMode getMode();
}