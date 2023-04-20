package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.IGristSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Acts as a pipeline and state holder for any {@link GeneratedCostProvider} back to {@link GristCostGenerator}.
 */
public class GenerationContext
{
	private final GenerationContext parent;
	private final Item itemGeneratedFor;
	private final HashMap<Item, IGristSet> localCache = new HashMap<>();
	private final Function<GenerationContext, IGristSet> itemLookup;
	private final boolean primary;
	private boolean shouldUseCache;
	
	GenerationContext(Item itemGeneratedFor, Function<GenerationContext, IGristSet> itemLookup)
	{
		parent = null;
		this.itemGeneratedFor = itemGeneratedFor;
		this.itemLookup = itemLookup;
		primary = true;
		shouldUseCache = true;
	}
	
	private GenerationContext(Item itemGeneratedFor, GenerationContext parent)
	{
		this.parent = parent;
		this.itemGeneratedFor = itemGeneratedFor;
		localCache.putAll(parent.localCache);
		itemLookup = parent.itemLookup;
		primary = false;
		shouldUseCache = parent.shouldUseCache;
	}
	
	private GenerationContext nextGeneration(Item itemGeneratedFor)
	{
		return new GenerationContext(itemGeneratedFor, this);
	}
	
	private boolean isItemInProcess(Item item)
	{
		return item == getCurrentItem() || parent != null && parent.isItemInProcess(item);
	}
	
	private boolean testWithGeneratedItems(Ingredient ingredient)
	{
		return ingredient.test(new ItemStack(itemGeneratedFor)) || parent != null && parent.testWithGeneratedItems(ingredient);
	}
	
	
	public Item getCurrentItem()
	{
		return itemGeneratedFor;
	}
	
	public boolean isPrimary()
	{
		return primary;
	}
	
	public boolean shouldUseCache()
	{
		return shouldUseCache;
	}
	
	public IGristSet costForIngredient(Ingredient ingredient, boolean removeContainerCost)
	{
		if(ingredient.test(ItemStack.EMPTY))
			return IGristSet.EMPTY;
		
		if(testWithGeneratedItems(ingredient))
			return null;	//If the ingredient tests positive for an item already in generation, prevent recursion and return a null grist cost
		
		IGristSet minCost = null;
		for(ItemStack stack : ingredient.getItems())
		{
			if(ingredient.test(new ItemStack(stack.getItem())))
			{
				IGristSet cost;
				if(removeContainerCost)
					cost = costWithoutContainer(stack);
				else cost = lookupCostFor(stack);
				
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
		}
		return minCost;
	}
	
	public IGristSet lookupCostFor(ItemStack stack)
	{
		return lookupCostFor(stack.getItem());
	}
	
	public IGristSet lookupCostFor(Item item)
	{
		if(!localCache.containsKey(item))
		{
			if(isItemInProcess(item))
				return null;
			
			IGristSet result = itemLookup.apply(nextGeneration(item));
			
			localCache.put(item, result);
			
			return result;
		} else return localCache.get(item);
	}
	
	public IGristSet costWithoutContainer(ItemStack stack)
	{
		IGristSet cost = lookupCostFor(stack);
		
		if(cost != null)
		{
			ItemStack container = stack.getCraftingRemainingItem();
			if(!container.isEmpty())
			{
				IGristSet containerCost = lookupCostFor(container);
				if(containerCost != null)
					return containerCost.mutableCopy().scale(-1).addGrist(cost);
				else return null;
			}
		}
		return cost;
	}
	
	public <S> S withoutCache(Supplier<S> supplier)
	{
		if(!shouldUseCache)
			return supplier.get();
		else
		{
			shouldUseCache = false;
			Map<Item, IGristSet> savedCache = new HashMap<>(localCache);
			localCache.clear();
			
			S value = supplier.get();
			
			localCache.putAll(savedCache);
			shouldUseCache = true;
			return value;
		}
	}
}