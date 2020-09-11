package com.mraof.minestuck.item.crafting.alchemy;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.function.Supplier;

public class ImmutableGristSet extends GristSet
{
	public ImmutableGristSet()
	{
		super(Collections.emptyMap());
	}
	
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
	
	@Override
	public ImmutableGristSet asImmutable()
	{
		return this;
	}
	
}