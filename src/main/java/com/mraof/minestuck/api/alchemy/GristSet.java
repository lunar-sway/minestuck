package com.mraof.minestuck.api.alchemy;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An interface for anything that might contain grist.
 * There are several different implementations of this interface with different properties.
 * <p>
 * If you want to limit yourself to a set that doesn't change after creation, check out {@link GristSet.Immutable}.
 * You can create an immutable grist set using {@link GristSet#of(GristAmount...)},
 * or you can create an instance of {@link DefaultImmutableGristSet} directly.
 * You can convert any grist set into an immutable grist set using {@link GristSet#asImmutable()}.
 * <p>
 * If you want a grist set that you can modify after creation, check out {@link MutableGristSet}.
 * You can create a normal mutable grist set using {@link MutableGristSet#newDefault()}.
 * You can also create a mutable copy of any grist set using {@link GristSet#mutableCopy()}.
 * There's also {@link NonNegativeGristSet}, which is a mutable grist set that throws an exception if any amount goes below zero.
 */
public interface GristSet
{
	String GRIST_COMMA = "grist.comma";
	
	default long getGrist(GristType type)
	{
		return this.asMap().getOrDefault(type, 0L);
	}
	
	default boolean hasType(GristType type)
	{
		return this.asMap().containsKey(type);
	}
	
	default boolean isEmpty()
	{
		return this.asMap().values().stream().allMatch(amount -> amount == 0);
	}
	
	default boolean equalContent(GristSet other)
	{
		for(GristType type : GristTypes.REGISTRY)
			if(this.getGrist(type) != other.getGrist(type))
				return false;
		return true;
	}
	
	/**
	 * @return a value estimate for this grist set
	 */
	default double getValue()
	{
		double sum = 0;
		for(GristAmount amount : asAmounts())
			sum += amount.getValue();
		return sum;
	}
	
	/**
	 * @return an unmodifiable map with all grist types and amounts contained in this grist set.
	 */
	Map<GristType, Long> asMap();
	
	GristSet.Immutable asImmutable();
	
	default MutableGristSet mutableCopy()
	{
		return new DefaultMutableGristSet(this);
	}
	
	default List<GristAmount> asAmounts()
	{
		return this.asMap().entrySet().stream().map(entry -> new GristAmount(entry.getKey(), entry.getValue())).toList();
	}
	
	default Component asTextComponent()
	{
		Component component = null;
		for(GristAmount grist : asAmounts())
		{
			if(component == null)
				component = grist.asTextComponent();
			else component = Component.translatable(GRIST_COMMA, component, grist.asTextComponent());
		}
		if(component != null)
			return component;
		else return Component.empty();
	}
	
	GristSet.Immutable EMPTY = Collections::emptyMap;
	
	static GristSet.Immutable of(GristAmount... amounts)
	{
		if(amounts.length == 0)
			return GristSet.EMPTY;
		if(amounts.length == 1)
			return amounts[0];
		
		var builder = ImmutableMap.<GristType, Long>builder();
		for(GristAmount amount : amounts)
			builder.put(amount.type(), amount.amount());
		return new DefaultImmutableGristSet(builder);
	}
	
	/**
	 * A version of {@link GristSet}, but with the extra guarantee that this set cannot be changed.
	 * Suitable for things like recipes, where the set should not be allowed to change after the recipe was loaded.
	 * Can be obtained from any grist set by calling {@link GristSet#asImmutable()}.
	 */
	interface Immutable extends GristSet
	{
		@Override
		default Immutable asImmutable()
		{
			return this;
		}
	}
	
	/**
	 * Container for different codecs for grist sets.
	 * Codecs are not placed directly in the grist set class to avoid cyclic class loading between grist set and grist amount.
	 */
	final class Codecs
	{
		public static final Codec<GristSet.Immutable> NON_NEGATIVE_CODEC = GristAmount.NON_NEGATIVE_LIST_CODEC.xmap(DefaultImmutableGristSet::create, GristSet::asAmounts);
		/**
		 * Codec for serializing a grist set in a map format. Currently used for json serialization.
		 * Perhaps we should prefer this format for nbt-serialization as well? Something worth considering for the future.
		 */
		public static final Codec<GristSet.Immutable> MAP_CODEC = Codec.unboundedMap(GristTypes.REGISTRY.byNameCodec(), Codec.LONG).xmap(DefaultImmutableGristSet::new, GristSet::asMap);
		public static final Codec<GristSet.Immutable> LIST_CODEC = GristAmount.LIST_CODEC.xmap(DefaultImmutableGristSet::create, GristSet::asAmounts);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, GristSet.Immutable> STREAM_CODEC = StreamCodec.composite(
				GristAmount.STREAM_CODEC.apply(ByteBufCodecs.list()),
				GristSet::asAmounts,
				DefaultImmutableGristSet::create
		);
	}
}
