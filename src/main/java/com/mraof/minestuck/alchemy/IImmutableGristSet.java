package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;

import java.util.Map;

public interface IImmutableGristSet extends IGristSet
{
	Codec<IImmutableGristSet> NON_NEGATIVE_CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(ImmutableGristSet::create, IImmutableGristSet::asAmounts);
	/**
	 * Codec for serializing a grist set in a map format. Currently used for json serialization.
	 * Perhaps we should prefer this format for nbt-serialization as well? Something worth considering for the future.
	 */
	Codec<IImmutableGristSet> MAP_CODEC = Codec.unboundedMap(GristTypes.getRegistry().getCodec(), Codec.LONG).xmap(ImmutableGristSet::new, IImmutableGristSet::asMap);
	
	@Override
	default IImmutableGristSet asImmutable()
	{
		return this;
	}
}
