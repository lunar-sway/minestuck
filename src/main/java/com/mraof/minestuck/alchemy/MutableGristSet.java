package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class MutableGristSet implements IGristSet
{
	public static final Codec<MutableGristSet> CODEC = GristAmount.LIST_CODEC.xmap(MutableGristSet::new, MutableGristSet::asAmounts);
	public static final String MISSING_MESSAGE = "grist.missing";
	public static final String GRIST_COMMA = "grist.comma";
	
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
	
	public MutableGristSet(Supplier<GristType>[] type, long[] amount)
	{
		this();
		
		for (int i = 0; i < type.length; i++)
		{
			this.gristTypes.put(type[i].get(), amount[i]);
		}
	}
	
	/**
	 * Creates a set of grist values with multiple grist/amount pairs. used in setting up the Grist Registry.
	 */
	public MutableGristSet(GristType[] type, long[] amount)
	{
		this();

		for (int i = 0; i < type.length; i++)
		{
			this.gristTypes.put(type[i], amount[i]);
		}
	}
	
	@Deprecated
	public MutableGristSet(GristAmount... grist)
	{
		this();
		for (GristAmount amount : grist)
		{
			this.gristTypes.put(amount.type(), amount.amount());
		}
	}
	
	public MutableGristSet(IGristSet set)
	{
		this();
		addGrist(set);
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
	public IImmutableGristSet asImmutable()
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
	public MutableGristSet addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			this.gristTypes.compute(type, (key, value) -> value == null ? amount : value + amount);
		}
		return this;
	}
	
	public MutableGristSet addGrist(Supplier<GristType> type, long amount)
	{
		return addGrist(type.get(), amount);
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
	
	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public MutableGristSet addGrist(IGristSet set)
	{
		for (GristAmount grist : set.asAmounts())
			this.addGrist(grist.type(), grist.amount());
		
		return this;
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
	
	public boolean equalContent(IGristSet other)
	{
		for(GristType type : GristTypes.values())
			if(this.getGrist(type) != other.getGrist(type))
				return false;
		return true;
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
	
	public static void write(IGristSet gristSet, FriendlyByteBuf buffer)
	{
		Collection<GristAmount> amounts = gristSet.asAmounts();
		buffer.writeInt(amounts.size());
		amounts.forEach(gristAmount -> gristAmount.write(buffer));
	}
	
	public static IGristSet read(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		GristAmount[] amounts = new GristAmount[size];
		for(int i = 0; i < size; i++)
			amounts[i] = GristAmount.read(buffer);
		
		return new MutableGristSet(amounts);
	}
}