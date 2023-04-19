package com.mraof.minestuck.alchemy.recipe.generator;

import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public interface GeneratedCostProvider
{
	@Nullable
	GristCostResult generate(Item item, GristCostResult lastCost, GenerationContext context);
	
	default void build()
	{}
}