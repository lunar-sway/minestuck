package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GenerationContext
{
	private final BiFunction<Item, GenerationContext, GristSet> itemLookup;
	private final boolean primary;
	private boolean shouldUseCache = true;
	
	GenerationContext(BiFunction<Item, GenerationContext, GristSet> itemLookup)
	{
		this(itemLookup, true, true);
	}
	
	private GenerationContext(BiFunction<Item, GenerationContext, GristSet> itemLookup, boolean primary, boolean shouldUseCache)
	{
		this.itemLookup = itemLookup;
		this.primary = primary;
		this.shouldUseCache = shouldUseCache;
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
		return isPrimary() ? new GenerationContext(itemLookup, false, shouldUseCache) : this;
	}
	
	public GristSet lookupCostFor(Item item)
	{
		return itemLookup.apply(item, nextGeneration());
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
			S value = supplier.get();
			shouldUseCache = true;
			return value;
		}
	}
}