package com.mraof.minestuck.api.alchemy;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.*;

/**
 * An interface for anything that might contain grist.
 * There are several different implementations of this interface with different properties.
 * <p>
 * If you want to limit yourself to a set that doesn't change after creation, check out {@link ImmutableGristSet}.
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
	
	ImmutableGristSet asImmutable();
	
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
	
	ImmutableGristSet EMPTY = Collections::emptyMap;
	
	static ImmutableGristSet of(GristAmount... amounts)
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
	
	static void write(GristSet gristSet, FriendlyByteBuf buffer)
	{
		Collection<GristAmount> amounts = gristSet.asAmounts();
		buffer.writeInt(amounts.size());
		amounts.forEach(gristAmount -> gristAmount.write(buffer));
	}
	
	static ImmutableGristSet read(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		List<GristAmount> list = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
			list.add(GristAmount.read(buffer));
		
		return DefaultImmutableGristSet.create(list);
	}
}
