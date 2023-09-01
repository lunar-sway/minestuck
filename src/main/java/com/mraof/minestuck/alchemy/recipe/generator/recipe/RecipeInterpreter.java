package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface RecipeInterpreter
{
	List<Item> getOutputItems(Recipe<?> recipe);
	
	GristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback);
	
	default void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker)
	{}
	
	InterpreterSerializer<?> getSerializer();
}