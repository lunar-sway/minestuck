package com.mraof.minestuck.api.alchemy;

/**
 * A version of {@link GristSet}, but with the extra guarantee that this set cannot be changed.
 * Suitable for things like recipes, where the set should not be allowed to change after the recipe was loaded.
 * Can be obtained from any grist set by calling {@link GristSet#asImmutable()}.
 */
public interface ImmutableGristSet extends GristSet
{
	@Override
	default ImmutableGristSet asImmutable()
	{
		return this;
	}
}
