package com.mraof.minestuck.alchemy.generator.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.generator.GenerationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collections;
import java.util.List;

public class DefaultInterpreter implements RecipeInterpreter
{
	public static final DefaultInterpreter INSTANCE = new DefaultInterpreter();
	
	//TODO interpreter (perhaps setting) that makes the interpreter not remove container cost for ingredient
	
	@Override
	public List<Item> getOutputItems(Recipe<?> recipe)
	{
		ItemStack stack = recipe.getResultItem();
		return stack.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack.getItem());
	}
	
	@Override
	public GristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context)
	{
		if(recipe.isSpecial())
			return null;

		GristSet totalCost = new GristSet();
		for(Ingredient ingredient : recipe.getIngredients())
		{
			GristSet ingredientCost = context.costForIngredient(ingredient, true);
			if(ingredientCost == null)
				return null;
			else totalCost.addGrist(ingredientCost);
		}
		
		totalCost.scale(1F/recipe.getResultItem().getCount(), false);	//Do not round down because it's better to have something cost a little to much than it possibly costing nothing
		
		return totalCost;
	}
	
	@Override
	public InterpreterSerializer<?> getSerializer()
	{
		return InterpreterSerializers.DEFAULT.get();
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