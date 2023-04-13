package com.mraof.minestuck.alchemy.recipe;

import com.mraof.minestuck.alchemy.recipe.CombinationMode;
import net.minecraft.world.Container;

public interface ItemCombiner extends Container
{
	CombinationMode getMode();
}