package com.mraof.minestuck.item.crafting.alchemy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerRegistry;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;


public class GristGutter extends GristSet
{
	public int gutterTotal = -1;
	
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
	//todo: what happens to grist gutter super overflow
	
	@Override
	public GristGutter addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			this.gristTypes.compute(type, (key, value) ->
					value == null ? amount : value + amount);
			gutterTotal += amount;
		
		}
		return this;
	}
	
	

	public GristGutter logGutter(GristType type, long amount){
		/**
		 we're using a simple logger that'll tell us how much grist is going into the gutter by updating us on
		 the current gutter total, followed by the "value" (which just means the type of grist getting added)
		 */
		
	LOGGER.debug("Gutter before adding " + amount + " "
			+ type.getDisplayName().getString() + " grist:");
	LOGGER.debug("Total: " + getGutterTotal());
	LOGGER.debug("Value: " + getValue());
	
	
	for(GristType t : this.gristTypes.keySet())
	{
		LOGGER.debug(t.getDisplayName().toString() + ": " + gristTypes.get(t));
	}
		
		LOGGER.debug("Gutter AFTER adding " + amount + " "
				+ type.getDisplayName().toString() + " grist:");
		LOGGER.debug("Total: " + getGutterTotal());
		LOGGER.debug("Value: " + getValue());
		
		for(GristType t : this.gristTypes.keySet())
		{
			LOGGER.debug(t.getDisplayName().toString() + ": "
					+ gristTypes.get(t));
			
		}
		
		return this;
	}
	
	
	
	/**
this is how we take grist from the gutter and throw it into the player's cache
	 we use the xMover variable (comprised of the minimum of something or other)...
	 */
	public GristSet splice(int i)
	{
		GristSet spliceSet = new GristSet();
		
		for(GristType t : this.gristTypes.keySet())
		{
			int xMover = (int) Math.min(gristTypes.get(t), i);
			spliceSet.addGrist(t, xMover);
			
			gutterTotal -= xMover;
		}
		return spliceSet;
	}
	
}