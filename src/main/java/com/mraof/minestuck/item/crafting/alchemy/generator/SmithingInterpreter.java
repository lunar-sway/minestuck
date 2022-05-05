package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Field;

public class SmithingInterpreter extends DefaultInterpreter
{
	public static final SmithingInterpreter INSTANCE = new SmithingInterpreter();

	private static final Field baseField = ObfuscationReflectionHelper.findField(UpgradeRecipe.class, "f_44518_");
	private static final Field additionField = ObfuscationReflectionHelper.findField(UpgradeRecipe.class, "f_44519_");

	@ObjectHolder("minestuck:smithing")
	public static final InterpreterSerializer<SmithingInterpreter> SERIALIZER = null;

	@Override
	public GristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context)
	{
		// UpgradeRecipes don't list their ingredients as ingredients so use this as workaround
		try
		{
			GristSet totalCost = new GristSet();

			Ingredient base = (Ingredient)baseField.get(recipe);
			GristSet baseCost = context.costForIngredient(base, true);
			if(baseCost == null)
				return null;
			else totalCost.addGrist(baseCost);

			Ingredient addition = (Ingredient)additionField.get(recipe);
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