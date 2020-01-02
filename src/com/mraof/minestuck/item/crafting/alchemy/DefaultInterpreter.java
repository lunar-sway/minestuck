package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Function;

public class DefaultInterpreter implements GristCostGenerator.RecipeInterpreter
{
	public static final DefaultInterpreter INSTANCE = new DefaultInterpreter();
	
	@Override
	public GristSet generateCost(IRecipe<?> recipes, Function<Ingredient, GristSet> ingredientInterpreter)
	{
		GristSet totalCost = new GristSet();
		for(Ingredient ingredient : recipes.getIngredients())
		{
			GristSet ingredientCost = ingredientInterpreter.apply(ingredient);
			if(ingredientCost == null)
				return null;
			else totalCost.addGrist(ingredientCost);
		}
		return totalCost;
	}
}