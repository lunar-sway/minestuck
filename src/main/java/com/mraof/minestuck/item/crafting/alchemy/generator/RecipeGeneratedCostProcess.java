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
		List<Pair<IRecipe<?>, RecipeInterpreter>> recipes = lookupMap.getOrDefault(item, Collections.emptyList());
		
		if(!recipes.isEmpty())
		{
			GristSet minCost = null;
			for(Pair<IRecipe<?>, RecipeInterpreter> recipePair : recipes)
			{
				GristSet cost = costForRecipe(recipePair.getFirst(), recipePair.getSecond(), item, context);
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
	
	private GristSet costForRecipe(IRecipe<?> recipe, RecipeInterpreter interpreter, Item item, GenerationContext context)
	{
		try
		{
			return interpreter.generateCost(recipe, item, (ingredient, removeContainer) -> costForIngredient(ingredient, removeContainer, context));
		} catch(Exception e)
		{
			LOGGER.error("Got exception while getting cost for recipe {}", recipe.getId(), e);
			return null;
		}
	}
	
	private GristSet costForIngredient(Ingredient ingredient, boolean removeContainerCost, GenerationContext context)
	{
		if(ingredient.test(ItemStack.EMPTY))
			return GristSet.EMPTY;
		
		GristSet minCost = null;
		for(ItemStack stack : ingredient.getMatchingStacks())
		{
			if(ingredient.test(new ItemStack(stack.getItem())))
			{
				GristSet cost = context.lookupCostFor(stack.getItem());
				
				if(removeContainerCost && cost != null)
					cost = removeContainerCost(stack, cost, context);
				
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
		}
		return minCost;
	}
	
	private GristSet removeContainerCost(ItemStack stack, GristSet cost, GenerationContext context)
	{
		ItemStack container = stack.getContainerItem();
		if(!container.isEmpty())
		{
			GristSet containerCost = context.lookupCostFor(container);
			if(containerCost != null)
				return containerCost.copy().scale(-1).addGrist(cost);
			else return null;
		}
		return cost;
	}
	
	private void checkRecipeLogging(Item item, GristSet cost, GenerationContext context)
	{
		if(LOG_ITEMS_WITH_RECIPE_AND_COST)
		{
			GristSet generatedCost = context.withoutCache(() -> costFromRecipes(item, false, context));
			
			if(generatedCost != null)
				LOGGER.info("Found item {} with grist cost recipe that is also valid for grist cost generation. Recipe cost: {}, generated cost: {}", item.getRegistryName(), cost, generatedCost);
		}
	}
}