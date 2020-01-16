package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;

import java.util.function.Function;

public interface GeneratedCostProvider
{
	GristCostResult generate(Item item, GristCostResult lastCost, boolean primary, Function<Item, GristSet> itemLookup);
	
	default void build()
	{}
}