package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public enum SmithingInterpreter implements RecipeInterpreter
{
	INSTANCE;
	
	public static final Codec<SmithingInterpreter> CODEC = Codec.unit(INSTANCE);
	
	private static final Field templateField = ObfuscationReflectionHelper.findField(SmithingTransformRecipe.class, "template");
	private static final Field baseField = ObfuscationReflectionHelper.findField(SmithingTransformRecipe.class, "base");
	private static final Field additionField = ObfuscationReflectionHelper.findField(SmithingTransformRecipe.class, "addition");
	
	@Override
	public List<Item> getOutputItems(Recipe<?> recipe)
	{
		ItemStack stack = recipe.getResultItem(null);
		return stack.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack.getItem());
	}
	
	@Override
	public GristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback)
	{
		// UpgradeRecipes don't list their ingredients as ingredients so use this as workaround
		try
		{
			MutableGristSet totalCost = MutableGristSet.newDefault();
			
			Ingredient template = (Ingredient) templateField.get(recipe);
			GristSet templateCost = callback.lookupCostFor(template);
			if(templateCost == null)
				return null;
			totalCost.add(templateCost);
			
			Ingredient base = (Ingredient) baseField.get(recipe);
			GristSet baseCost = callback.lookupCostFor(base);
			if(baseCost == null)
				return null;
			totalCost.add(baseCost);
			
			Ingredient addition = (Ingredient) additionField.get(recipe);
			GristSet additionCost = callback.lookupCostFor(addition);
			if(additionCost == null)
				return null;
			totalCost.add(additionCost);
			
			totalCost.scale(1F / recipe.getResultItem(null).getCount(), false);    //Do not round down because it's better to have something cost a little to much than it possibly costing nothing
			
			return totalCost;
		} catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Codec<? extends RecipeInterpreter> codec()
	{
		return CODEC;
	}
}