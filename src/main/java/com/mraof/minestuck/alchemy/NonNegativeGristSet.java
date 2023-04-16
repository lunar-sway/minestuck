package com.mraof.minestuck.alchemy;

import net.minecraft.nbt.ListTag;

public class NonNegativeGristSet extends GristSet
{
	public NonNegativeGristSet()
	{
	
	}
	
	public NonNegativeGristSet(IGristSet set)
	{
		for(GristAmount amount : set.asAmounts())
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
	
	public static NonNegativeGristSet read(ListTag list)
	{
		NonNegativeGristSet set = new NonNegativeGristSet();
		for(int i = 0; i < list.size(); i++)
		{
			GristAmount amount = GristAmount.read(list.getCompound(i), null);
			if(amount.amount() >= 0)
				set.addGrist(amount);
		}
		
		return set;
	}
}