package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public interface RecipeInterpreter
{
	List<Item> getOutputItems(IRecipe<?> recipe);
	
	default GristSet generateCost(IRecipe<?> recipe, Item output, GenerationContext context)
	{
		return generateCost(recipe, output, context::costForIngredient);
	}
	
	@Deprecated	//Use the function with the context instead
	default GristSet generateCost(IRecipe<?> recipe, Item output, RecipeGeneratedCostHandler.IngredientLookup ingredientInterpreter)
	{
		return null;
	}
	
	InterpreterSerializer<?> getSerializer();
}