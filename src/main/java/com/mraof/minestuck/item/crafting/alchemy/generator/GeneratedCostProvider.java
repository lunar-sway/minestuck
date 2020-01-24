package com.mraof.minestuck.item.crafting.alchemy.generator;

import net.minecraft.item.Item;

public interface GeneratedCostProvider
{
	GristCostResult generate(Item item, GristCostResult lastCost, GenerationContext context);
	
	default void build()
	{}
}