package com.mraof.minestuck.alchemy;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ImmutableGristSet extends GristSet implements IImmutableGristSet
{
	public ImmutableGristSet(GristSet set)
	{
		super(ImmutableMap.copyOf(set.getMap()));
	}
	
	public ImmutableGristSet(ImmutableMap.Builder<GristType, Long> builder)
	{
		super(builder.build());
	}
	
	public ImmutableGristSet(GristType type, long amount)
	{
		super(ImmutableMap.of(type, amount));
	}
	
	public ImmutableGristSet(Supplier<GristType> type, long amount)
	{
		this(type.get(), amount);
	}
	
	public ImmutableGristSet(Map<GristType, Long> grist)
	{
		super(ImmutableMap.copyOf(grist));
	}
	
	public static ImmutableGristSet create(List<GristAmount> amounts)
	{
		ImmutableMap.Builder<GristType, Long> builder = ImmutableMap.builder();
		for(GristAmount gristAmount : amounts)
			builder.put(gristAmount.type(), gristAmount.amount());
		return new ImmutableGristSet(builder);
	}
	
	@Override
	public Map<GristType, Long> asMap()
	{
		return this.getMap();
	}
}