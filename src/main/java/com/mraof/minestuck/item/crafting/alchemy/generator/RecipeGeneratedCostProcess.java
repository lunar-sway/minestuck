package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

class RecipeGeneratedCostProcess
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<Item, List<Pair<Recipe<?>, RecipeInterpreter>>> lookupMap;
	private final Map<Item, GristSet> generatedCosts = new HashMap<>();
	
	RecipeGeneratedCostProcess(Map<Item, List<Pair<Recipe<?>, RecipeInterpreter>>> lookupMap)
	{
		this.lookupMap = lookupMap;
	}
	
	Map<Item, GristSet> buildMap()
	{
		//Clean out null grist costs
		generatedCosts.entrySet().removeIf(entry -> entry.getValue() == null);
		
		return ImmutableMap.copyOf(generatedCosts);
	}
	
	Set<Item> itemSet()
	{
		return lookupMap.keySet();
	}
	
	GristCostResult generateCost(Item item, GristCostResult lastCost, GenerationContext context)
	{
		if(lastCost != null)
		{
			if(context.isPrimary())
				checkRecipeLogging(item, lastCost.getCost(), context);
			return lastCost;
		} else if(generatedCosts.containsKey(item))
		{
			return GristCostResult.ofOrNull(generatedCosts.get(item));
		} else
		{
			GristSet result = costFromRecipes(item, true, context);
			//TODO Clean cost of entries with 0, set it to null if it is empty (no free cookies for you). Also log these events so that the costs of base ingredients can be modified accordingly
			
			if(context.isPrimary())
				generatedCosts.put(item, result);
			return GristCostResult.ofOrNull(result);
		}
	}
	
	private GristSet costFromRecipes(Item item, boolean isCostNull, GenerationContext context)
	{
		List<Pair<Recipe<?>, RecipeInterpreter>> recipes = lookupMap.getOrDefault(item, Collections.emptyList());
		
		if(!recipes.isEmpty())
		{
			GristSet minCost = null;
			for(Pair<Recipe<?>, RecipeInterpreter> recipePair : recipes)
			{
				GristSet cost = costForRecipe(recipePair.getLeft(), recipePair.getRight(), item, context);
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
			return minCost;
		} else
		{
			if(MinestuckConfig.COMMON.logIngredientItemsWithoutCosts.get() && isCostNull)
				LOGGER.info("Item {} was looked up, but it did not have any grist costs or recipes.", item.getRegistryName());
			return null;
		}
	}
	
	private GristSet costForRecipe(Recipe<?> recipe, RecipeInterpreter interpreter, Item item, GenerationContext context)
	{
		try
		{
			return interpreter.generateCost(recipe, item, context);
		} catch(Exception e)
		{
			LOGGER.error("Got exception while getting cost for recipe {}", recipe.getId(), e);
			return null;
		}
	}
	
	private void checkRecipeLogging(Item item, GristSet cost, GenerationContext context)
	{
		if(MinestuckConfig.COMMON.logItemsWithRecipeAndCost.get())
		{
			GristSet generatedCost = context.withoutCache(() -> costFromRecipes(item, false, context));
			
			if(generatedCost != null)
			{
				if(generatedCost.getValue() < cost.getValue())
					LOGGER.warn("Found item {} with grist cost recipe greater than a potential generated grist cost. Recipe cost: {}, generated cost: {}", item.getRegistryName(), cost, generatedCost);
				else LOGGER.info("Found item {} with grist cost recipe that is also valid for grist cost generation. Recipe cost: {}, generated cost: {}", item.getRegistryName(), cost, generatedCost);
			}
		}
	}
}