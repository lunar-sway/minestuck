package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;

class RecipeGeneratedCostProcess
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final boolean LOG_ITEMS_WITHOUT_RECIPES = true;
	private static final boolean LOG_ITEMS_WITH_RECIPE_AND_COST = true;
	
	private final Map<Item, List<Pair<IRecipe<?>, RecipeInterpreter>>> lookupMap;
	private final Map<Item, GristSet> generatedCosts = new HashMap<>();
	
	RecipeGeneratedCostProcess(Map<Item, List<Pair<IRecipe<?>, RecipeInterpreter>>> lookupMap)
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
	
	GristCostResult generateCost(Item item, GristCostResult lastCost, boolean primary, Function<Item, GristSet> itemLookup)
	{
		if(lastCost != null)
		{
			if(primary)
				checkRecipeLogging(item, lastCost.getCost(), itemLookup);
			return lastCost;
		} else if(generatedCosts.containsKey(item))
		{
			return GristCostResult.ofOrNull(generatedCosts.get(item));
		} else
		{
			GristSet result = costFromRecipes(item, true, itemLookup);
			//TODO Clean cost of entries with 0, set it to null if it is empty (no free cookies for you). Also log these events so that the costs of base ingredients can be modified accordingly
			
			if(primary)
				generatedCosts.put(item, result);
			return GristCostResult.ofOrNull(result);
		}
	}
	
	private GristSet costFromRecipes(Item item, boolean isCostNull, Function<Item, GristSet> itemLookup)
	{
		List<Pair<IRecipe<?>, RecipeInterpreter>> recipes = lookupMap.getOrDefault(item, Collections.emptyList());
		
		if(!recipes.isEmpty())
		{
			GristSet minCost = null;
			for(Pair<IRecipe<?>, RecipeInterpreter> recipePair : recipes)
			{
				GristSet cost = costForRecipe(recipePair.getFirst(), recipePair.getSecond(), item, itemLookup);
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
			return minCost;
		} else
		{
			if(LOG_ITEMS_WITHOUT_RECIPES && isCostNull)
				LOGGER.info("Item {} was looked up, but it did not have any grist costs or recipes.", item.getRegistryName());
			return null;
		}
	}
	
	private GristSet costForRecipe(IRecipe<?> recipe, RecipeInterpreter interpreter, Item item, Function<Item, GristSet> itemLookup)
	{
		try
		{
			return interpreter.generateCost(recipe, item, (ingredient, removeContainer) -> costForIngredient(ingredient, removeContainer, itemLookup));
		} catch(Exception e)
		{
			LOGGER.error("Got exception while getting cost for recipe {}", recipe.getId(), e);
			return null;
		}
	}
	
	private GristSet costForIngredient(Ingredient ingredient, boolean removeContainerCost, Function<Item, GristSet> itemLookup)
	{
		if(ingredient.test(ItemStack.EMPTY))
			return GristSet.EMPTY;
		
		GristSet minCost = null;
		for(ItemStack stack : ingredient.getMatchingStacks())
		{
			if(ingredient.test(new ItemStack(stack.getItem())))
			{
				GristSet cost = itemLookup.apply(stack.getItem());
				
				if(removeContainerCost && cost != null)
					cost = removeContainerCost(stack, cost, itemLookup);
				
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
		}
		return minCost;
	}
	
	private GristSet removeContainerCost(ItemStack stack, GristSet cost, Function<Item, GristSet> itemLookup)
	{
		ItemStack container = stack.getContainerItem();
		if(!container.isEmpty())
		{
			GristSet containerCost = itemLookup.apply(container.getItem());
			if(containerCost != null)
				return containerCost.copy().scale(-1).addGrist(cost);
			else return null;
		}
		return cost;
	}
	
	private void checkRecipeLogging(Item item, GristSet cost, Function<Item, GristSet> itemLookup)
	{
		if(LOG_ITEMS_WITH_RECIPE_AND_COST)
		{
			GristSet generatedCost = costFromRecipes(item, false, itemLookup);
			
			if(generatedCost != null)
				LOGGER.info("Found item {} with grist cost recipe that is also valid for grist cost generation. Recipe cost: {}, generated cost: {}", item.getRegistryName(), cost, generatedCost);
		}
	}
}