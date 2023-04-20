package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mraof.minestuck.alchemy.MutableGristSet;
import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface RecipeInterpreter
{
	List<Item> getOutputItems(Recipe<?> recipe);
	
	MutableGristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context);
	
	InterpreterSerializer<?> getSerializer();
}