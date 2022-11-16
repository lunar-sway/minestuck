package com.mraof.minestuck.alchemy.generator;

import net.minecraft.world.item.Item;

public interface GeneratedCostProvider
{
	GristCostResult generate(Item item, GristCostResult lastCost, GenerationContext context);
	
	default void build()
	{}
}