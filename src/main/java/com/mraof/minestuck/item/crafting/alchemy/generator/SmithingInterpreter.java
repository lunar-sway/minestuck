package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Field;

public class SmithingInterpreter extends DefaultInterpreter
{
	public static final SmithingInterpreter INSTANCE = new SmithingInterpreter();

	@ObjectHolder("minestuck:smithing")
	public static final InterpreterSerializer<SmithingInterpreter> SERIALIZER = null;

	@Override
	public GristSet generateCost(IRecipe<?> recipe, Item output, GenerationContext context)
	{
		// SmithingRecipes don't list their ingredients as ingredients so use this as workaround
		try
		{
			GristSet totalCost = new GristSet();

			Ingredient base = (Ingredient)ObfuscationReflectionHelper.findField(SmithingRecipe.class, "field_234837_a_").get(recipe);
			GristSet baseCost = context.costForIngredient(base, true);
			if(baseCost == null)
				return null;
			else totalCost.addGrist(baseCost);

			Ingredient addition = (Ingredient)ObfuscationReflectionHelper.findField(SmithingRecipe.class, "field_234838_b_").get(recipe);
			GristSet additionCost = context.costForIngredient(addition, true);
			if(additionCost == null)
				return null;
			else totalCost.addGrist(additionCost);

			totalCost.scale(1F/recipe.getResultItem().getCount(), false);	//Do not round down because it's better to have something cost a little to much than it possibly costing nothing

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
		return SERIALIZER;
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