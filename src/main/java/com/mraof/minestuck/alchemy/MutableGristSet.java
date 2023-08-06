package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.api.alchemy.GristType;

import java.util.function.Supplier;

public interface MutableGristSet extends GristSet
{
	Codec<MutableGristSet> CODEC = GristAmount.LIST_CODEC.xmap(DefaultMutableGristSet::new, MutableGristSet::asAmounts);
	
	static MutableGristSet newDefault()
	{
		return new DefaultMutableGristSet();
	}
	
	@Override
	default ImmutableGristSet asImmutable()
	{
		return new DefaultImmutableGristSet(this);
	}
	
	MutableGristSet set(GristType type, long amount);
	
	default MutableGristSet add(GristType type, long amount)
	{
		return this.set(type, this.getGrist(type) + amount);
	}
	
	default MutableGristSet add(Supplier<GristType> type, long amount)
	{
		return this.add(type.get(), amount);
	}
	
	default MutableGristSet add(GristSet set)
	{
		for (GristAmount grist : set.asAmounts())
			this.add(grist.type(), grist.amount());
		
		return this;
	}
	
	default MutableGristSet scale(float scale, boolean roundDown)
	{
		for(GristAmount gristAmount : this.asAmounts())
		{
			long amount = gristAmount.amount();
			if(amount != 0)
				this.set(gristAmount.type(), roundDown ? (long) (amount * scale) : roundToNonZero(amount * scale));
		}
		
		return this;
	}
	
	default MutableGristSet scale(int scale)
	{
		return scale(scale, true);
	}
	
	private static int roundToNonZero(float value)
	{
		if(value < 0)
			return Math.min(-1, Math.round(value));
		else return Math.max(1, Math.round(value));
	}
}
