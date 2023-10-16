package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.GristCostResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class GeneratedGristCostCache implements GeneratedCostProvider
{
	private static final Function<GeneratorCallback, GristSet> INVALID_PROVIDER = _callback -> {
		throw new UnsupportedOperationException();
	};
	
	@Nullable
	private ImmutableGristSet cachedCost = null;
	private boolean hasGeneratedCost = false;
	private final Function<GeneratorCallback, GristSet> costProvider;
	
	public GeneratedGristCostCache(Function<GeneratorCallback, GristSet> costProvider)
	{
		this.costProvider = costProvider;
	}
	
	public static GeneratedGristCostCache fromNetwork(FriendlyByteBuf buffer)
	{
		GeneratedGristCostCache cache = new GeneratedGristCostCache(INVALID_PROVIDER);
		cache.hasGeneratedCost = true;
		if(buffer.readBoolean())
			cache.cachedCost = GristSet.read(buffer);
		else
			cache.cachedCost = null;
		return cache;
	}
	
	public void toNetwork(FriendlyByteBuf buffer)
	{
		if(this.cachedCost != null)
		{
			buffer.writeBoolean(true);
			GristSet.write(this.cachedCost, buffer);
		} else
			buffer.writeBoolean(false);
	}
	
	@Nullable
	public ImmutableGristSet getCachedCost()
	{
		return this.cachedCost;
	}
	
	@Nullable
	@Override
	public GristCostResult generate(Item item, GeneratorCallback callback)
	{
		if(callback.shouldUseSavedResult() && this.hasGeneratedCost)
			return GristCostResult.ofOrNull(this.cachedCost);
		
		GristSet cost = this.costProvider.apply(callback);
		if(callback.shouldSaveResult())
		{
			this.hasGeneratedCost = true;
			if(cost != null)
				this.cachedCost = cost.asImmutable();
		}
		return GristCostResult.ofOrNull(cost);
	}
}
