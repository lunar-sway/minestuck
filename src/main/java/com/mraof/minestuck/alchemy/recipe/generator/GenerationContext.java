package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
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
public final class GenerationContext implements GeneratorCallback
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
	
	private boolean testWithGeneratedItems(Ingredient ingredient)
	{
		return ingredient.test(new ItemStack(itemGeneratedFor)) || parent != null && parent.testWithGeneratedItems(ingredient);
	}
	
	
	public Item getCurrentItem()
	{
		return itemGeneratedFor;
	}
	
	@Override
	public boolean shouldSaveResult()
	{
		return primary;
	}
	
	@Override
	public boolean shouldUseSavedResult()
	{
		return shouldUseCache;
	}
	
	@Override
	public GristSet lookupCostFor(Ingredient ingredient)
	{
		if(ingredient.test(ItemStack.EMPTY))
			return GristSet.EMPTY;
		
		if(testWithGeneratedItems(ingredient))
			return null;	//If the ingredient tests positive for an item already in generation, prevent recursion and return a null grist cost
		
		GristSet minCost = null;
		for(ItemStack stack : ingredient.getItems())
		{
			if(ingredient.test(new ItemStack(stack.getItem())))
			{
				GristSet cost = costWithoutContainer(stack);
				
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
		}
		return minCost;
	}
	
	@Override
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
			ItemStack container = stack.getCraftingRemainingItem();
			if(!container.isEmpty())
			{
				GristSet containerCost = lookupCostFor(container);
				if(containerCost != null)
					return containerCost.mutableCopy().scale(-1).add(cost);
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