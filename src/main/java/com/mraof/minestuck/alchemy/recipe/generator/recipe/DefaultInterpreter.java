package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collections;
import java.util.List;

//TODO interpreter (perhaps setting) that makes the interpreter not remove container cost for ingredient
public enum DefaultInterpreter implements RecipeInterpreter
{
	INSTANCE;
	
	public static final Codec<DefaultInterpreter> CODEC = Codec.unit(INSTANCE);
	
	@Override
	public List<Item> getOutputItems(Recipe<?> recipe)
	{
		ItemStack stack = recipe.getResultItem(null);
		return stack.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack.getItem());
	}
	
	@Override
	public MutableGristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback)
	{
		if(recipe.isSpecial())
			return null;
		
		MutableGristSet totalCost = MutableGristSet.newDefault();
		for(Ingredient ingredient : recipe.getIngredients())
		{
			GristSet ingredientCost = callback.lookupCostFor(ingredient);
			if(ingredientCost == null)
				return null;
			else totalCost.add(ingredientCost);
		}
		
		totalCost.scale(1F / recipe.getResultItem(null).getCount(), false);    //Do not round down because it's better to have something cost a little to much than it possibly costing nothing
		
		return totalCost;
	}
	
	@Override
	public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker)
	{
		recipe.getIngredients().forEach(tracker::report);
	}
	
	@Override
	public Codec<? extends RecipeInterpreter> codec()
	{
		return CODEC;
	}
}