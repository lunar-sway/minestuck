package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class SmithingInterpreter implements RecipeInterpreter
{
	public static final SmithingInterpreter INSTANCE = new SmithingInterpreter();
	
	private static final Field templateField = ObfuscationReflectionHelper.findField(SmithingTransformRecipe.class, "f_265949_");
	private static final Field baseField = ObfuscationReflectionHelper.findField(SmithingTransformRecipe.class, "f_265888_");
	private static final Field additionField = ObfuscationReflectionHelper.findField(SmithingTransformRecipe.class, "f_265907_");
	
	@Override
	public List<Item> getOutputItems(Recipe<?> recipe)
	{
		ItemStack stack = recipe.getResultItem(null);
		return stack.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack.getItem());
	}
	
	@Override
	public GristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context)
	{
		// UpgradeRecipes don't list their ingredients as ingredients so use this as workaround
		try
		{
			MutableGristSet totalCost = MutableGristSet.newDefault();
			
			Ingredient template = (Ingredient)templateField.get(recipe);
			GristSet templateCost = context.costForIngredient(template, true);
			if(templateCost == null)
				return null;
			totalCost.add(templateCost);

			Ingredient base = (Ingredient)baseField.get(recipe);
			GristSet baseCost = context.costForIngredient(base, true);
			if(baseCost == null)
				return null;
			totalCost.add(baseCost);

			Ingredient addition = (Ingredient)additionField.get(recipe);
			GristSet additionCost = context.costForIngredient(addition, true);
			if(additionCost == null)
				return null;
			totalCost.add(additionCost);

			totalCost.scale(1F/recipe.getResultItem(null).getCount(), false);	//Do not round down because it's better to have something cost a little to much than it possibly costing nothing

			return totalCost;
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public InterpreterSerializer<?> getSerializer()
	{
		return InterpreterSerializers.SMITHING.get();
	}

	public static class Serializer extends InterpreterSerializer<SmithingInterpreter>
	{
		@Override
		public SmithingInterpreter read(JsonElement json)
		{
			return INSTANCE;
		}

		@Override
		public JsonElement write(SmithingInterpreter interpreter)
		{
			return JsonNull.INSTANCE;
		}
	}
}