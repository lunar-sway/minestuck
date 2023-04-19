package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.alchemy.IGristSet;

import javax.annotation.Nullable;

public class GristCostResult
{
	@Nullable
	private final IGristSet cost;
	
	public GristCostResult(@Nullable IGristSet cost)
	{
		this.cost = cost;
	}
	
	public static GristCostResult ofOrNull(@Nullable IGristSet cost)
	{
		return cost != null ? new GristCostResult(cost) : null;
	}
	
	@Nullable
	public IGristSet getCost()
	{
		return cost;
	}
}