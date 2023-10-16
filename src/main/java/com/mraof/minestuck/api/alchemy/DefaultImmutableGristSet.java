package com.mraof.minestuck.api.alchemy;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * An immutable grist set that can hold any number of grist types.
 * Suitable when an immutable grist set is needed, but {@link GristAmount} is insufficient.
 */
public final class DefaultImmutableGristSet implements ImmutableGristSet
{
	private final ImmutableMap<GristType, Long> map;
	
	public DefaultImmutableGristSet(GristSet set)
	{
		this(set.asMap());
	}
	
	public DefaultImmutableGristSet(Map<GristType, Long> grist)
	{
		map = ImmutableMap.copyOf(grist);
	}
	
	public DefaultImmutableGristSet(ImmutableMap.Builder<GristType, Long> builder)
	{
		map = builder.build();
	}
	
	public static ImmutableGristSet create(List<GristAmount> amounts)
	{
		ImmutableMap.Builder<GristType, Long> builder = ImmutableMap.builder();
		for(GristAmount gristAmount : amounts)
			builder.put(gristAmount.type(), gristAmount.amount());
		return new DefaultImmutableGristSet(builder);
	}
	
	@Override
	public Map<GristType, Long> asMap()
	{
		return this.map;
	}
}