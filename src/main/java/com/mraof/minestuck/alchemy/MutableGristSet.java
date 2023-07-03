package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;

import java.util.*;
import java.util.function.Supplier;

/**
 * A grist set that can be modified after creation.
 * See {@link GristSet#mutableCopy()} for a quick way to create a mutable grist set from an existing grist set.
 */
public class MutableGristSet implements GristSet
{
	public static final Codec<MutableGristSet> CODEC = GristAmount.LIST_CODEC.xmap(MutableGristSet::new, MutableGristSet::asAmounts);
	
	private final Map<GristType, Long> gristTypes;

	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public MutableGristSet()
	{
		this(new TreeMap<>());
	}
	
	protected MutableGristSet(Map<GristType, Long> map)
	{
		this.gristTypes = map;
	}
	
	public MutableGristSet(Supplier<GristType> type, long amount)
	{
		this(type.get(), amount);
	}
	
	/**
	 * Creates a set of grist values with one grist/amount pair. used in setting up the Grist Registry.
	 */
	public MutableGristSet(GristType type, long amount)
	{
		this();
		this.gristTypes.put(type, amount);
	}
	
	public MutableGristSet(GristSet set)
	{
		this();
		add(set);
	}
	
	public MutableGristSet(Iterable<GristAmount> amounts)
	{
		this();
		for (GristAmount amount : amounts)
		{
			this.gristTypes.put(amount.type(), amount.amount());
		}
	}
	
	@Override
	public ImmutableGristSet asImmutable()
	{
		return new DefaultImmutableGristSet(this);
	}
	
	/**
	 * Gets the amount of grist, given a type of grist.
	 */
	@Override
	public long getGrist(GristType type)
	{
		return this.gristTypes.getOrDefault(type, 0L);
	}
	
	public long getGrist(Supplier<GristType> type)
	{
		return getGrist(type.get());
	}
	
	/**
	 * Sets the amount of grist, given a type of grist and the new amount.
	 */
	public MutableGristSet setGrist(GristType type, long amount)
	{
		if(type != null)
		{
			if (amount == 0)
			{
				this.gristTypes.remove(type);
			}
			else
			{
				gristTypes.put(type, amount);
			}
		}
		return this;
	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public MutableGristSet add(GristType type, long amount)
	{
		if(type != null)
		{
			this.gristTypes.compute(type, (key, value) -> value == null ? amount : value + amount);
		}
		return this;
	}
	
	public MutableGristSet add(Supplier<GristType> type, long amount)
	{
		return add(type.get(), amount);
	}
	
	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public MutableGristSet add(GristSet set)
	{
		for (GristAmount grist : set.asAmounts())
			this.add(grist.type(), grist.amount());
		
		return this;
	}
	
	public boolean hasType(GristType type)
	{
		return gristTypes.containsKey(type);
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
	
	public MutableGristSet scale(int scale)
	{
		return scale(scale, true);
	}
	
	/**
	 * Multiplies all the grist amounts by a factor.
	 */
	public MutableGristSet scale(float scale, boolean roundDown)
	{
		this.gristTypes.forEach((type, amount) -> {
			if (amount != 0)
			{
				this.gristTypes.put(type, roundDown ? (long) (amount * scale) : roundToNonZero(amount * scale));
			}
		});

		return this;
	}
	
	private int roundToNonZero(float value)
	{
		if(value < 0)
			return Math.min(-1, Math.round(value));
		else return Math.max(1, Math.round(value));
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
	
	@Override
	public MutableGristSet mutableCopy()
	{
		return new MutableGristSet(new TreeMap<>(gristTypes));
	}
}