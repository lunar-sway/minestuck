package com.mraof.minestuck.alchemy;

import com.mojang.serialization.Codec;

public interface ImmutableGristSet extends GristSet
{
	Codec<ImmutableGristSet> NON_NEGATIVE_CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(DefaultImmutableGristSet::create, ImmutableGristSet::asAmounts);
	/**
	 * Codec for serializing a grist set in a map format. Currently used for json serialization.
	 * Perhaps we should prefer this format for nbt-serialization as well? Something worth considering for the future.
	 */
	Codec<ImmutableGristSet> MAP_CODEC = Codec.unboundedMap(GristTypes.getRegistry().getCodec(), Codec.LONG).xmap(DefaultImmutableGristSet::new, ImmutableGristSet::asMap);
	
	@Override
	default ImmutableGristSet asImmutable()
	{
		return this;
	}
}
