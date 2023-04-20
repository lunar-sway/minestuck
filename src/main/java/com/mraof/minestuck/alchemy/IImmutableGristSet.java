package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;

public interface IImmutableGristSet extends IGristSet
{
	Codec<IImmutableGristSet> NON_NEGATIVE_CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(ImmutableGristSet::create, IImmutableGristSet::asAmounts);
	
	@Override
	default IImmutableGristSet asImmutable()
	{
		return this;
	}
}
