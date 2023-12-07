package com.mraof.minestuck.api.alchemy;

import java.util.*;

/**
 * A grist set that can be modified after creation.
 * See {@link GristSet#mutableCopy()} for a quick way to create a mutable grist set from an existing grist set.
 */
public final class DefaultMutableGristSet implements MutableGristSet
{
	private final Map<GristType, Long> gristMap = new TreeMap<>(), mapView = Collections.unmodifiableMap(gristMap);

	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public DefaultMutableGristSet()
	{}
	
	public DefaultMutableGristSet(GristSet set)
	{
		this(set.asAmounts());
	}
	
	public DefaultMutableGristSet(Iterable<GristAmount> amounts)
	{
		for(GristAmount amount : amounts)
			this.set(amount.type(), amount.amount());
	}
	
	@Override
	public Map<GristType, Long> asMap()
	{
		return this.mapView;
	}
	
	@Override
	public MutableGristSet set(GristType type, long amount)
	{
		Objects.requireNonNull(type);
		this.gristMap.put(type, amount);
		return this;
	}
	
	@Override
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		build.append("gristSet:[");

		boolean first = true;
		for (Map.Entry<GristType, Long> entry : gristMap.entrySet())
		{
			if (!first)
				build.append(',');
			build.append(entry.getKey()).append("=").append(entry.getValue());
			first = false;
		}

		build.append(']');
		return build.toString();
	}
}
