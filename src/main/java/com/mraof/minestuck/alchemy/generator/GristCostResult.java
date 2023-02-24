package com.mraof.minestuck.alchemy.generator;

import com.mraof.minestuck.alchemy.GristSet;

import javax.annotation.Nullable;

public class GristCostResult
{
	@Nullable
	private final GristSet cost;
	
	public GristCostResult(@Nullable GristSet cost)
	{
		this.cost = cost;
	}
	
	public static GristCostResult ofOrNull(@Nullable GristSet cost)
	{
		return cost != null ? new GristCostResult(cost) : null;
	}
	
	@Nullable
	public GristSet getCost()
	{
		return cost;
	}
}