package com.mraof.minestuck.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public record EmptyEncodedUnitCodec<A>(A instance) implements Codec<A>
{
	@Override
	public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input)
	{
		return DataResult.success(Pair.of(instance, ops.empty()));
	}
	
	@Override
	public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix)
	{
		return DataResult.success(ops.empty());
	}
}
