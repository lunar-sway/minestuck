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

public final class GeneratedGristCostCache
{
	@Nullable
	private ImmutableGristSet cachedCost = null;
	private boolean hasGeneratedCost = false;
	
	public void fromNetwork(FriendlyByteBuf buffer)
	{
		this.hasGeneratedCost = true;
		if(buffer.readBoolean())
			this.cachedCost = GristSet.read(buffer);
		else
			this.cachedCost = null;
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
	
	public GeneratedCostProvider generatedProvider(Function<GeneratorCallback, GristSet> costProvider)
	{
		return new GeneratedCostProvider()
		{
			@Nullable
			@Override
			public GristCostResult generate(Item item, GeneratorCallback callback)
			{
				if(callback.shouldUseSavedResult() && hasGeneratedCost)
					return GristCostResult.ofOrNull(cachedCost);
				
				GristSet cost = costProvider.apply(callback);
				if(callback.shouldSaveResult())
				{
					hasGeneratedCost = true;
					if(cost != null)
						cachedCost = cost.asImmutable();
				}
				return GristCostResult.ofOrNull(cost);
			}
		};
	}
}
