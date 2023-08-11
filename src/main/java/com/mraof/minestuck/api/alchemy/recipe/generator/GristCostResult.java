package com.mraof.minestuck.api.alchemy.recipe.generator;

import com.mraof.minestuck.api.alchemy.GristSet;

import javax.annotation.Nullable;

public record GristCostResult(@Nullable GristSet cost)
{
	public static GristCostResult ofOrNull(@Nullable GristSet cost)
	{
		return cost != null ? new GristCostResult(cost) : null;
	}
}
