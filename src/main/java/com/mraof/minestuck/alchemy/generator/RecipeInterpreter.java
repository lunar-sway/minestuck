package com.mraof.minestuck.alchemy.generator;

import com.mraof.minestuck.alchemy.GristSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface RecipeInterpreter
{
	List<Item> getOutputItems(Recipe<?> recipe);
	
	GristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context);
	
	InterpreterSerializer<?> getSerializer();
}