package com.mraof.minestuck.api.alchemy;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

/**
 * A version of {@link GristSet} that can be modified after creation.
 * To get a new empty instance, use {@link MutableGristSet#newDefault()}.
 * A mutable copy can be made from any grist set using {@link GristSet#mutableCopy()}.
 * See also {@link NonNegativeGristSet}.
 */
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
