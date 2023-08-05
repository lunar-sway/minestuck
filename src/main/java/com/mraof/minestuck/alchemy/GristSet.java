package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.*;

/**
 * An interface for anything that might contain grist.
 * For an implementation that can be modified, see {@link MutableGristSet}.
 * For an implementation that cannot be modified, see {@link DefaultImmutableGristSet}.
 * There's also an immutable version of the interface: {@link ImmutableGristSet}.
 */
public interface GristSet
{
	String MISSING_MESSAGE = "grist.missing";
	String GRIST_COMMA = "grist.comma";
	
	long getGrist(GristType type);
	
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
	
	List<GristAmount> asAmounts();
	
	Map<GristType, Long> asMap();
	
	boolean isEmpty();
	
	default boolean equalContent(GristSet other)
	{
		for(GristType type : GristTypes.values())
			if(this.getGrist(type) != other.getGrist(type))
				return false;
		return true;
	}
	
	ImmutableGristSet asImmutable();
	
	default MutableGristSet mutableCopy()
	{
		return new MutableGristSet(this);
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
	
	default Component createMissingMessage()
	{
		return Component.translatable(MISSING_MESSAGE, asTextComponent());
	}
	
	ImmutableGristSet EMPTY = new ImmutableGristSet()
	{
		@Override
		public long getGrist(GristType type)
		{
			return 0;
		}
		@Override
		public List<GristAmount> asAmounts()
		{
			return Collections.emptyList();
		}
		@Override
		public Map<GristType, Long> asMap()
		{
			return Collections.emptyMap();
		}
		@Override
		public boolean isEmpty()
		{
			return true;
		}
	};
	
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
