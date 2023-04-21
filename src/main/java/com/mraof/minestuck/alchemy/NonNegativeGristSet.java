package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;

import java.util.List;

/**
 * A version of {@link MutableGristSet}, which ensures that the grist set never reaches negative amounts
 * and instead throws an exception when an operation is made that would otherwise set a negative amount.
 */
public class NonNegativeGristSet extends MutableGristSet
{
	public static Codec<NonNegativeGristSet> CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(NonNegativeGristSet::new, NonNegativeGristSet::asAmounts);
	
	public NonNegativeGristSet()
	{
	
	}
	
	public NonNegativeGristSet(GristSet set)
	{
		this(set.asAmounts());
	}
	
	public NonNegativeGristSet(List<GristAmount> amounts)
	{
		for(GristAmount amount : amounts)
			if(amount.amount() < 0)
				throw new IllegalArgumentException("Can't create a non-negative grist set with negative "+amount.type());
			else add(amount);
	}
	
	@Override
	public MutableGristSet setGrist(GristType type, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Negative values not allowed!");
		return super.setGrist(type, amount);
	}
	
	@Override
	public MutableGristSet add(GristType type, long amount)
	{
		if(getGrist(type) + amount < 0)
		{
			throw new IllegalArgumentException("Grist count may not go below 0" + " Type " + type.getDisplayName().toString() + " has " + getGrist(type) + " and adding " + amount);
		}
		return super.add(type, amount);
	}
	
	@Override
	public MutableGristSet scale(float scale, boolean roundDown)
	{
		if(scale < 0)
			throw new IllegalArgumentException("Negative values not allowed!");
		return super.scale(scale, roundDown);
	}
}