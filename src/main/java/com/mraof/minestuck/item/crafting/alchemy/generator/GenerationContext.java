package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Acts as a pipeline and state holder for any {@link GeneratedCostProvider} back to {@link GristCostGenerator}.
 */
public class GenerationContext
{
	//This cache is only meant to help
	private final HashMap<Item, GristSet> localCache = new HashMap<>();
	private final BiFunction<Item, GenerationContext, GristSet> itemLookup;
	private final boolean primary;
	private boolean shouldUseCache;
	
	GenerationContext(BiFunction<Item, GenerationContext, GristSet> itemLookup)
	{
		this.itemLookup = itemLookup;
		primary = true;
		shouldUseCache = true;
	}
	
	private GenerationContext(GenerationContext parent)
	{
		localCache.putAll(parent.localCache);
		itemLookup = parent.itemLookup;
		primary = false;
		shouldUseCache = parent.shouldUseCache;
	}
	
	public boolean isPrimary()
	{
		return primary;
	}
	
	public boolean shouldUseCache()
	{
		return shouldUseCache;
	}
	
	private GenerationContext nextGeneration()
	{
		return new GenerationContext(this);
	}
	
	public GristSet lookupCostFor(Item item)
	{
		if(!localCache.containsKey(item))
		{
			GristSet result = itemLookup.apply(item, nextGeneration());
			
			localCache.put(item, result);
			
			return result;
		} else return localCache.get(item);
	}
	
	public GristSet lookupCostFor(ItemStack stack)
	{
		return lookupCostFor(stack.getItem());
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