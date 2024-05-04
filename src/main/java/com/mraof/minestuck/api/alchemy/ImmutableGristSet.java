package com.mraof.minestuck.api.alchemy;

import com.mojang.serialization.Codec;

/**
 * A version of {@link GristSet}, but with the extra guarantee that this set cannot be changed.
 * Suitable for things like recipes, where the set should not be allowed to change after the recipe was loaded.
 * Can be obtained from any grist set by calling {@link GristSet#asImmutable()}.
 */
public interface ImmutableGristSet extends GristSet
{
	Codec<ImmutableGristSet> NON_NEGATIVE_CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(DefaultImmutableGristSet::create, ImmutableGristSet::asAmounts);
	/**
	 * Codec for serializing a grist set in a map format. Currently used for json serialization.
	 * Perhaps we should prefer this format for nbt-serialization as well? Something worth considering for the future.
	 */
	Codec<ImmutableGristSet> MAP_CODEC = Codec.unboundedMap(GristTypes.REGISTRY.byNameCodec(), Codec.LONG).xmap(DefaultImmutableGristSet::new, ImmutableGristSet::asMap);
	Codec<ImmutableGristSet> LIST_CODEC = GristAmount.LIST_CODEC.xmap(DefaultImmutableGristSet::create, ImmutableGristSet::asAmounts);
	
	@Override
	default ImmutableGristSet asImmutable()
	{
		return this;
	}
}
