package com.mraof.minestuck.alchemy.generator.recipe;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.generator.GenerationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface RecipeInterpreter
{
	List<Item> getOutputItems(Recipe<?> recipe);
	
	GristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context);
	
	InterpreterSerializer<?> getSerializer();
}