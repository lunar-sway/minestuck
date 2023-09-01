package com.mraof.minestuck.api.alchemy.recipe.generator;

import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

/**
 * Something that can provide grist costs during grist cost generation,
 * possibly caching the generated grist cost if it needed to be generated based on the cost of other items.
 */
public interface GeneratedCostProvider
{
	/**
	 * @param item the item to provide a grist cost for.
	 * @param callback context for the cost generation, providing the recursive call for getting a grist cost for another item, and is used to determine whether to cache the result.
	 * @return The grist cost result from this function. Null result means that no result was found, while a non-null result of a null grist cost means that the item explicitly is unalchemizable.
	 */
	@Nullable
	GristCostResult generate(Item item, GeneratorCallback callback);
	
	/**
	 * Called once when grist cost generation is done.
	 */
	default void build()
	{}
	
	default void reportPreliminaryLookups(LookupTracker tracker)
	{}
	
	default void onCostFromOtherRecipe(Item item, GristCostResult lastCost, GeneratorCallback callback)
	{}
}