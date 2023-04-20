package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.ListTag;

import java.util.List;

public class NonNegativeGristSet extends GristSet
{
	public static Codec<NonNegativeGristSet> CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(NonNegativeGristSet::new, NonNegativeGristSet::asAmounts);
	
	public NonNegativeGristSet()
	{
	
	}
	
	public NonNegativeGristSet(IGristSet set)
	{
		this(set.asAmounts());
	}
	
	public NonNegativeGristSet(List<GristAmount> amounts)
	{
		for(GristAmount amount : amounts)
			if(amount.amount() < 0)
				throw new IllegalArgumentException("Can't create a non-negative grist set with negative "+amount.type());
			else addGrist(amount);
	}
	
	@Override
	public GristSet setGrist(GristType type, long amount)
	{
		if(amount < 0)
			throw new IllegalArgumentException("Negative values not allowed!");
		return super.setGrist(type, amount);
	}
	
	@Override
	public GristSet addGrist(GristType type, long amount)
	{
		if(getGrist(type) + amount < 0)
		{
			throw new IllegalArgumentException("Grist count may not go below 0" + " Type " + type.getDisplayName().toString() + " has " + getGrist(type) + " and adding " + amount);
		}
		return super.addGrist(type, amount);
	}
	
	@Override
	public GristSet scale(float scale, boolean roundDown)
	{
		if(scale < 0)
			throw new IllegalArgumentException("Negative values not allowed!");
		return super.scale(scale, roundDown);
	}
}