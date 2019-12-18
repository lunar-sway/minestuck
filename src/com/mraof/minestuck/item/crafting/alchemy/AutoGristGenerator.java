package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Adds all the recipes that are based on the existing vanilla crafting registries, like grist conversions of items composed of other things.
 */
public class AutoGristGenerator
{
	
	private HashMap<Item, List<IRecipe>> recipeList;
	private HashSet<Item> lookedOver;
	private int returned = 0;
	
	/*
	public void prepare()
	{
		recipeList = new HashMap<>();
		
		Debug.debug("Looking for dynamic grist conversions...");
		for(IRecipe recipe : Lists.<IRecipe>newArrayList())	//TODO Figure out how to access recipes moving forward
		{
			try
			{
				if(recipe.isDynamic())
					continue;
				ItemStack output = recipe.getRecipeOutput();
				if(output.isEmpty())
					continue;
				Item item = output.getItem();
				
				if(!recipeList.containsKey(item))
					recipeList.put(item, new ArrayList<>());
				recipeList.get(item).add(recipe);
				
			} catch(NullPointerException e)
			{
				Debug.logger.warn(String.format("A null pointer exception was thrown for %s. This was not expected. Stacktrace: ", recipe), e);
			}
		}
		Debug.info("Found " + recipeList.size() + " nondynamic recipes.");
	}
	
	public void execute()
	{
		Debug.debug("Calculating grist conversion...");
		Iterator<Map.Entry<Item, List<IRecipe>>> it = recipeList.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Item, List<IRecipe>> pairs = it.next();
			boolean b = false;
			for(IRecipe recipe : pairs.getValue())
			{
				lookedOver = new HashSet<>();
				try
				{
					b = checkRecipe(recipe);
				} catch(Exception e)
				{
					Debug.logger.warn(String.format("Failed to look over recipe \"%s\" for \"%s\". Cause:", recipe, pairs.getKey()), e);
				}
				if(b)
					break;
			}
		}
		
		Debug.info("Added "+returned+" grist conversions.");
	}
	
	public GristSet lookupCostForItem(Item item)
	{
		GristSet cost = AlchemyCostRegistry.getGristConversion(item);
		if(cost != null)
			return cost;
		
		if(recipeList.containsKey(item))
		{
			List<IRecipe> recipes = recipeList.get(item);
			if(recipes == null)
				return null;
			boolean b = false;
			for(IRecipe recipe : recipes)
			{
				lookedOver = new HashSet<>();
				try
				{
					b = checkRecipe(recipe);
				} catch(Exception e)
				{
					Debug.logger.warn(String.format("Failed to look over recipe \"%s\" for \"%s\". Cause:", recipe, item), e);
				}
				if(b)
					return AlchemyCostRegistry.getGristConversion(item);
			}
		}
		return null;
	}
	
	private boolean checkRecipe(IRecipe<?> recipe)
	{
		ItemStack output = recipe.getRecipeOutput();
		if(AlchemyCostRegistry.getGristConversion(output) != null)
			return true;
		if(lookedOver.contains(output.getItem()))
		{
			return false;
		} else {
			lookedOver.add(output.getItem());
		}
		
		GristSet set = new GristSet();
		for(Ingredient ingredient : recipe.getIngredients())
		{
			if(!ingredient.isSimple())
			{
				return false;
			}
			
			GristSet ingrCost = findCostForIngredient(ingredient);
			
			if(ingrCost == null)
				return false;
			
			set.addGrist(ingrCost);
		}
		
		set.scale(1/ (float) output.getCount(), false);
		AlchemyCostRegistry.addGristConversion(output.getItem(), set);
		
		returned++;
		return true;
	}
	
	private GristSet findCostForIngredient(Ingredient ingredient)
	{
		if(ingredient == Ingredient.EMPTY)
			return new GristSet();
		
		GristSet ingrCost = null;
		for(ItemStack stack : ingredient.getMatchingStacks())
		{
			GristSet itemCost = findCostForItem(stack, true);
			if(itemCost != null && (ingrCost == null || itemCost.getValue() < ingrCost.getValue()))
				ingrCost = itemCost;
		}
		
		return ingrCost;
	}
	
	private GristSet findCostForItem(ItemStack item, boolean withoutContainer)
	{
		if(AlchemyCostRegistry.getGristConversion(item) != null)
		{
			return withoutContainer ? getCostWithoutContainer(item) : AlchemyCostRegistry.getGristConversion(item);
		} else if(!item.isEmpty())
		{
			List<IRecipe> subrecipes = recipeList.get(item.getItem());
			if(subrecipes != null)
			{
				for(IRecipe recipe : subrecipes)
					if(checkRecipe(recipe))
					{
						return withoutContainer ? getCostWithoutContainer(item) : AlchemyCostRegistry.getGristConversion(item);
					}
			}
		}
		return null;
	}
	
	//Assumes that the grist cost for the stack has been checked to not be null beforehand
	private GristSet getCostWithoutContainer(ItemStack stack)
	{
		GristSet cost = AlchemyCostRegistry.getGristConversion(stack);
		if(stack.getItem().hasContainerItem(stack))
		{
			ItemStack container = stack.getItem().getContainerItem(stack);
			GristSet containerCost = findCostForItem(container, false);
			if(containerCost != null)
				for(GristAmount amount : containerCost.getArray())
					cost.setGrist(amount.getType(), Math.max(0, cost.getGrist(amount.getType()) - amount.getAmount()));
		}
		return cost;
	}*/
}
