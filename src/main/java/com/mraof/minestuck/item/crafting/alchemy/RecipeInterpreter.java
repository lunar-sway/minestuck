package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Function;

public interface RecipeInterpreter
{
	List<Item> getOutputItems(IRecipe<?> recipe);
	
	GristSet generateCost(IRecipe<?> recipe, Item output, Function<Ingredient, GristSet> ingredientInterpreter);
	
	InterpreterSerializer<?> getSerializer();
}