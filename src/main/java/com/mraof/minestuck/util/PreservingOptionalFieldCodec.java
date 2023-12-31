package com.mraof.minestuck.util;

import com.mojang.serialization.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A version of {@link com.mojang.serialization.codecs.OptionalFieldCodec} that preserves any partial result or error from the wrapped codec.
 * Note: In mc1.20.2+, this codec can be replaced by {@code ExtraCodecs.strictOptionalField()}.
 */
public final class PreservingOptionalFieldCodec<A> extends MapCodec<Optional<A>>
{
	public static <A> MapCodec<List<A>> forList(Codec<List<A>> elementCodec, String name)
	{
		return new PreservingOptionalFieldCodec<>(elementCodec, name)
				.xmap(optional -> optional.orElse(Collections.emptyList()),
						list -> list.isEmpty() ? Optional.empty() : Optional.of(list));
	}
	
	private final Codec<A> elementCodec;
	private final String name;
	
	public PreservingOptionalFieldCodec(Codec<A> elementCodec, String name)
	{
		this.elementCodec = elementCodec;
		this.name = name;
	}
	
	@Override
	public <T> Stream<T> keys(DynamicOps<T> ops)
	{
		return Stream.of(ops.createString(this.name));
	}
	
	@Override
	public <T> DataResult<Optional<A>> decode(DynamicOps<T> ops, MapLike<T> input)
	{
		T value = input.get(name);
		if(value == null)
			return DataResult.success(Optional.empty());
		
		return this.elementCodec.parse(ops, value).map(Optional::of);
	}
	
	@Override
	public <T> RecordBuilder<T> encode(Optional<A> input, DynamicOps<T> ops, RecordBuilder<T> prefix)
	{
		if(input.isPresent())
			return prefix.add(this.name, this.elementCodec.encodeStart(ops, input.get()));
		return prefix;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PreservingOptionalFieldCodec<?> that = (PreservingOptionalFieldCodec<?>) o;
		return Objects.equals(elementCodec, that.elementCodec) && Objects.equals(name, that.name);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(elementCodec, name);
	}
	
	@Override
	public String toString()
	{
		return "PreservingOptionalFieldCodec[" + name + ": " + elementCodec + ']';
	}
}
