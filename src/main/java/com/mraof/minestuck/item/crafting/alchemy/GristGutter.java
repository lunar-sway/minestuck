package com.mraof.minestuck.item.crafting.alchemy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerRegistry;
import com.mraof.minestuck.item.crafting.alchemy.GristType;

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

	private static final Logger LOGGER = LogManager.getLogger();
	
	public long getGutterTotal()
	{
		if(gutterTotal < 0)
		{
			//TODO: Recalculate total
			// Then assign the newly-calculated total to gutterTotal.
		}
		return gutterTotal;
	}
	
	
	@Override
	public GristGutter addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			LOGGER.debug("Gutter before adding " + amount + " " + type.getDisplayName().getString() + " grist:");
			LOGGER.debug("Total: " + gutterTotal);
			LOGGER.debug("Value: " + getValue());
	
			
			for(GristType t : this.gristTypes.keySet())
			{
				LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
			}
			
			
			this.gristTypes.compute(type, (key, value) -> value == null ? amount : value + amount);
			gutterTotal += amount;
			
			
			LOGGER.debug("Gutter AFTER adding " + amount + " " + type.getDisplayName().toString() + " grist:");
			LOGGER.debug("Total: " + gutterTotal);
			LOGGER.debug("Value: " + getValue());
			
			for(GristType t : this.gristTypes.keySet())
			{
				LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
				
			}
		}
		return this;
	}
}