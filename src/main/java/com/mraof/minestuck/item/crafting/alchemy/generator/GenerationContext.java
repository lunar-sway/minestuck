package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

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
	private final HashMap<Item, GristSet> localCache = new HashMap<>();
	private final Function<GenerationContext, GristSet> itemLookup;
	private final boolean primary;
	private boolean shouldUseCache;
	
	GenerationContext(Item itemGeneratedFor, Function<GenerationContext, GristSet> itemLookup)
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
	
	public GristSet costForIngredient(Ingredient ingredient, boolean removeContainerCost)
	{
		if(ingredient.test(ItemStack.EMPTY))
			return GristSet.EMPTY;
		
		GristSet minCost = null;
		for(ItemStack stack : ingredient.getMatchingStacks())
		{
			if(ingredient.test(new ItemStack(stack.getItem())))
			{
				GristSet cost;
				if(removeContainerCost)
					cost = costWithoutContainer(stack);
				else cost = lookupCostFor(stack);
				
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
		}
		return minCost;
	}
	
	public GristSet lookupCostFor(ItemStack stack)
	{
		return lookupCostFor(stack.getItem());
	}
	
	public GristSet lookupCostFor(Item item)
	{
		if(!localCache.containsKey(item))
		{
			if(isItemInProcess(item))
				return null;
			
			GristSet result = itemLookup.apply(nextGeneration(item));
			
			localCache.put(item, result);
			
			return result;
		} else return localCache.get(item);
	}
	
	public GristSet costWithoutContainer(ItemStack stack)
	{
		GristSet cost = lookupCostFor(stack);
		
		if(cost != null)
		{
			ItemStack container = stack.getContainerItem();
			if(!container.isEmpty())
			{
				GristSet containerCost = lookupCostFor(container);
				if(containerCost != null)
					return containerCost.copy().scale(-1).addGrist(cost);
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
			Map<Item, GristSet> savedCache = new HashMap<>(localCache);
			localCache.clear();
			
			S value = supplier.get();
			
			localCache.putAll(savedCache);
			shouldUseCache = true;
			return value;
		}
	}
}