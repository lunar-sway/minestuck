package com.mraof.minestuck.alchemy;

public interface IImmutableGristSet extends IGristSet
{
	@Override
	default IImmutableGristSet asImmutable()
	{
		return this;
	}
}
