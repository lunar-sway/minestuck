package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class DefaultInterpreter implements RecipeInterpreter
{
	public static final DefaultInterpreter INSTANCE = new DefaultInterpreter();
	
	@ObjectHolder("minestuck:default")
	public static final InterpreterSerializer<DefaultInterpreter> SERIALIZER = null;
	
	//TODO interpreter (perhaps setting) that makes the interpreter not remove container cost for ingredient
	
	@Override
	public List<Item> getOutputItems(IRecipe<?> recipe)
	{
		ItemStack stack = recipe.getResultItem();
		return stack.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack.getItem());
	}
	
	@Override
	public GristSet generateCost(IRecipe<?> recipe, Item output, GenerationContext context)
	{
		if(recipe.isSpecial())
			return null;

		GristSet totalCost = new GristSet();

		// Frick reflection frick reflection frick reflection frick reflection i hate java
		// this was a whole minecraft oversight anyway
		// so if they eventually fix this, remove this bit ok?
		NonNullList<Ingredient> ingredients;
		if (recipe instanceof SmithingRecipe)
		{
			try
			{
				Field baseField = SmithingRecipe.class.getDeclaredField("base");
				baseField.setAccessible(true);
				Ingredient base = (Ingredient)baseField.get(recipe);

				Field additionField = SmithingRecipe.class.getDeclaredField("addition");
				additionField.setAccessible(true);
				Ingredient addition = (Ingredient)additionField.get(recipe);

				// The first argument of NonNullList.of is the default value and doesn't actually go in the list :|
				ingredients = NonNullList.of(null, base, addition);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
			ingredients = recipe.getIngredients();

		for(Ingredient ingredient : ingredients)
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