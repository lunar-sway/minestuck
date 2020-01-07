package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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
	
	public static class Serializer extends InterpreterSerializer<DefaultInterpreter>
	{
		@Override
		protected GristCostGenerator.RecipeInterpreter read(JsonElement json)
		{
			return INSTANCE;
		}
		
		@Override
		protected JsonElement write(DefaultInterpreter interpreter)
		{
			return JsonNull.INSTANCE;
		}
	}
}