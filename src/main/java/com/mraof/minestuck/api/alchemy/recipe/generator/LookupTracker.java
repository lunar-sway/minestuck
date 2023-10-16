package com.mraof.minestuck.api.alchemy.recipe.generator;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface LookupTracker
{
	void report(Item item);
	
	default void report(ItemStack stack)
	{
		this.report(stack.getItem());
	}
	
	default void report(Ingredient ingredient)
	{
		for(ItemStack stack : ingredient.getItems())
			this.report(stack);
	}
}
