package com.mraof.minestuck.item.crafting.alchemy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerRegistry;

/**
 this is intended to tell the system "this is the max grist for each type(which is something equal and arbitrary)"
 that adds up to our desired amount.
 */

/**
at the current time of writing this, it's under review whether it'll be used.
the main reason being, if ever someone would want to add a new grist type, they would have to set the GUTTER_CAPACITY
higher.
 */
public class GristGutter extends GristSet
{
	public int gutterTotal;
	
	public static final int GUTTER_CAPACITY = 1000;
		/*
	public static long getGristLimitPerType()
	{
		/*
		Collection<GristType> values = GristTypes.values();
		int gutterCapacity = GUTTER_CAPACITY;
		
		long gutterTotal = gutterCapacity / values.size();
		
		return gutterTotal;
		
		return GUTTER_CAPACITY; */
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public GristGutter addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			this.gristTypes.compute(type, (key, value)
					-> value == null ? amount : value + amount);
			gutterTotal += amount;
			LOGGER.debug("total gutter amount currently: " + gutterTotal);
			LOGGER.debug("total gutter value currently: " + getValue());
		}
		return this;
	}

}