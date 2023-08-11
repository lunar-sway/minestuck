package com.mraof.minestuck.alchemy.recipe;

import com.mraof.minestuck.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import com.mraof.minestuck.alchemy.recipe.generator.GristCostResult;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class GeneratedGristCostCache implements GeneratedCostProvider
{
	private static final Function<GenerationContext, GristSet> INVALID_PROVIDER = context -> {
		throw new UnsupportedOperationException();
	};
	
	@Nullable
	private ImmutableGristSet cachedCost = null;
	private boolean hasGeneratedCost = false;
	private final Function<GenerationContext, GristSet> costProvider;
	
	public GeneratedGristCostCache(Function<GenerationContext, GristSet> costProvider)
	{
		this.costProvider = costProvider;
	}
	
	public static GeneratedGristCostCache empty(Function<GenerationContext, GristSet> costProvider)
	{
		return new GeneratedGristCostCache(costProvider);
	}
	
	public static GeneratedGristCostCache of(@Nullable ImmutableGristSet cost)
	{
		GeneratedGristCostCache cache = new GeneratedGristCostCache(INVALID_PROVIDER);
		cache.cachedCost = cost;
		cache.hasGeneratedCost = true;
		return cache;
	}
	
	@Nullable
	public ImmutableGristSet getCachedCost()
	{
		return this.cachedCost;
	}
	
	@Nullable
	@Override
	public GristCostResult generate(Item item, GenerationContext context)
	{
		if(context.shouldUseCache() && this.hasGeneratedCost)
			return GristCostResult.ofOrNull(this.cachedCost);
		
		GristSet cost = this.costProvider.apply(context);
		if(context.isPrimary())
		{
			this.hasGeneratedCost = true;
			if(cost != null)
				this.cachedCost = cost.asImmutable();
		}
		return GristCostResult.ofOrNull(cost);
	}
}
