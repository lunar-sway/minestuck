package com.mraof.minestuck.alchemy;

import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface IGristSet
{
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
	
	boolean isEmpty();
	
	IImmutableGristSet asImmutable();
	
	default GristSet mutableCopy()
	{
		return new GristSet(this);
	}
	
	default Component asTextComponent()
	{
		Component component = null;
		for(GristAmount grist : asAmounts())
		{
			if(component == null)
				component = grist.asTextComponent();
			else component = Component.translatable(GristSet.GRIST_COMMA, component, grist.asTextComponent());
		}
		if(component != null)
			return component;
		else return Component.empty();
	}
	
	default Component createMissingMessage()
	{
		return Component.translatable(GristSet.MISSING_MESSAGE, asTextComponent());
	}
	
	IImmutableGristSet EMPTY = new IImmutableGristSet()
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
}
