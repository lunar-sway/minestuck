package com.mraof.minestuck.api.alchemy;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Map;

/**
 * A {@link MutableGristSet}, which ensures that the grist set never reaches negative amounts
 * and instead throws an exception when an operation is made that would otherwise set a negative amount.
 */
public final class NonNegativeGristSet implements MutableGristSet
{
	public static Codec<NonNegativeGristSet> CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(NonNegativeGristSet::new, NonNegativeGristSet::asAmounts);
	
	private final DefaultMutableGristSet wrappedSet = new DefaultMutableGristSet();
	
	public NonNegativeGristSet()
	{}
	
	public NonNegativeGristSet(GristSet set)
	{
		this(set.asAmounts());
	}
	
	public NonNegativeGristSet(List<GristAmount> amounts)
	{
		for(GristAmount amount : amounts)
			this.set(amount.type(), amount.amount());
	}
	
	@Override
	public Map<GristType, Long> asMap()
	{
		return wrappedSet.asMap();
	}
	
	@Override
	public NonNegativeGristSet set(GristType type, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Negative grist amounts not allowed!");
		
		wrappedSet.set(type, amount);
		return this;
	}
}