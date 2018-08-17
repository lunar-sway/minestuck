package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.modSupport.Minegicka3Support;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.*;

/**
 * Adds all the recipes that are based on the existing vanilla crafting registries, like grist conversions of items composed of other things.
 */
public class AutoGristGenerator
{
	
	private HashMap<Integer, List<IRecipe>> recipeList;
	private HashSet<Integer> lookedOver;
	private int returned = 0;
	
	
	public void prepare()
	{
		recipeList = new HashMap<>();
		
		Debug.debug("Looking for dynamic grist conversions...");
		for(IRecipe recipe : CraftingManager.REGISTRY)
		{
			try
			{
				if(recipe.isDynamic())
					continue;
				ItemStack output = recipe.getRecipeOutput();
				if(output.isEmpty())
					continue;
				int param = RecipeItemHelper.pack(output);
				
				if(!recipeList.containsKey(param))
					recipeList.put(param, new ArrayList<>());
				recipeList.get(param).add(recipe);
				
			} catch(NullPointerException e)
			{
				Debug.logger.warn(String.format("A null pointer exception was thrown for %s. This was not expected. Stacktrace: ", recipe), e);
			}
		}
		Debug.info("Found " + recipeList.size() + " nondynamic recipes.");
	}
	
	public void excecute()
	{
		Debug.debug("Calculating grist conversion...");
		Iterator<Map.Entry<Integer, List<IRecipe>>> it = recipeList.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer, List<IRecipe>> pairs = it.next();
			boolean b = false;
			for(IRecipe recipe : pairs.getValue())
			{
				lookedOver = new HashSet<>();
				try
				{
					b = checkRecipe(recipe);
				} catch(Exception e)
				{
					Debug.logger.warn(String.format("Failed to look over recipe \"%s\" for \"%s\". Cause:", recipe, RecipeItemHelper.unpack(pairs.getKey())), e);
				}
				if(b)
					break;
			}
		}
		
		Debug.info("Added "+returned+" grist conversions.");
	}
	
	public GristSet lookupCostForItem(ItemStack item)
	{
		GristSet cost = GristRegistry.getGristConversion(item);
		if(cost != null)
			return cost;
		
		int i = RecipeItemHelper.pack(item);
		if(recipeList.containsKey(i))
		{
			List<IRecipe> recipes = recipeList.get(i);
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
					return GristRegistry.getGristConversion(item);
			}
		}
		return null;
	}
	
	private boolean checkRecipe(IRecipe recipe)
	{
		ItemStack output = recipe.getRecipeOutput();
		if(GristRegistry.getGristConversion(output) != null)
			return true;
		if(lookedOver.contains(RecipeItemHelper.pack(output)))
		{
			return false;
		} else {
			lookedOver.add(RecipeItemHelper.pack(output));
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
		
		set.scaleGrist(1/ (float) output.getCount());
		GristRegistry.addGristConversion(output, output.getHasSubtypes(), set);
		
		returned ++;
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
		if(GristRegistry.getGristConversion(item) != null)
		{
			return withoutContainer ? getCostWithoutContainer(item) : GristRegistry.getGristConversion(item);
		} else if(!item.isEmpty())
		{
			List<IRecipe> subrecipes = recipeList.get(RecipeItemHelper.pack(item));
			if(subrecipes != null)
			{
				for(IRecipe recipe : subrecipes)
					if(checkRecipe(recipe))
					{
						return withoutContainer ? getCostWithoutContainer(item) : GristRegistry.getGristConversion(item);
					}
			}
		}
		return null;
	}
	
	//Assumes that the grist cost for the stack has been checked to not be null beforehand
	private GristSet getCostWithoutContainer(ItemStack stack)
	{
		GristSet cost = GristRegistry.getGristConversion(stack);
		if(stack.getItem().hasContainerItem(stack))
		{
			ItemStack container = stack.getItem().getContainerItem(stack);
			GristSet containerCost = findCostForItem(container, false);
			if(containerCost != null)
				for(GristAmount amount : containerCost.getArray())
					cost.setGrist(amount.getType(), Math.max(0, cost.getGrist(amount.getType()) - amount.getAmount()));
		}
		return cost;
	}
}
