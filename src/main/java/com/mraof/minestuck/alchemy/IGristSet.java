package com.mraof.minestuck.alchemy;

import net.minecraft.network.chat.Component;

import java.util.Collection;

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
	
	Collection<GristAmount> asAmounts();
	
	boolean isEmpty();
	
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
}
