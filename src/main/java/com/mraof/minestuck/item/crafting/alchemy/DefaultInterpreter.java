package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DefaultInterpreter implements RecipeInterpreter
{
	public static final DefaultInterpreter INSTANCE = new DefaultInterpreter();
	
	@ObjectHolder("minestuck:default")
	public static final InterpreterSerializer<DefaultInterpreter> SERIALIZER = null;
	
	@Override
	public List<Item> getOutputItems(IRecipe<?> recipe)
	{
		ItemStack stack = recipe.getRecipeOutput();
		return stack.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack.getItem());
	}
	
	@Override
	public GristSet generateCost(IRecipe<?> recipes, Item item, Function<Ingredient, GristSet> ingredientInterpreter)
	{
		if(recipes.isDynamic())
			return null;
		
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
	
	@Override
	public InterpreterSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}
	
	public static class Serializer extends InterpreterSerializer<DefaultInterpreter>
	{
		@Override
		public DefaultInterpreter read(JsonElement json)
		{
			return INSTANCE;
		}
		
		@Override
		public JsonElement write(DefaultInterpreter interpreter)
		{
			return JsonNull.INSTANCE;
		}
	}
}