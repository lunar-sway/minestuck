package com.mraof.minestuck.util;


import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class GristSet
{

	public TreeMap<GristType, Integer> gristTypes;

	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public GristSet()
	{
		this.gristTypes = new TreeMap<>();
	}

	public GristSet(Map<GristType, Integer> map)
	{
		this.gristTypes = new TreeMap<>(map);
	}

	/**
	 * Creates a set of grist values with one grist/amount pair. used in setting up the Grist Registry.
	 */
	public GristSet(GristType type, int amount)
	{
		this();
		this.gristTypes.put(type, amount);
	}

	/**
	 * Creates a set of grist values with multiple grist/amount pairs. used in setting up the Grist Registry.
	 */
	public GristSet(GristType[] type, int[] amount)
	{
		this();

		for (int i = 0; i < type.length; i++)
		{
			this.gristTypes.put(type[i], amount[i]);
		}
	}

	public GristSet(GristAmount... grist)
	{
		this();
		for (GristAmount amount : grist)
		{
			this.gristTypes.put(amount.getType(), amount.getAmount());
		}
	}

	/**
	 * Gets the amount of grist, given a type of grist.
	 */
	public int getGrist(GristType type)
	{
		return this.gristTypes.getOrDefault(type, 0);
	}
	
	/**
	 * @return a value estimate for this grist set
	 */
	public float getValue()
	{
		float sum = 0;
		for(GristAmount amount : getArray())
			sum += amount.getValue();
		return sum;
	}
	
	/**
	 * Sets the amount of grist, given a type of grist and the new amount.
	 */
	public GristSet setGrist(GristType type, int amount)
	{
		if(type != null)
			gristTypes.put(type, amount);
		return this;
	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristType type, int amount)
	{
		if (amount == 0)
		{
			this.gristTypes.remove(type);
		}
		else
		{
			this.gristTypes.compute(type, (key, value) -> value == null ? amount : value + amount);
		}
		return this;
	}

	/**
	 * Returns a Hashtable with grist->amount pairs.
	 */
	public TreeMap<GristType, Integer> getMap()
	{
		return this.gristTypes;
	}

	/**
	 * Returns a ArrayList containing GristAmount objects representing the set.
	 */
	public ArrayList<GristAmount> getArray()
	{
		return new ArrayList<>(this.gristTypes.entrySet().stream().map((entry) -> new GristAmount(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
	}

	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public GristSet addGrist(GristSet set)
	{
		for (GristAmount grist : set.getArray())
		{
			this.addGrist(grist);
		}
		return this;

	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristAmount grist)
	{
		this.addGrist(grist.getType(), grist.getAmount());
		return this;
	}

	/**
	 * Multiplies all the grist amounts by a factor.
	 */
	public GristSet scaleGrist(float scale)
	{

		this.gristTypes.forEach((type, amount) -> {
			if (amount > 0)
			{
				this.gristTypes.put(type, (int) Math.max(amount * scale, 1));
			}
		});

		return this;
	}

	/**
	 * Checks if this grist set is empty.
	 */
	public boolean isEmpty()
	{
		return this.gristTypes.isEmpty();
	}

	@Override
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		build.append("gristSet:[");

		boolean first = true;
		for (Map.Entry<GristType, Integer> entry : gristTypes.entrySet())
		{
			if (!first)
				build.append(',');
			build.append(entry.getKey().getRegistryName()).append("=").append(entry.getValue());
			first = false;
		}

		build.append(']');
		return build.toString();
	}

	public GristSet copy()
	{
		return new GristSet(this.gristTypes);
	}

}
