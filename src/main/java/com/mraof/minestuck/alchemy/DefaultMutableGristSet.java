package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.api.alchemy.GristType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * A grist set that can be modified after creation.
 * See {@link GristSet#mutableCopy()} for a quick way to create a mutable grist set from an existing grist set.
 */
public final class DefaultMutableGristSet implements MutableGristSet
{
	private final Map<GristType, Long> gristTypes;

	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public DefaultMutableGristSet()
	{
		this.gristTypes = new TreeMap<>();
	}
	
	public DefaultMutableGristSet(GristSet set)
	{
		this();
		add(set);
	}
	
	public DefaultMutableGristSet(Iterable<GristAmount> amounts)
	{
		this();
		for (GristAmount amount : amounts)
		{
			this.gristTypes.put(amount.type(), amount.amount());
		}
	}
	
	/**
	 * Gets the amount of grist, given a type of grist.
	 */
	@Override
	public long getGrist(GristType type)
	{
		return this.gristTypes.getOrDefault(type, 0L);
	}
	
	@Override
	public MutableGristSet set(GristType type, long amount)
	{
		Objects.requireNonNull(type);
		this.gristTypes.put(type, amount);
		return this;
	}
	
	/**
	 * Returns a ArrayList containing GristAmount objects representing the set.
	 */
	@Override
	public List<GristAmount> asAmounts()
	{
		return this.gristTypes.entrySet().stream().map((entry) -> new GristAmount(entry.getKey(), entry.getValue())).toList();
	}
	
	@Override
	public Map<GristType, Long> asMap()
	{
		return this.gristTypes;
	}
	
	/**
	 * Checks if this grist set is empty.
	 */
	@Override
	public boolean isEmpty()
	{
		return this.gristTypes.values().stream().allMatch(amount -> amount == 0);
	}
	
	@Override
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		build.append("gristSet:[");

		boolean first = true;
		for (Map.Entry<GristType, Long> entry : gristTypes.entrySet())
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