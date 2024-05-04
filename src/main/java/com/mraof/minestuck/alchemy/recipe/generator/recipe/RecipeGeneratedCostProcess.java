package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.GristCostResult;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

class RecipeGeneratedCostProcess
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<Item, List<Pair<RecipeHolder<?>, RecipeInterpreter>>> lookupMap;
	private final Map<Item, ImmutableGristSet> generatedCosts = new HashMap<>();
	
	RecipeGeneratedCostProcess(Map<Item, List<Pair<RecipeHolder<?>, RecipeInterpreter>>> lookupMap)
	{
		this.lookupMap = lookupMap;
	}
	
	Map<Item, ImmutableGristSet> buildMap()
	{
		//Clean out null grist costs
		generatedCosts.entrySet().removeIf(entry -> entry.getValue() == null);
		
		return ImmutableMap.copyOf(generatedCosts);
	}
	
	Set<Item> itemSet()
	{
		return lookupMap.keySet();
	}
	
	void reportPreliminaryLookups(LookupTracker tracker)
	{
		for(List<Pair<RecipeHolder<?>, RecipeInterpreter>> recipes : this.lookupMap.values())
		{
			for(Pair<RecipeHolder<?>, RecipeInterpreter> recipe : recipes)
			{
				recipe.getValue().reportPreliminaryLookups(recipe.getKey().value(), tracker);
			}
		}
	}
	
	GristCostResult generateCost(Item item, GeneratorCallback callback)
	{
		if(generatedCosts.containsKey(item))
		{
			return GristCostResult.ofOrNull(generatedCosts.get(item));
		} else
		{
			GristSet result = costFromRecipes(item, callback);
			//TODO Clean cost of entries with 0, set it to null if it is empty (no free cookies for you). Also log these events so that the costs of base ingredients can be modified accordingly
			
			if(callback.shouldSaveResult())
				generatedCosts.put(item, result == null ? null : result.asImmutable());
			return GristCostResult.ofOrNull(result);
		}
	}
	
	void onCostFromOtherRecipe(Item item, GristCostResult lastCost, GeneratorCallback callback)
	{
		if(callback.shouldSaveResult())
			checkRecipeLogging(item, lastCost.cost(), (GenerationContext) callback);
	}
	
	private GristSet costFromRecipes(Item item, GeneratorCallback callback)
	{
		List<Pair<RecipeHolder<?>, RecipeInterpreter>> recipes = lookupMap.getOrDefault(item, Collections.emptyList());
		
		if(!recipes.isEmpty())
		{
			GristSet minCost = null;
			for(Pair<RecipeHolder<?>, RecipeInterpreter> recipePair : recipes)
			{
				GristSet cost = costForRecipe(recipePair.getLeft(), recipePair.getRight(), item, callback);
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
			return minCost;
		} else
		{
			return null;
		}
	}
	
	private GristSet costForRecipe(RecipeHolder<?> recipe, RecipeInterpreter interpreter, Item item, GeneratorCallback callback)
	{
		try
		{
			return interpreter.generateCost(recipe.value(), item, callback);
		} catch(Exception e)
		{
			LOGGER.error("Got exception while getting cost for recipe {}", recipe.id(), e);
			return null;
		}
	}
	
	private void checkRecipeLogging(Item item, GristSet cost, GenerationContext context)
	{
		if(MinestuckConfig.COMMON.logItemsWithRecipeAndCost.get())
		{
			GristSet generatedCost = context.withoutCache(() -> costFromRecipes(item, context));
			
			if(generatedCost != null)
			{
				if(generatedCost.getValue() < cost.getValue())
					LOGGER.warn("Found item {} with grist cost recipe greater than a potential generated grist cost. Recipe cost: {}, generated cost: {}", item, cost, generatedCost);
				else LOGGER.info("Found item {} with grist cost recipe that is also valid for grist cost generation. Recipe cost: {}, generated cost: {}", item, cost, generatedCost);
			}
		}
	}
}